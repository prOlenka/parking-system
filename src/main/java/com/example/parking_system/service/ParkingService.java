package com.example.parking_system.service;

import com.example.parking_system.dto.EntryRequest;
import com.example.parking_system.dto.ExitRequest;
import com.example.parking_system.dto.ReportResponse;
import com.example.parking_system.entity.ParkingRecord;
import com.example.parking_system.repository.ParkingRecordRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    private final ParkingRecordRepository repository;
    private final int TOTAL_SPOTS = 100;

    public ParkingService(ParkingRecordRepository repository) {
        this.repository = repository;
    }

    public LocalDateTime registerEntry(EntryRequest request) {
        ParkingRecord record = new ParkingRecord();
        record.setCarNumber(request.carNumber());
        record.setCarType(request.carType());
        record.setEntryTime(LocalDateTime.now());
        repository.save(record);
        return record.getEntryTime();
    }

    public LocalDateTime registerExit(ExitRequest request) {
        ParkingRecord record = repository.findByCarNumberAndExitTimeIsNull(request.carNumber())
                .orElseThrow(() -> new RuntimeException("Car not found on parking"));
        record.setExitTime(LocalDateTime.now());
        repository.save(record);
        return record.getExitTime();
    }

    public ReportResponse getReport(LocalDateTime start, LocalDateTime end) {
        List<ParkingRecord> records = repository.findAllWithinPeriod(start, end);
        long occupied = repository.countOccupied();
        long free = TOTAL_SPOTS - occupied;
        double avgMinutes = records.stream()
                .filter(r -> r.getExitTime() != null)
                .mapToLong(r -> Duration.between(r.getEntryTime(), r.getExitTime()).toMinutes())
                .average()
                .orElse(0.0);
        return new ReportResponse(occupied, free, avgMinutes);
    }
}
