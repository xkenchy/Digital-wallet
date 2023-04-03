package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{
    Account Account1 = new Account(2005, BigDecimal.valueOf(200.00).setScale(2), 1001);

    JdbcAccountDao accountDao;
    Account testAccount;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        accountDao = new JdbcAccountDao(jdbcTemplate);

        testAccount = new Account(2000, BigDecimal.valueOf(100), 1000);
    }

    @Test
    public void getBalance_returnsExpectedValue() {
        BigDecimal retrievedBalance = accountDao.getBalance(Account1.getUserId());
        BigDecimal expectedBalance = Account1.getAmount();

        Assert.assertEquals(expectedBalance, retrievedBalance);

    }
}
