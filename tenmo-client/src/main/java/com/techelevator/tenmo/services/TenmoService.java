package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exceptions.IdNotFoundException;
import com.techelevator.tenmo.exceptions.NotEnoughMoneyException;
import com.techelevator.tenmo.exceptions.NotYourAccountException;
import com.techelevator.tenmo.exceptions.TransferInvalidDataException;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TenmoService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TenmoService(String url) {
        this.baseUrl = url;
    }

    public BigDecimal getBalance(AuthenticatedUser currentUser) {
       try {
           ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "users/" + currentUser.getUser().getId() +
                   "/account", HttpMethod.GET, makeGetEntity(currentUser), BigDecimal.class);
           return response.getBody();
       }
        catch (RestClientResponseException | ResourceAccessException | NotYourAccountException ex) {
            BasicLogger.log(ex.getMessage());
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public String sendBucks (AuthenticatedUser user, BigDecimal amount, int sendToUserId)  {
        try {
            //map transfer body
            Transfer transfer = new Transfer();
            transfer.setAmount(amount);
            transfer.setFromName(user.getUser().getUsername());
            transfer.setUserIdTo(sendToUserId);
            transfer.setUserIdFrom(user.getUser().getId());

            ResponseEntity<BigDecimal> response = restTemplate.postForEntity(baseUrl + "transfer/",
                    makeTransferEntity(user, transfer), BigDecimal.class);
            System.out.println("Your new balance is: " + response.getBody());
            return response.getBody().toString();
        } catch(RestClientResponseException | ResourceAccessException ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }
    }

    public String requestBucks (AuthenticatedUser user ,int requestUserId, BigDecimal amount){
      try {
          Transfer transfer = new Transfer();
          transfer.setAmount(amount);
          transfer.setUserIdTo(user.getUser().getId());
          transfer.setToName(user.getUser().getUsername());
          transfer.setUserIdFrom(requestUserId);
          transfer.setTransferStatus(1);
          transfer.setTransferType(1);
          transfer.setNameOfTransferStatus("Pending");
          transfer.setNameOfTransferType("Send");
          ResponseEntity<String > response = restTemplate.postForEntity(baseUrl + "transfer/request/"
                  + requestUserId, makeTransferEntity(user,transfer), String.class);
          return  response.getBody();

      }catch (ResourceAccessException | RestClientResponseException ex){
          System.out.println(ex.getMessage());
          return ex.getMessage();
      }



    }

    public Transfer viewTransferDetails(int id, AuthenticatedUser authenticatedUser){
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/" + id,
                    HttpMethod.GET, makeGetEntity(authenticatedUser), Transfer.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    public User[] getUsers (AuthenticatedUser authenticatedUser){
        try{
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl+ "users", HttpMethod.GET,
                    makeGetEntity(authenticatedUser), User[].class);
            return  response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Transfer[] viewTransferHistory (AuthenticatedUser curUser) {
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "users/" + curUser.getUser().getId()
            + "/transfer", HttpMethod.GET, makeGetEntity(curUser), Transfer[].class);
                  return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.log(ex.getMessage());
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private HttpEntity<Void> makeGetEntity(AuthenticatedUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser user, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());

        return new HttpEntity<>(transfer, headers);
    }





}
