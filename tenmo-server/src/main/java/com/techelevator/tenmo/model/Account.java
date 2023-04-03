package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    int accountId;
    BigDecimal amount;
    int userId;

    public Account(int accountId, BigDecimal amount, int userId) {
        this.accountId = accountId;
        this.amount = amount;
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
