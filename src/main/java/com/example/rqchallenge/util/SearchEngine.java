package com.example.rqchallenge.util;

import com.example.rqchallenge.employees.Employee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class SearchEngine {

    public List<Employee> searchByName(List<Employee> employees, String filter){
        return employees.stream()
                .filter(item -> item.getEmployeeName().toLowerCase(Locale.ROOT).contains(filter.toLowerCase()))
                .collect(Collectors.toList());
    }
}
