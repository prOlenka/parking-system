package com.example.parking_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на въезд")
public record EntryRequest(
        @Schema(description = "Номер автомобиля", example = "A123BC77")
        String carNumber,

        @Schema(description = "Тип автомобиля", example = "Sedan")
        String carType
) {}
