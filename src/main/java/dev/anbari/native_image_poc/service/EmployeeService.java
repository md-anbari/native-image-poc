package dev.anbari.native_image_poc.service;

import dev.anbari.native_image_poc.domain.Employee;
import dev.anbari.native_image_poc.domain.EmployeeEntity;
import dev.anbari.native_image_poc.repository.EmployeeRepository;
import dev.anbari.native_image_poc.util.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        log.info("Saving employee: {}", employee.getName());
        EmployeeEntity saved = employeeRepository.save(EmployeeMapper.toEntity(employee));
        return EmployeeMapper.toDto(saved);
    }

    public List<Employee> list() {
        log.info("Listing all employees");
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }
}