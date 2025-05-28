package com.example.parking_system.controller;

import com.example.parking_system.dto.EntryRequest;
import com.example.parking_system.dto.ExitRequest;
import com.example.parking_system.dto.ReportResponse;
import com.example.parking_system.service.ParkingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ResponseEntity<LocalDateTime> registerEntry(@RequestBody EntryRequest request) {
        LocalDateTime entryTime = parkingService.registerEntry(request);
        return ResponseEntity.ok(entryTime);
    }

    @PostMapping("/exit")
    public ResponseEntity<LocalDateTime> registerExit(@RequestBody ExitRequest request) {
        LocalDateTime exitTime = parkingService.registerExit(request);
        return ResponseEntity.ok(exitTime);
    }

    @GetMapping("/report")
    public ResponseEntity<ReportResponse> getReport(
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(parkingService.getReport(startDate, endDate));
    }
}
