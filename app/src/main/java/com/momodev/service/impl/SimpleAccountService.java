package com.momodev.service.impl;

import com.momodev.constants.AppConfig;
import com.momodev.constants.AppMessage;
import com.momodev.enums.BillState;
import com.momodev.enums.PaymentState;
import com.momodev.models.Account;
import com.momodev.models.Bill;
import com.momodev.models.Payment;
import com.momodev.service.AccountService;
import com.momodev.service.DataOperator;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SimpleAccountService implements AccountService {

    private DataOperator dataOperator;

    public SimpleAccountService() {}

    public SimpleAccountService(DataOperator dataOperator) {
        this.dataOperator = dataOperator;
    }

    @Override
    public void deposit(BigDecimal amount) {
        try {
            Account account = dataOperator.getById(AppConfig.DEFAULT_ACCOUNT_ID, Account.class).orElseThrow();
            account.setBalance(account.getBalance().add(amount));
            dataOperator.persist(account, Account.class);
        } catch (Exception e) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        try {
            Account account = dataOperator.getById(AppConfig.DEFAULT_ACCOUNT_ID, Account.class).orElseThrow();
            if (amount.compareTo(account.getBalance()) < 0) {
                account.setBalance(account.getBalance().subtract(amount));
                dataOperator.persist(account, Account.class);
            } else
                System.out.println(AppMessage.NOT_ENOUGH_MONEY + account.getBalance());
        } catch (Exception e) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }
    }

    @Override
    public List<Payment> listPayment() {
        try {
            return dataOperator.load(Payment.class);
        } catch (Exception e) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Bill> listBill() {
        try {
            return dataOperator.load(Bill.class);
        } catch (Exception e) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return new ArrayList<>();
        }
    }

    @Override
    public void pay(List<String> billIds) {
        // Find bill first
        List<Bill> bills = new ArrayList<>();
        Account account = null;
        try {
            for (String id : billIds) {
                Optional<Bill> bill = dataOperator.getById(id, Bill.class);
                bill.ifPresent(bills::add);
            }
        } catch (Exception ex) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return;
        }

        if (bills.isEmpty()) {
            System.out.println(AppMessage.NO_BILL_TO_PAY);
            return;
        }
        // Now validate the transaction
        try {
            BigDecimal totalBillAmount = new BigDecimal(0);
            account = dataOperator.getById(AppConfig.DEFAULT_ACCOUNT_ID, Account.class).orElseThrow();
            for (Bill bill : bills) {
                totalBillAmount = totalBillAmount.add(bill.getAmount());
            }
            if (totalBillAmount.compareTo(account.getBalance()) > 0) {
                System.out.println(AppMessage.NOT_ENOUGH_MONEY + account.getBalance() + ". Total bill amount is: " + totalBillAmount);
                return;
            }
        } catch (Exception ex) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }
        // Now pay the bill in transaction
        for (Bill bill : bills) {
            payBill(bill, account);
            System.out.println(AppMessage.PAY_BILL_SUCCESSFUL + bill.getId());
        }
    }

    private void payBill(Bill bill, Account account) {
        // Back up data first
        try {
            dataOperator.backUp(bill, Bill.class);
            dataOperator.backUp(account, Account.class);
        } catch (Exception ex) {
            System.out.println(AppMessage.CANNOT_BACKUP);
            return;
        }
        // Now persist data
        Payment payment = new Payment(bill.getAmount(), LocalDateTime.now(), PaymentState.PROCESSES, bill.getId());;
        bill.setState(BillState.PAID);
        account.setBalance(account.getBalance().subtract(bill.getAmount()));
        try {
            dataOperator.persist(bill, Bill.class);
            dataOperator.persist(account, Account.class);
            dataOperator.persist(payment, Payment.class);
        } catch (Exception ex) {
            System.out.println(AppMessage.ERR_READ_DATA);
            System.out.println("Trying to rollback");
            try {
                dataOperator.restore(bill, Bill.class);
                dataOperator.restore(account, Account.class);
                dataOperator.deleteIfExist(payment, Payment.class);
            } catch (Exception exception) {
                System.out.println("Really serious problem");
                return;
            }
        }
        // Remove backup
        try {
            dataOperator.deleteBackup(bill, Bill.class);
            dataOperator.deleteBackup(account, Account.class);
        } catch (Exception exception) {
            System.out.println(AppMessage.BACKUP_DIRTY);
        }
    }

    @Override
    public void schedule(String billId, LocalDateTime time) {
        // TODO We can you cron job to do this in UNIX system
        System.out.println("bill no " + billId + " will be paid at " + time.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Override
    public List<Bill> dueDate() {
        try {
            return dataOperator.load(Bill.class)
                    .stream()
                    .filter(bill -> bill.getState() == BillState.NOT_PAID
                            && bill.getDate().isBefore(LocalDate.now()))
                    .toList();
        } catch (Exception e) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Bill> searchBillBy(String criteria, String value) {
        switch (criteria.toLowerCase()) {
            case "provider" -> {
                return searchBillByProvider(value);
            }
            case "state" -> {
                return searchBillByState(value);
            }
            default -> {
                System.out.println(AppMessage.INVALID_SEARCH_BILL_ARGS);
                return new ArrayList<>();
            }
        }
    }

    @Override
    public BigDecimal getBalance(String accountId) {
        try {
            Account account = dataOperator.getById(accountId, Account.class).orElseThrow();
            return account.getBalance();
        } catch (Exception exception) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return null;
        }
    }

    @Override
    public void init() {
        try {
            Account account = Account.init();
            dataOperator.persist(account, Account.class);
        } catch (Exception exception) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }
    }

    private List<Bill> searchBillByState(String value) {
        System.out.println(AppMessage.NOT_SUPPORTED);
        return new ArrayList<>();
    }

    private List<Bill> searchBillByProvider(String provider) {
        try {
            return dataOperator.load(Bill.class).stream()
                    .filter(bill -> Objects.equals(bill.getProvider(), provider))
                    .toList();
        } catch (Exception ex) {
            System.out.println(AppMessage.ERR_READ_DATA);
            return new ArrayList<>();
        }
    }

    public static SimpleAccountService getDefault() throws IOException {
        return new SimpleAccountService(DataOperator.getDefault());
    }

    public DataOperator getDataOperator() {
        return dataOperator;
    }

    public void setDataOperator(DataOperator dataOperator) {
        this.dataOperator = dataOperator;
    }
}
