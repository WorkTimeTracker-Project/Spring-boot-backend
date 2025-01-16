package com.ozer.workTimeTracker;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class WorkSession {

    public WorkSession() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String totalWorkTime;

    private LocalDate date;

    @PrePersist
    @PreUpdate
    public void calculateTotalWorkTimeAndDate() {
        if (startTime != null && endTime != null) {
            long seconds = Duration.between(startTime, endTime).getSeconds();
            this.totalWorkTime = formatDuration(seconds);
            //this.date = endTime.toLocalDate();
        }
    }


    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        this.date = startTime.toLocalDate();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(String totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

