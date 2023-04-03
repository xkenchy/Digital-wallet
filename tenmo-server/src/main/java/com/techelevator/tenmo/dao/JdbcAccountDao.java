package com.techelevator.tenmo.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //postman exception
    @Override
    public BigDecimal getBalance (int id) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
    }
    //make shorter, null pointer exception (cli)
    public int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account JOIN tenmo_user ON tenmo_user.user_id = " +
                "account.user_id WHERE tenmo_user.user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }
    //make shorter, null pointer exception (cli)
    public int getUserIdByAccountId(int accountId) {
        String sql = "SELECT user_id FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, accountId);
    }

    public  BigDecimal subtractBalance(BigDecimal amountToChange, int id) {
        String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ? RETURNING balance;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, amountToChange, id);
    }

    public  BigDecimal addBalance(BigDecimal amountToChange, int id) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ? RETURNING balance;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, amountToChange, id);
    }
}
