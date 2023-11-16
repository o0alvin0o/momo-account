package com.momodev.models;

import com.momodev.constants.AppConfig;

import java.io.Serial;
import java.math.BigDecimal;

public class Account extends ActiveRecord {

    @Serial
    private static final long serialVersionUID = 2L;

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public static Account init() {
        Account account = new Account();
        account.setId(AppConfig.DEFAULT_ACCOUNT_ID);
        account.setBalance(new BigDecimal(0));
        return account;
    }
}
