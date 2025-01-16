package com.ozer.workTimeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin("*")
@RequestMapping("/api/work-sessions")
@RestController
public class WorkSessionController {
    @Autowired
    private WorkSessionService workSessionService;

    @PostMapping("/start/{employee}")
    public ResponseEntity<WorkSession> startSession(@PathVariable String employee) {
        WorkSession workSession = workSessionService.startSession(employee);
        if (workSession == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(workSession);
    }

    @PostMapping("/end/{employee}")
    public ResponseEntity<WorkSession> endSession(@PathVariable String employee) {
        WorkSession workSession = workSessionService.endSession(employee);
        if (workSession == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(workSession);
    }

    @PutMapping("update/")
    public ResponseEntity updateSession(@RequestBody WorkSessionDTO workSessionDTO) {
        try  {
            WorkSession workSession = workSessionService.updateSession(workSessionDTO);
            return ResponseEntity.ok(workSession);
        } catch (NoSuchElementException nse) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nse.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/admin/all/{employee}")
    public ResponseEntity getAllWorkingTime(@PathVariable String employee) {
        try {
            List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeName(employee);
            return ResponseEntity.ok(workSessions);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (NoSuchElementException nse) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nse.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/admin/date/{employee}/{date}")
    public ResponseEntity getDateWorkingTime(@PathVariable String employee, @PathVariable LocalDate date) {
        try {
            List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeNameAndDate(employee, date);
            return ResponseEntity.ok(workSessions);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (NoSuchElementException nse) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nse.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/admin/date/{employee}/{startDate}/{endDate}")
    public ResponseEntity getDateBetweenWorkingTime(@PathVariable String employee, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        try {
            List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeNameAndDateBetweeen(employee,startDate, endDate);
            return ResponseEntity.ok(workSessions);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (NoSuchElementException nse) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nse.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("employee")
    public Optional<List<Employee>> getDateBetweenWorkingTime() {
        return Optional.ofNullable(workSessionService.getAllEmployees());
    }

    @PostMapping("/employee/add/{name}")
    public ResponseEntity<Employee> addEmployee(@PathVariable String name) {
        Employee employee = workSessionService.addEmployee(name);

        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/employee/delete/{name}")
    public ResponseEntity delete(@PathVariable String name) {
        try {
            Employee employee = workSessionService.deleteEmployee(name);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }
    }

}
