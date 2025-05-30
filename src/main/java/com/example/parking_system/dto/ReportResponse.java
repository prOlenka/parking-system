package com.example.parking_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с отчетом о парковке")
public record ReportResponse(

        @Schema(description = "Количество занятых мест", example = "15")
        long occupiedSpots,

        @Schema(description = "Количество свободных мест", example = "85")
        long freeSpots,

        @Schema(description = "Средняя длительность парковки в минутах", example = "42.5")
        double averageDurationMinutes
) {}
