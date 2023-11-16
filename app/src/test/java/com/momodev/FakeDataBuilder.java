package com.momodev;

import com.momodev.enums.BillState;
import com.momodev.enums.PaymentState;
import com.momodev.models.Bill;
import com.momodev.models.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FakeDataBuilder {
    public static Bill getElectricBill() {
        Bill result = new Bill();
        result.setBillType("Electric");
        result.setDate(LocalDate.of(2023, 12, 20));
        result.setProvider("EVN HCM");
        result.setAmount(new BigDecimal(250000));
        result.setState(BillState.NOT_PAID);
        return result;
    }

    public static Bill getWaterBill() {
        Bill result = new Bill();
        result.setBillType("Water");
        result.setDate(LocalDate.of(2023, 12, 21));
        result.setProvider("TRUNG AN");
        result.setAmount(new BigDecimal(120000));
        result.setState(BillState.NOT_PAID);
        return result;
    }

    public static Payment getPaymentForAtWith(String forBillId, LocalDateTime at, PaymentState withState) {
        Payment result = new Payment();
        result.setDate(at);
        result.setBillId(forBillId);
        result.setAmount(new BigDecimal(120000));
        result.setState(withState);
        return result;
    }
}
