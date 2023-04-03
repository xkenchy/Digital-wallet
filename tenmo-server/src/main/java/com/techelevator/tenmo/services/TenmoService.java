package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.exception.IdNotFoundException;
import com.techelevator.tenmo.exception.NotEnoughMoneyException;
import com.techelevator.tenmo.exception.NotYourAccountException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Component
public class TenmoService {
    private final JdbcUserDao userDao;
    private final JdbcAccountDao accountDao;
    private final JdbcTransferDao transferDao;

    public TenmoService(JdbcUserDao userDao, JdbcAccountDao accountDao, JdbcTransferDao jdbcTransferDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = jdbcTransferDao;
    }


    public BigDecimal getBalance(int id, String loggedInUser) throws NotYourAccountException, IdNotFoundException{
        User accountOwner = userDao.getUserById(id);
        if (accountOwner != null) {
            String ownerUsername = accountOwner.getUsername();
            if (ownerUsername.equals(loggedInUser)) {
                return accountDao.getBalance(id);
            } else {
                throw new NotYourAccountException("Forbidden account, make sure to enter correct id");
            }
        } else {
            throw new IdNotFoundException("Please enter a correct Id");
        }
    }
    // make this a transaction??????
    @Transactional
    public BigDecimal sendBucks(Transfer transfer, String loggedInUser) throws NotEnoughMoneyException, NotYourAccountException, IdNotFoundException {
       //setting variables for security check: fromId and fromName given to us
       int givenUserId = transfer.getUserIdFrom();
       String givenUsername = transfer.getFromName();

       //setting variable for security check: fromId expected based on Principal
       int actualUserIdFrom = userDao.findIdByUsername(loggedInUser);
       String actualUsername = loggedInUser;
       // checking if given user id is valid, ensuring database call returns a value
       User userTo = userDao.getUserById(transfer.getUserIdTo());
        if (userTo != null) {

            // Security: checking if given values match expected values. We only care which account money is coming from
            if (givenUsername.equals((actualUsername)) &&  givenUserId == actualUserIdFrom) {
                //finishing transfer object to be submitted to database
                transfer.setUserIdFrom(actualUserIdFrom);

                //If a user (not in CLI) passes a transfer json body with account id's that do not match user ids, we change them here. Weird, but all we could think of.
                int accountToId = accountDao.getAccountIdByUserId(transfer.getUserIdTo());
                int accountFromId = accountDao.getAccountIdByUserId(transfer.getUserIdFrom());
                transfer.setAccountTo(accountToId);
                transfer.setAccountFrom(accountFromId);

                //checking if fromAccount has sufficient funds
                if (transfer.getAmount().compareTo(accountDao.getBalance(actualUserIdFrom)) <= 0) {
                    transferDao.createTransfer(transfer, accountFromId, accountToId);
                    accountDao.addBalance(transfer.getAmount(), transfer.getUserIdTo());
                    return accountDao.subtractBalance(transfer.getAmount(), actualUserIdFrom);
                }
                throw new NotEnoughMoneyException("Insufficient Funds brouhaha");
            } else {
                throw new NotYourAccountException("Forbidden account, please double check your inputted information");
            }
        } throw new IdNotFoundException("Please enter a valid Id");
    }

    public List<User> getUsers(){
       return userDao.findAll();
    }

    public List<Transfer> getAllTransfersByUserId (int userId) {
        return transferDao.getAllTransfersByUserId(userId);
    }

    public Transfer viewTransferDetails(int id) {
        return transferDao.viewTransferDetails(id);
    }
}
