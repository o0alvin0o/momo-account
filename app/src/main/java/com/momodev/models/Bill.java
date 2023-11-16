package com.momodev.models;

import com.momodev.enums.BillState;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill extends ActiveRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    private String billType;
    private BigDecimal amount;
    private LocalDate date;
    private BillState state;
    private String provider;

    public Bill() {
    }

    public Bill(String billType, BigDecimal amount, LocalDate date, BillState state, String provider) {
        this.billType = billType;
        this.amount = amount;
        this.date = date;
        this.state = state;
        this.provider = provider;
    }
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BillState getState() {
        return state;
    }

    public void setState(BillState state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}