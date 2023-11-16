package com.momodev.service;

import com.momodev.models.Bill;
import com.momodev.models.Payment;
import com.momodev.service.impl.SimpleAccountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {

    static AccountService getDefault() throws IOException {
        return new SimpleAccountService(DataOperator.getDefault());
    }

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

    List<Payment> listPayment();

    List<Bill> listBill();

    void pay(List<String> billIds);

    void schedule(String billId, LocalDateTime time);

    List<Bill> dueDate();

    List<Bill> searchBillBy(String criteria, String value);

    BigDecimal getBalance(String accountId);

    void init();
}
