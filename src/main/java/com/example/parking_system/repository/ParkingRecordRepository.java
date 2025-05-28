package com.example.parking_system.repository;

import com.example.parking_system.entity.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    Optional<ParkingRecord> findByCarNumberAndExitTimeIsNull(String carNumber);

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.entryTime BETWEEN :start AND :end")
    List<ParkingRecord> findAllWithinPeriod(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.exitTime IS NULL")
    long countOccupied();
}

