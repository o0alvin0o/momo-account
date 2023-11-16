package com.momodev.models;

import com.momodev.FakeDataBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillTest extends BaseModelTest {
    @Test
    void testDataOperation_Successful() {

        Bill electricBill = FakeDataBuilder.getElectricBill();
        electricBill.setId("1");
        Bill waterBill = FakeDataBuilder.getWaterBill();
        waterBill.setId("2");
        List<Bill> expected = List.of(waterBill, electricBill);

        dataOperator.persist(expected, Bill.class);

        List<Bill> allBills = dataOperator.load(Bill.class);
        Bill loadedElectricBill = dataOperator.getById("1", Bill.class).orElse(new Bill());
        Bill loadedWaterBill = dataOperator.getById("2", Bill.class).orElse(new Bill());

        assertEquals(expected.size(), allBills.size());
        assertEquals(electricBill.getBillType(), loadedElectricBill.getBillType());
        assertEquals(waterBill.getDate(), loadedWaterBill.getDate());
    }
}
