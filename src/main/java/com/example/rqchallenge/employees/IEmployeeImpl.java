package com.example.rqchallenge.employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class IEmployeeImpl implements IEmployeeController{

    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees(){
        try{
            return ResponseEntity.ok().body(employeeService.getAllEmployees());
        } catch (Exception e) {
                log.error("Error occurred while retrieving all employee data",e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        try{
            return ResponseEntity.ok().body(employeeService.getEmployeesByNameSearch(searchString));
        } catch (Exception e) {
            log.error("Error occurred while searching employee data",e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try{
            return ResponseEntity.ok().body(employeeService.getEmployeeById(id));
        } catch (Exception e) {
            log.error(String.format("Error occurred while retrieving information of EmployeeID: %s", id),e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try{
            return ResponseEntity.ok().body(employeeService.getHighestSalaryOfEmployees());
        } catch (Exception e) {
            log.error("Error occurred while retrieving highest salary", e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try{
            return ResponseEntity.ok().body(employeeService.getTopTenHighestEarningEmployeeNames());
        } catch (Exception e) {
            log.error("Error occurred while retrieving Top ten highest salary earning employee", e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        try{
            return ResponseEntity.ok().body(employeeService.createEmployee(employeeInput));
        } catch (Exception e) {
            log.error("Error occurred while creating employees", e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try{
            return ResponseEntity.ok().body(employeeService.deleteEmployeeById(id));
        } catch (Exception e) {
            log.error(String.format("Error occurred while deleting employee ID : %s", id), e);
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
