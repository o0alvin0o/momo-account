package com.momodev.service;

import com.momodev.FakeDataBuilder;
import com.momodev.constants.AppConfig;
import com.momodev.models.Account;
import com.momodev.models.Bill;
import com.momodev.service.impl.SimpleAccountService;
import org.apache.groovy.json.internal.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SimpleAccountServiceTest {

    SimpleAccountService simpleAccountService = new SimpleAccountService();

    DataOperator dataOperator = Mockito.mock(DataOperator.class);

    @BeforeEach
    void setUp() {
        simpleAccountService.setDataOperator(dataOperator);
    }
    @Test
    void testPay_Successful() throws IOException, ClassNotFoundException {
        Bill waterBill = FakeDataBuilder.getWaterBill();
        Bill electricBill = FakeDataBuilder.getElectricBill();
        Account account = Account.init();

        when(dataOperator.getById(waterBill.getId(), Bill.class)).thenReturn(Optional.of(waterBill));
        when(dataOperator.getById(electricBill.getId(), Bill.class)).thenReturn(Optional.of(electricBill));
        when(dataOperator.getById(AppConfig.DEFAULT_ACCOUNT_ID, Account.class)).thenReturn(Optional.of(account));

        doNothing().when(dataOperator).backUp(waterBill, Bill.class);
        doNothing().when(dataOperator).backUp(electricBill, Bill.class);

        doNothing().when(dataOperator).persist(waterBill, Bill.class);
        doNothing().when(dataOperator).persist(electricBill, Bill.class);
        doNothing().when(dataOperator).persist(account, Account.class);

        doNothing().when(dataOperator).deleteBackup(waterBill, Bill.class);
        doNothing().when(dataOperator).deleteBackup(electricBill, Bill.class);

        assertDoesNotThrow(() -> simpleAccountService.pay(List.of(waterBill.getId(), electricBill.getId(), "not found bill")));
        verify(dataOperator, times(1)).getById("not found bill", Bill.class);
        verify(dataOperator, times(1)).getById(waterBill.getId(), Bill.class);

    }

    @Test
    void testPay_ThrowException() throws IOException, ClassNotFoundException {
        when(dataOperator.getById("any id", Bill.class)).thenThrow(IOException.class);
        assertDoesNotThrow(() -> simpleAccountService.pay(List.of("any id")));
        verify(dataOperator, times(1)).getById("any id", Bill.class);

    }
}
