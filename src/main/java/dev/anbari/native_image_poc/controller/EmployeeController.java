package dev.anbari.native_image_poc.controller;

import dev.anbari.native_image_poc.domain.Employee;
import dev.anbari.native_image_poc.service.DocumentService;
import dev.anbari.native_image_poc.service.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DocumentService documentService;

    /**
     * Create a new Employee
     * Only ADMIN can create
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Employee createEmployee(@RequestBody Employee employee) {
        log.info("Creating employee: {}", employee.getName());
        return employeeService.save(employee);
    }

    /**
     * Get all Employees
     * ADMIN and USER can list
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeService.list();
    }

    /**
     * Export all Employees as Excel file
     * ADMIN and USER can download
     */
    @GetMapping(value = "/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void exportExcel(HttpServletResponse response) throws IOException {
        log.info("Exporting employees to Excel");
        documentService.exportExcel(response);
    }

}

