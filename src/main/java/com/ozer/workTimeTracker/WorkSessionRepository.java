package com.ozer.workTimeTracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, Long> {


    @Query("SELECT w FROM WorkSession w WHERE w.employeeName = :name AND w.endTime IS NULL")
    Optional<WorkSession> findOpenSessionByMitarbeiterName(@Param("name") String employeeName);

    Optional<List<WorkSession>> findByEmployeeName(@Param("name") String employeeName);

    Optional<List<WorkSession>> findByEmployeeNameAndDate(@Param("name") String employeeName, @Param("date") LocalDate date);

    Optional<List<WorkSession>> findByEmployeeNameAndDateBetween(
            @Param("name") String employeeName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
