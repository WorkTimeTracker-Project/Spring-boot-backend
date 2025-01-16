package com.ozer.workTimeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WorkSessionService {

    @Autowired
    WorkSessionRepository workSessionRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public List<WorkSessionDTO> getByEmployeeName(String employee) {
        if(employee == null) {
            throw new IllegalArgumentException("employee name cannot be null");
        }
        List<WorkSession> workSessions = workSessionRepository.findByEmployeeName(employee)
                .orElseThrow(() -> new NoSuchElementException("no worksessions for " + employee + " found "));

        return mapToWorksessionDTO(workSessions);
    }

    public List<WorkSessionDTO> getByEmployeeNameAndDate(String employee, LocalDate date) {
        if(employee == null) {
            throw new IllegalArgumentException("employee name cannot be null");
        }
        List<WorkSession> workSessions = workSessionRepository.findByEmployeeNameAndDate(employee, date)
                .orElseThrow(() -> new NoSuchElementException("no worksessions for " + employee + " from " + date + " found "));

        return mapToWorksessionDTO(workSessions);
    }


    public List<WorkSessionDTO> getByEmployeeNameAndDateBetweeen(String employee, LocalDate startDate, LocalDate endDate) {
        if(employee == null) {
            throw new IllegalArgumentException("employee name cannot be null");
        }
        List<WorkSession> workSessions = workSessionRepository.findByEmployeeNameAndDateBetween(employee, startDate, endDate)
                .orElseThrow(() -> new NoSuchElementException("no worksessions for " + employee + " from " + startDate + " to " + endDate + " found "));

        return mapToWorksessionDTO(workSessions);
    }


    private static List<WorkSessionDTO> mapToWorksessionDTO(List<WorkSession> workSessions) {
        List <WorkSessionDTO> workSessionDTOList = new ArrayList<>();
        if (!workSessions.isEmpty()) {
            for (WorkSession workSession : workSessions) {
                WorkSessionDTO workSessionDTO = new WorkSessionDTO();
                workSessionDTO.setId(workSession.getId());
                workSessionDTO.setEmployeeName(workSession.getEmployeeName());
                workSessionDTO.setStartTime(workSession.getStartTime().toLocalTime().truncatedTo(ChronoUnit.SECONDS));

                if(workSession.getEndTime() == null)
                {
                    workSessionDTO.setEndTime(null);
                    workSessionDTO.setTotalWorkTime(null);
                } else {
                    workSessionDTO.setEndTime(workSession.getEndTime().toLocalTime().truncatedTo(ChronoUnit.SECONDS));
                    workSessionDTO.setTotalWorkTime(workSession.getTotalWorkTime());
                }

                workSessionDTO.setDate(workSession.getDate());
                workSessionDTOList.add(workSessionDTO);
            }
        }
        return workSessionDTOList;
    }




    public WorkSession startSession(String employee) {
        if(employee == null) {
            throw new IllegalArgumentException("employee name cannot be null");
        }

        Optional<WorkSession> openSession = workSessionRepository.findOpenSessionByMitarbeiterName(employee);

        if(openSession.isPresent()) {
            WorkSession workSession = openSession.get();
            LocalDate startDate = workSession.getStartTime().toLocalDate();
            LocalDate currentDate = LocalDate.now();

            if(startDate.equals(currentDate)) {
                System.out.println("du bist bereits eingestempelt");
                return workSession;
            } else {
                System.out.println("Vortag nicht mit gehen gestempelt, wird automatisch geschlossen");
                workSession.setEndTime(LocalDateTime.of(startDate, LocalTime.of(23, 59)));
                workSessionRepository.save(workSession);
                return null;
            }
        }
        WorkSession workSession = new WorkSession();
        workSession.setEmployeeName(employee);
        workSession.setStartTime(LocalDateTime.now());
        workSessionRepository.save(workSession);
        return workSession;
    }

    public WorkSession endSession(String employee) {
        if(employee == null) {
            throw new IllegalArgumentException("employee name cannot be null");
        }

        Optional<WorkSession> openSession = workSessionRepository.findOpenSessionByMitarbeiterName(employee);
        if (openSession.isPresent()) {
            WorkSession workSession = openSession.get();
            LocalDate startDate = workSession.getStartTime().toLocalDate();
            LocalDate currentDate = LocalDate.now();

            if(startDate.isBefore(currentDate)) {
                System.out.println("Vortag nicht mit gehen gestempelt, wird automatisch geschlossen");
                workSession.setEndTime(LocalDateTime.of(startDate, LocalTime.of(23, 59)));
                workSessionRepository.save(workSession);
                return null;
            } else if (startDate.equals(currentDate)) {
                workSession.setEndTime(LocalDateTime.now());
                workSessionRepository.save(workSession);
                return workSession;
            }
        }
        System.out.println("keine offene Sitzung vorhanden");
        return null;
    }


    public WorkSession updateSession(WorkSessionDTO workSessionDTO) {
        WorkSession workSession  = workSessionRepository.findById(workSessionDTO.getId())
                .orElseThrow(() -> new NoSuchElementException("WorkSession with id " + workSessionDTO.getId() + " not found"));
        workSession.setEmployeeName(workSessionDTO.getEmployeeName());
        workSession.setStartTime(workSessionDTO.getStartTime().atDate(workSessionDTO.getDate()));
        workSession.setEndTime(workSessionDTO.getEndTime().atDate(workSessionDTO.getDate()));
        workSession.setDate(workSessionDTO.getDate());

        workSessionRepository.save(workSession);

        return workSession;
    }

    public Employee addEmployee(String name) {
        Employee newEmployee = new Employee();
        newEmployee.setName(name);
        employeeRepository.save(newEmployee);
        return newEmployee;
    }

    public Employee deleteEmployee(String name) {
        Employee employee = employeeRepository.findByName(name)
                        .orElseThrow(() -> new NoSuchElementException("no employee with name " + name + " found"));
        employeeRepository.delete(employee);
        return employee;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


}
