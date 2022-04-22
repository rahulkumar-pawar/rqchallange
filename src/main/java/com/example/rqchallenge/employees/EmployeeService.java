/*
 * Employee Service is responsible for all Employee operations.
 * It includes CRUD and some custom search
 */
package com.example.rqchallenge.employees;

import com.example.rqchallenge.config.CommonConfig;
import com.example.rqchallenge.config.Constants;
import com.example.rqchallenge.util.CommonAPIClient;
import com.example.rqchallenge.util.SearchEngine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    CommonAPIClient client;

    @Autowired
    CommonConfig config;

    @Autowired
    SearchEngine searchEngine;

    /**
     *
     * @return List of employees
     * @throws Exception in case any error in processing response
     */
    public List<Employee> getAllEmployees() throws Exception {
        try {
            log.debug("Retrieving all employee details");
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            log.debug("Retrieved all employee details");
            if(response.isPresent() && validateResponse(response.get())) {
                JSONObject jsonObject = new JSONObject(response.get());
                ObjectMapper mapper = new ObjectMapper();
                return Arrays.asList(mapper.readValue(jsonObject.get("data").toString(), Employee[].class));
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
        }
    }

    /**
     *
     * @param searchString
     * @return list of searched employees
     * @throws Exception in case any error in processing response
     */
    public List<Employee> getEmployeesByNameSearch(String searchString) throws Exception {
        try {
            log.debug("Retrieving employees based on searchString - {}", searchString);
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            log.debug("Retrieved search result of employee details");
            if(response.isPresent()) {
                return searchEngine.searchByName(processResponse(response.get()), searchString);
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
        }
    }

    /**
     *
     * @param id Employee ID
     * @return Employee details
     * @throws Exception in case not found or error in processing response
     */
    public Employee getEmployeeById(String id) throws Exception {
        try {
            log.debug("Getting employee based on ID:{}", id);
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%s/%s",config.getBaseUrl(),config.getApiVersion(),config.getEntity(),id),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            log.debug("Retrieved employee details ID:{}",id);
            return getEmployee(response);
        } catch (Exception e) {
            log.error("Error occurred while retrieving the employee details ", e);
            throw new Exception("Error occurred while retrieving the employee details");
        }
    }

    /**
     *
     * @return Get the highest salary of employee
     * @throws Exception in case any error in processing response
     */
    public Integer getHighestSalaryOfEmployees() throws Exception {
        try {
            log.debug("Getting highest salary");
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            log.debug("Retrieved highest salary");
            if(response.isPresent()) {
                return searchEngine.getHighestSalaryOfEmployees(processResponse(response.get()));
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving highest salary of an employee ", e);
            throw new Exception("Error occurred while retrieving highest salary of an employee");
        }
    }

    /**
     *
     * @return List of top ten employees who is having the highest salary
     * @throws Exception in case any error in processing response
     */
    public List<String> getTopTenHighestEarningEmployeeNames() throws Exception {
        try {
            log.debug("Getting top ten salaried employee");
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            log.debug("retrieved top ten salaried employee");
            if(response.isPresent()) {
                return searchEngine.getTopRecordsBasedOnSalary(processResponse(response.get()), Constants.TOP_RECORDS);
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.error("Error occurred while retrieving top ten highest earning employee ", e);
            throw new Exception("Error occurred while retrieving top ten highest earning employee");
        }
    }

    public Employee createEmployee(Map<String, Object> employeeInput) throws Exception {
        try {

            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/create",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.POST.toString(), Optional.ofNullable(new JSONObject(employeeInput).toString()));
            return getEmployee(response);
        } catch (Exception e) {
            log.error("Error occurred while creating the employee", e);
            throw new Exception("Error occurred while creating the employee");
        }
    }

    public String deleteEmployeeById(String id) throws Exception {
        try {
            Optional<Employee> employee = getEmployeeIfValid(id);
            if(Objects.nonNull(employee) && employee.isPresent()){
                Optional<String> response =  client.getResponseFromDummyAPI(
                        String.format("%s%s/delete/%s",config.getBaseUrl(),config.getApiVersion(),config.getEntity(),id),
                        Constants.HttpMethods.DELETE.toString(), Optional.empty());
                 if(validateResponse(response.get())){
                     return String.format("Employee record naming '%s' is deleted", employee.get().getEmployeeName());
                 }else{
                     throw new Exception(String.format("Error occurred while deleting the employee ID: %s", id));
                 }
            }else{
                throw new Exception(String.format("Error occurred while deleting the employee ID: %s", id));
            }
        } catch (Exception e) {
            log.error(String.format("Error occurred while deleting the employee ID: %s", id), e);
            throw new Exception(String.format("Error occurred while deleting the employee ID: %s", id));
        }
    }

    public Optional<Employee> getEmployeeIfValid(String id) {
        try{
            Optional<Employee> employee = Optional.ofNullable(getEmployeeById(id));
            return employee;
        } catch (Exception e) {
            log.error(String.format("Record not found with employee ID: %s", id), e);
            return Optional.empty();
        }
    }

    private Employee getEmployee(Optional<String> response) throws Exception {
        if(response.isPresent() && validateResponse(response.get())) {
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(response.get());
            return mapper.readValue(jsonObject.get("data").toString(), Employee.class);
        }else{
            throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
        }
    }

    private List<Employee> processResponse(String responseData) throws Exception {

        try {
            if(validateResponse(responseData)){
                JSONObject jsonObject = new JSONObject(responseData);
                ObjectMapper mapper = new ObjectMapper();
                List<Employee> employees = Arrays.asList(mapper.readValue(jsonObject.get("data").toString(), Employee[].class));
                return employees;
            }else{
                throw new Exception("Error while processing the response");
            }
        } catch (JsonProcessingException e) {
            throw new Exception("Error while processing the response",e);
        }
    }

    private boolean validateResponse(String responseData) throws Exception {
        try{
            JSONObject jsonObject = new JSONObject(responseData);
            return Boolean.TRUE;
        }catch (Exception e){
            throw new Exception("Error while parsing the response",e);
        }
    }
}
