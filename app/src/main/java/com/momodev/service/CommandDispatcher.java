package com.momodev.service;

import com.momodev.constants.AppConfig;
import com.momodev.constants.AppMessage;
import com.momodev.constants.Command;
import com.momodev.enums.BillState;
import com.momodev.models.Account;
import com.momodev.models.ActiveRecord;
import com.momodev.models.Bill;
import com.momodev.models.Payment;
import com.momodev.utils.AppUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandDispatcher {

    private AccountService accountService;

    public void dispatch(String... args) {
        String[] values = Arrays.copyOfRange(args, 1, args.length);
        switch (args[0].toLowerCase()) {
            case Command.EXIT -> exitApp();
            case Command.PAY -> pay(values);
            case Command.DEPOSIT -> deposit(args[1]);
            case Command.WITHDRAW -> withdraw(args[1]);
            case Command.LIST_BILL -> listBill();
            case Command.LIST_PAYMENT -> listPayment();
            case Command.SCHEDULE -> schedule(values);
            case Command.DUE_DATE -> dueDate();
            case Command.SEARCH_BILL_BY -> searchBillBy(values);
            case Command.GEN_TEST_BILL -> generateTestData();
            case Command.BALANCE -> getBalance();
            case Command.INIT -> init();

            default -> notSupportedCommand();
        }
    }

    private void getBalance() {
        try {
            System.out.println("You have: " + accountService.getBalance(AppConfig.DEFAULT_ACCOUNT_ID));
        } catch (Exception exception) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }
    }

    private void searchBillBy(String[] values) {
        if (values.length < 2) {
            System.out.println(AppMessage.INVALID_SEARCH_BILL_ARGS);
            return;
        }
        List<Bill> bills = accountService.searchBillBy(values[0], values[1]);

        printResult(bills, Bill.class);
    }

    private void dueDate() {
        List<Bill> bills = accountService.dueDate();
        printResult(bills, Bill.class);
    }

    private void schedule(String[] values) {
        int hour;
        int minute;
        try {
            String billId = values[0];
            LocalDate scheduleDate = LocalDate.parse(values[1], DateTimeFormatter.ISO_LOCAL_DATE);
            if (values[2].length() != 5 || values[2].charAt(2) != ':') {
                System.out.println(AppMessage.ENTER_VALID_TIME);
                return;
            }
            hour = Integer.parseInt(values[2].substring(0, 2));
            minute = Integer.parseInt(values[2].substring(3, 5));
            LocalDateTime scheduleTime = scheduleDate.atTime(hour, minute);
            accountService.schedule(billId, scheduleTime);
        } catch (NumberFormatException ex) {
            System.out.println(AppMessage.ENTER_VALID_TIME);
        } catch (DateTimeParseException exception) {
            System.out.println(AppMessage.ENTER_VALID_DATE);
        }
    }

    private void listPayment() {
        List<Payment> payments = accountService.listPayment();
        printResult(payments, Payment.class);
    }

    private void listBill() {
        List<Bill> bills = accountService.listBill();
        printResult(bills, Bill.class);
    }

    private void withdraw(String value) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(value);
            accountService.withdraw(amount);
        } catch (NumberFormatException ex) {
            System.out.println(AppMessage.ENTER_VALID_NUMBER);
        }
    }

    private void deposit(String value) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(value);
            accountService.deposit(amount);
        } catch (NumberFormatException ex) {
            System.out.println(AppMessage.ENTER_VALID_NUMBER);
        }
    }

    private void pay(String[] values) {
        accountService.pay(Arrays.asList(values));
    }

    private void generateTestData() {
        try {
            Account account = Account.init();

            Bill electricBill = new Bill("Electric", new BigDecimal(500000),
                    LocalDate.of(2023,12,15), BillState.NOT_PAID, "EVN HCM");
            Bill waterBill = new Bill("Water", new BigDecimal(100000),
                    LocalDate.of(2023,12,10), BillState.NOT_PAID, "TRUNG AN");
            Bill fptBill = new Bill("Internet", new BigDecimal(200000),
                    LocalDate.of(2023,11,29), BillState.NOT_PAID, "FPT");
            Bill viettelBill = new Bill("Internet", new BigDecimal(200000),
                    LocalDate.of(2023,11,28), BillState.NOT_PAID, "VIETTEL");

            Bill dueBill1 = new Bill("Test due 1", new BigDecimal(80000),
                    LocalDate.of(2023,10,28), BillState.NOT_PAID, "DUE1");
            Bill dueBill2 = new Bill("Test due 2", new BigDecimal(20000),
                    LocalDate.of(2023,9,15), BillState.NOT_PAID, "DUE2");

            DataOperator dataOperator = DataOperator.getDefault();
            try {
                dataOperator.persist(account, Account.class);
                dataOperator.persist(List.of(electricBill, waterBill, fptBill, viettelBill, dueBill1, dueBill2), Bill.class);
            } catch (Exception ex) {
                System.out.println(AppMessage.ERR_READ_DATA);
            }

        } catch (Exception exception) {
            System.out.println(AppMessage.ERR_READ_DATA);
        }

    }

    private <T extends ActiveRecord> void printResult(List<T> records, Class clazz) {
        AppUtils.printHeaders(clazz);
        if (records.isEmpty()) {
            return;
        }
        AppUtils.printRecords(records, clazz);
    }
    private void exitApp() {
        System.out.println(AppMessage.EXIT);
    }

    private void notSupportedCommand() {
        System.out.println(AppMessage.NOT_SUPPORTED);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
        accountService.init();
    }
}