package com.example.parking_system.controller;

import com.example.parking_system.dto.EntryRequest;
import com.example.parking_system.dto.ExitRequest;
import com.example.parking_system.dto.ReportResponse;
import com.example.parking_system.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/parking")
@Tag(name = "Парковка", description = "Операции с парковкой")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    @Operation(
            summary = "Регистрация въезда",
            description = "Регистрирует автомобиль при въезде и возвращает время въезда"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Время въезда успешно зарегистрировано",
            content = @Content(schema = @Schema(implementation = LocalDateTime.class))
    )
    public ResponseEntity<LocalDateTime> registerEntry(
            @RequestBody(
                    description = "Информация о въезде",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EntryRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody EntryRequest request) {
        LocalDateTime entryTime = parkingService.registerEntry(request);
        return ResponseEntity.ok(entryTime);
    }

    @PostMapping("/exit")
    @Operation(
            summary = "Регистрация выезда",
            description = "Регистрирует выезд автомобиля и возвращает время выезда"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Время выезда успешно зарегистрировано",
            content = @Content(schema = @Schema(implementation = LocalDateTime.class))
    )
    public ResponseEntity<LocalDateTime> registerExit(
            @RequestBody(
                    description = "Информация о выезде",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ExitRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ExitRequest request) {
        LocalDateTime exitTime = parkingService.registerExit(request);
        return ResponseEntity.ok(exitTime);
    }

    @GetMapping("/report")
    @Operation(
            summary = "Получить отчет",
            description = "Возвращает количество занятых и свободных мест и среднюю длительность парковки"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешно получен отчет",
            content = @Content(schema = @Schema(implementation = ReportResponse.class))
    )
    public ResponseEntity<ReportResponse> getReport(
            @Parameter(description = "Дата и время начала периода", example = "2024-05-01T00:00:00")
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Дата и время конца периода", example = "2024-05-30T23:59:59")
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(parkingService.getReport(startDate, endDate));
    }
}
