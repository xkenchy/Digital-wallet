package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int accountFrom;
    private int accountTo;
    private int userIdFrom;
    private int userIdTo;
    private String toName;
    private String fromName;
    private BigDecimal amount ;
    private int transferType = 2;
    private int transferStatus = 2;
    private String nameOfTransferType = "Send";
    private String nameOfTransferStatus = "Approved";



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToName() {
        return toName;
    }

    public String getFromName() {
        return fromName;
    }

    public int getTransferType() {
        return transferType;
    }

    public String getNameOfTransferType() {
        return nameOfTransferType;
    }

    public String getNameOfTransferStatus() {
        return nameOfTransferStatus;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setNameOfTransferType(String nameOfTransferType) {
        this.nameOfTransferType = nameOfTransferType;
    }

    public void setNameOfTransferStatus(String nameOfTransferStatus) {
        this.nameOfTransferStatus = nameOfTransferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Transfer() {

    };

    public Transfer (int accountFrom, int accountTo, BigDecimal amount){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    };

    public int getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(int userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public int getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(int userIdTo) {
        this.userIdTo = userIdTo;
    }
}
