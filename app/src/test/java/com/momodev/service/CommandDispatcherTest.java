package com.momodev.service;

import com.momodev.constants.AppMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommandDispatcherTest {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    final PrintStream originalOut = System.out;

    CommandDispatcher dispatcher = new CommandDispatcher();

    AccountService accountService = Mockito.mock(AccountService.class);

    @BeforeEach
    void setUp() {
        dispatcher.setAccountService(accountService);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    void testExitApp() {
        String[] command = {"exit"};
        dispatcher.dispatch(command);
        assertEquals(AppMessage.EXIT + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testNotSupportedCommand() {
        String[] command = {"not", "supported", "command"};
        dispatcher.dispatch(command);
        assertEquals(AppMessage.NOT_SUPPORTED + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testDeposit_ValidNumber() {
        String amount = "100000";
        String[] command = {"deposit", amount};
        doNothing().when(accountService).deposit(new BigDecimal(amount));

        dispatcher.dispatch(command);
        verify(accountService, times(1)).deposit(new BigDecimal(amount));
    }

    @Test
    void testDeposit_InValidNumber() {
        String[] command = {"deposit", "NAN"};
        dispatcher.dispatch(command);
        assertEquals(AppMessage.ENTER_VALID_NUMBER + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testWithDraw_ValidNumber() {
        String amount = "100000";
        String[] command = {"withdraw", amount};
        doNothing().when(accountService).withdraw(new BigDecimal(amount));

        dispatcher.dispatch(command);
        verify(accountService, times(1)).withdraw(new BigDecimal(amount));
    }

    @Test
    void testWithDraw_InValidNumber() {
        String[] command = {"withdraw", "NAN"};
        dispatcher.dispatch(command);
        assertEquals(AppMessage.ENTER_VALID_NUMBER + System.lineSeparator(), outContent.toString());
    }

    @Test
    void testListBill() {
        String[] command = {"list_bill"};
        when(accountService.listBill()).thenReturn(new ArrayList<>());
        dispatcher.dispatch(command);
        verify(accountService, times(1)).listBill();
    }

    @Test
    void testPay() {
        List<String> bills = List.of("1", "2");

        String[] command = {"pay", "1", "2"};
        doNothing().when(accountService).pay(bills);
        dispatcher.dispatch(command);
        verify(accountService, times(1)).pay(bills);
    }

    @Test
    void testListPayment() {
        String[] command = {"list_payment"};
        when(accountService.listPayment()).thenReturn(new ArrayList<>());
        dispatcher.dispatch(command);
        verify(accountService, times(1)).listPayment();
    }

    @Test
    void testSchedule() {
        LocalDateTime scheduleDate = LocalDateTime.of(2023, 12, 31, 18, 0);
        String[] command = {"schedule", "1","2023-12-31", "18:00"};
        doNothing().when(accountService).schedule("1", scheduleDate);
        dispatcher.dispatch(command);
        verify(accountService, times(1)).schedule("1", scheduleDate);
    }

    @Test
    void testDueDate() {
        String[] command = {"due_date"};
        when(accountService.dueDate()).thenReturn(new ArrayList<>());
        dispatcher.dispatch(command);
        verify(accountService, times(1)).dueDate();
    }

    @Test
    void testSearchBillBy() {
        String criteria = "PROVIDER";
        String value = "FPT";
        String[] command = {"search_bill_by", criteria, value};
        when(accountService.searchBillBy(criteria, value)).thenReturn(new ArrayList<>());
        dispatcher.dispatch(command);
        verify(accountService, times(1)).searchBillBy(criteria, value);
    }

}
