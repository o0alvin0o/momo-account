package com.momodev.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest extends BaseModelTest {

    @Test
    void testSaveAndLoad_SuccessFul() {
        Account expected = new Account();
        expected.setBalance(new BigDecimal(10000));

        dataOperator.persist(expected, Account.class);

        Account result = dataOperator.load(Account.class).get(0);
        assertEquals(expected.getBalance(), result.getBalance());
    }
}
