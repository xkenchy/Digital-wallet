package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.IdNotFoundException;
import com.techelevator.tenmo.exception.NotEnoughMoneyException;
import com.techelevator.tenmo.exception.NotYourAccountException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TenmoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    TenmoService tenmoService;

    public TenmoController(TenmoService tenmoService) {
        this.tenmoService = tenmoService;
    }
    @GetMapping("users/{id}/account")
    public BigDecimal getBalance(@PathVariable int id, Principal principal) throws NotYourAccountException, IdNotFoundException {
       return tenmoService.getBalance(id, principal.getName());
    }
    @PostMapping("transfer/")
    public BigDecimal sendBucks(@RequestBody Transfer transfer ,Principal principal) throws NotYourAccountException, NotEnoughMoneyException, IdNotFoundException {
            return tenmoService.sendBucks(transfer, principal.getName());
    }
    @GetMapping("users")
    public List<User> getUsers (){
        return  tenmoService.getUsers();
    }

    @GetMapping("users/{id}/transfer")
    public List<Transfer> getAllTransfersByUserId(@PathVariable int id) {
        return tenmoService.getAllTransfersByUserId(id);
    }
    @GetMapping("transfer/{id}")
    public Transfer viewTransferDetails(@PathVariable int id){
        return  tenmoService.viewTransferDetails(id);
    }
}
