package dev.anbari.native_image_poc.service;

import dev.anbari.native_image_poc.domain.DetailEntity;
import dev.anbari.native_image_poc.domain.EmployeeEntity;
import dev.anbari.native_image_poc.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final EmployeeRepository employeeRepository;

    /**
     * Export Employees to Excel (.xlsx)
     */
    public void exportExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        List<EmployeeEntity> employees = employeeRepository.findAll();
        log.info("Generating Excel for {} employees", employees.size());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employees");

            // Header Row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Position");
            headerRow.createCell(3).setCellValue("Salary");
            headerRow.createCell(4).setCellValue("Contract Type");

            // Data Rows
            int rowIdx = 1;
            for (EmployeeEntity emp : employees) {
                for (DetailEntity detail : emp.getDetails()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(emp.getId());
                    row.createCell(1).setCellValue(emp.getName());
                    row.createCell(2).setCellValue(emp.getPosition());
                    row.createCell(3).setCellValue(detail.getSalary());
                    row.createCell(4).setCellValue(detail.getContractType());
                }
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            log.info("Excel file generated successfully.");
        }
    }
}