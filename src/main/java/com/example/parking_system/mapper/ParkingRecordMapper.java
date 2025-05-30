package com.example.parking_system.mapper;

import com.example.parking_system.dto.EntryRequest;
import com.example.parking_system.entity.ParkingRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingRecordMapper {

    // EntryRequest → ParkingRecord
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entryTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "exitTime", ignore = true)
    ParkingRecord fromEntryRequest(EntryRequest request);

}

