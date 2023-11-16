package com.momodev.constants;

import java.lang.reflect.Field;

public class Command {
    public static final String EXIT = "exit";
    public static final String DEPOSIT = "deposit";
    public static final String BALANCE = "balance";
    public static final String WITHDRAW = "withdraw";
    public static final String LIST_BILL = "list_bill";
    public static final String PAY = "pay";
    public static final String DUE_DATE = "due_date";
    public static final String SCHEDULE = "schedule";
    public static final String LIST_PAYMENT = "list_payment";
    public static final String SEARCH_BILL_BY = "search_bill_by";
    public static final String GEN_TEST_BILL = "gen_bill";
    public static final String INIT = "init";

    public void printCommandList() throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.get(this));
        }
    }
}
