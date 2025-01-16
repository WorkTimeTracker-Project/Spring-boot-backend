package com.ozer.workTimeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
    public ResponseEntity<WorkSession> updateSession(@RequestBody WorkSessionDTO workSessionDTO) {
        WorkSession workSession = workSessionService.updateSession(workSessionDTO);
        if (workSession == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(workSession);
    }

    @GetMapping("/admin/all/{employee}")
    public List<WorkSessionDTO> getAllWorkingTime(@PathVariable String employee) {
        List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeName(employee);
        return workSessions;
    }

    @GetMapping("/admin/date/{employee}/{date}")
    public List<WorkSessionDTO> getDateWorkingTime(@PathVariable String employee, @PathVariable LocalDate date) {
        List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeNameAndDate(employee, date);
        return workSessions;
    }

    @GetMapping("/admin/date/{employee}/{startDate}/{endDate}")
    public List<WorkSessionDTO> getDateBetweenWorkingTime(@PathVariable String employee, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        List<WorkSessionDTO> workSessions = workSessionService.getByEmployeeNameAndDateBetweeen(employee,startDate, endDate);
        return workSessions;
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
    public ResponseEntity<Employee> delete(@PathVariable String name) {
        Employee employee = workSessionService.deleteEmployee(name);

        return ResponseEntity.ok(employee);
    }

}
