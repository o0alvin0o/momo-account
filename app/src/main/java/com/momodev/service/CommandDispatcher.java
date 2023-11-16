package com.momodev.service;

import com.momodev.constants.AppMessage;
import com.momodev.constants.Command;
import com.momodev.models.ActiveRecord;

import java.util.Arrays;
import java.util.List;

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
    }

    private void searchBillBy(String[] values) {
    }

    private void dueDate() {
    }

    private void schedule(String[] values) {
    }

    private void listPayment() {
    }

    private void listBill() {
    }

    private void withdraw(String value) {
    }

    private void deposit(String value) {
    }

    private void pay(String[] values) {
    }

    private void generateTestData() {
    }

    private <T extends ActiveRecord> void printResult(List<T> records, Class clazz) {
    }
    private void exitApp() {
        System.out.print(AppMessage.EXIT);
    }

    private void notSupportedCommand() {
        System.out.print(AppMessage.NOT_SUPPORTED);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void init() {
    }
}
