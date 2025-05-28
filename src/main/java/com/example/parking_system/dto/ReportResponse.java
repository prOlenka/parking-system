package com.example.parking_system.dto;

public record ReportResponse(
        long occupiedSpots,
        long freeSpots,
        double averageDurationMinutes
) {}

