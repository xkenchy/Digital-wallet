package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance (int id);
    BigDecimal subtractBalance(BigDecimal amountToChange, int id);
    BigDecimal addBalance(BigDecimal amountToChange, int id);
    int getAccountIdByUserId(int userId);
}
