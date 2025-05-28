package com.example.parking_system.batch;

import com.example.parking_system.entity.ParkingRecord;
import org.springframework.batch.item.ItemProcessor;

public class ParkingRecordItemProcessor implements ItemProcessor<ParkingRecord, ParkingRecord> {

    @Override
    public ParkingRecord process(ParkingRecord record) throws Exception {

        if (record.getCarNumber() == null || record.getCarNumber().isEmpty()) {
            return null;
        }
            record.setCarNumber(record.getCarNumber().toUpperCase());
            return record;
    }
}
