package com.momodev.models;

import com.momodev.enums.PaymentState;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment extends ActiveRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;
    private BigDecimal amount;
    private LocalDateTime time;
    private PaymentState state;
    private String billId;

    public Payment() {
    }

    public Payment(BigDecimal amount, LocalDateTime time, PaymentState state, String billId) {
        this.id = UUID.randomUUID().toString().substring(0, 11);
        this.amount = amount;
        this.time = time;
        this.state = state;
        this.billId = billId;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setDate(LocalDateTime time) {
        this.time = time;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}