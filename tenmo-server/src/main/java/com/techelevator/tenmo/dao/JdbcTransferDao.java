package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.exception.IdNotFoundException;
import com.techelevator.tenmo.exception.TransferInvalidDataException;
import org.postgresql.util.PSQLException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao  implements TransferDao{

    private final JdbcTemplate jdbcTemplate;



    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer createTransfer(Transfer transfer, int fromId, int toId)  throws TransferInvalidDataException{

        String sql ="INSERT INTO transfer VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING transfer_id;";
        try {
            Integer transferID = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferType(),
                    transfer.getTransferStatus(), fromId, toId, transfer.getAmount());
            if (transferID != null) {
                transfer.setId(transferID);
        }
        return transfer;
       }catch (Exception exception){
            throw new TransferInvalidDataException("Please enter valid data (id must not be your own, amount > 0");
       }

    }


    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {
        List<Transfer> result = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_to " +
                "WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            Transfer curTransfer = new Transfer();
            curTransfer.setId(rowSet.getInt("transfer_id"));
            curTransfer.setAccountTo(rowSet.getInt("account_to"));
            curTransfer.setAmount(rowSet.getBigDecimal("amount"));
            result.add(curTransfer);
        }
        return result;
    }


    @Override
    public Transfer viewTransferDetails(int id) throws IdNotFoundException{
        try {
            String sqlFromAccount = "SELECT * FROM transfer " +
                    "JOIN account ON account.account_id = transfer.account_from " +
                    "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                    "WHERE transfer_id = ? ";

            String sql2 = "SELECT * FROM transfer JOIN account ON account.account_id = transfer.account_to " +
                    "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                    "WHERE transfer_id = ? ";
            SqlRowSet rowSetFromAccount = jdbcTemplate.queryForRowSet(sqlFromAccount, id);
            SqlRowSet rowSetToAccount = jdbcTemplate.queryForRowSet(sql2, id);
            Transfer result = new Transfer();
            if (rowSetFromAccount.next()) {
                result.setId(rowSetFromAccount.getInt("transfer_id"));
                result.setAmount(rowSetFromAccount.getBigDecimal("amount"));
                result.setAccountFrom(rowSetFromAccount.getInt("account_from"));
                result.setFromName(rowSetFromAccount.getString("username"));

            }
            if (rowSetToAccount.next()) {
                result.setToName(rowSetToAccount.getString("username"));
                result.setAccountTo(rowSetToAccount.getInt("account_to"));
            }
            return result;
        } catch (Exception ex) {
            throw new IdNotFoundException("Please enter correct transfer id");
        }
    }
}
