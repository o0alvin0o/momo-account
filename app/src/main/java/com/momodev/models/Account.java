package com.momodev.models;

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

}
