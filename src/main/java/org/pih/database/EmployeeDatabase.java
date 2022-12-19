package org.pih.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pih.dto.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EmployeeDatabase {

    public static List<Employee> fetchEmployees(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Employee> employees = objectMapper.readValue(new File("employee.json"), new TypeReference<List<Employee>>() {
            });
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
