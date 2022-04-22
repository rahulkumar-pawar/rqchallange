/*
 * Search engine class search on collection provided and given criteria
 */
package com.example.rqchallenge.util;

import com.example.rqchallenge.employees.Employee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class SearchEngine {

    /**
     * search employee by name
     * @param employees
     * @param filter
     * @return
     */
    public List<Employee> searchByName(List<Employee> employees, String filter){
        return employees.stream()
                .filter(item -> item.getEmployeeName().toLowerCase(Locale.ROOT).contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get Top records as per salary
     * @param employees
     * @param totalRecords
     * @return list of employee names
     */
    public List<String> getTopRecordsBasedOnSalary(List<Employee> employees, int totalRecords){
        return employees.stream()
                .sorted((e1, e2)-> e2.getEmployeeSalary().compareTo(e1.getEmployeeSalary()))
                .limit(totalRecords)
                .map(e -> String.valueOf(e.getEmployeeName()))
                .collect(Collectors.toList());
    }

    /**
     * Get the highest salary among all employees
     * @param employees
     * @return highest salary
     */
    public Integer getHighestSalaryOfEmployees(List<Employee> employees) {
        return employees.stream()
                .sorted((e1, e2)-> e2.getEmployeeSalary().compareTo(e1.getEmployeeSalary()))
                .limit(1)
                .collect(Collectors.toList()).get(0).getEmployeeSalary();
    }
}
