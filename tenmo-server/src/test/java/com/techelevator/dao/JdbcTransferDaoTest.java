package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JdbcTransferDaoTest extends BaseDaoTests {


    Transfer transfer = new Transfer(2005, 2006, BigDecimal.valueOf(23).setScale(2));

    JdbcTransferDao transferDao;
    Transfer testTransfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        transferDao = new JdbcTransferDao(jdbcTemplate);

        testTransfer = new Transfer(2, 2, BigDecimal.valueOf(67).setScale(2));

    }

    @Test
    public void create_transfer_creates_expected_value() {

        Transfer retrievedTransfer = transferDao.createTransfer(transfer, transfer.getAccountFrom(), transfer.getAccountTo());
        Transfer expectedTransfer = transfer;
        expectedTransfer.setId(retrievedTransfer.getId());
        Assert.assertEquals(expectedTransfer, retrievedTransfer);
    }
}


