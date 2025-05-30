package com.example.parking_system.service;

import com.example.parking_system.dto.EntryRequest;
import com.example.parking_system.dto.ExitRequest;
import com.example.parking_system.dto.ReportResponse;
import com.example.parking_system.entity.ParkingRecord;
import com.example.parking_system.mapper.ParkingRecordMapper;
import com.example.parking_system.repository.ParkingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingServiceTest {

    private ParkingRecordRepository repository;
    private ParkingRecordMapper mapper;
    private ParkingService parkingService;

    @BeforeEach
    void setup() {
        repository = mock(ParkingRecordRepository.class);
        mapper = mock(ParkingRecordMapper.class);
        parkingService = new ParkingService(repository, mapper);
    }

    @Test
    void registerEntry_savesRecordAndReturnsTime() {
        EntryRequest request = new EntryRequest("A123BC", "car");
        ParkingRecord mappedRecord = new ParkingRecord();
        LocalDateTime now = LocalDateTime.now();
        mappedRecord.setEntryTime(now);

        when(mapper.fromEntryRequest(request)).thenReturn(mappedRecord);

        LocalDateTime result = parkingService.registerEntry(request);

        verify(repository).save(mappedRecord);
        assertEquals(now, result);
    }

    @Test
    void registerExit_updatesExitTimeAndReturnsIt() {
        String carNumber = "A123BC";
        ExitRequest request = new ExitRequest(carNumber);
        ParkingRecord record = new ParkingRecord();
        record.setCarNumber(carNumber);
        record.setEntryTime(LocalDateTime.now().minusHours(1));

        when(repository.findByCarNumberAndExitTimeIsNull(carNumber)).thenReturn(Optional.of(record));

        LocalDateTime result = parkingService.registerExit(request);

        assertNotNull(record.getExitTime());
        assertEquals(record.getExitTime(), result);
        verify(repository).save(record);
    }

    @Test
    void getReport_returnsCorrectValues() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        ParkingRecord record1 = new ParkingRecord();
        record1.setEntryTime(start.plusHours(1));
        record1.setExitTime(start.plusHours(2));

        ParkingRecord record2 = new ParkingRecord();
        record2.setEntryTime(start.plusHours(3));
        record2.setExitTime(start.plusHours(5));

        when(repository.findAllWithinPeriod(start, end)).thenReturn(List.of(record1, record2));
        when(repository.countOccupied()).thenReturn(30L);

        ReportResponse response = parkingService.getReport(start, end);

        assertEquals(30L, response.occupiedSpots());
        assertEquals(70L, response.freeSpots());
        assertEquals(90.0, response.averageDurationMinutes());
    }

    @Test
    void registerExit_carNotFound_throwsException() {
        ExitRequest request = new ExitRequest("UNKNOWN");
        when(repository.findByCarNumberAndExitTimeIsNull("UNKNOWN")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> parkingService.registerExit(request));

        assertEquals("Car not found on parking", ex.getMessage());
    }

}
