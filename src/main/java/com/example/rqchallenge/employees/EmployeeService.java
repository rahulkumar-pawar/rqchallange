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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    CommonAPIClient client;

    @Autowired
    CommonConfig config;

    @Autowired
    SearchEngine searchEngine;

    public List<Employee> getAllEmployees() throws Exception {
        try {
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            if(response.isPresent() && validateResponse(response.get())) {
                JSONObject jsonObject = new JSONObject(response.get());
                ObjectMapper mapper = new ObjectMapper();
                return Arrays.asList(mapper.readValue(jsonObject.get("data").toString(), Employee[].class));
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.debug("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
        }
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) throws Exception {
        try {
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%ss",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            if(response.isPresent()) {
                return searchEngine.searchByName(processResponse(response.get()), searchString);
            }else{
                throw new Exception(String.format("Invalid response received from URL: %s", config.getBaseUrl()));
            }
        } catch (Exception e) {
            log.debug("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
        }
    }

    public Employee getEmployeeById(String id) throws Exception {
        try {
            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s/%s/%s",config.getBaseUrl(),config.getApiVersion(),config.getEntity(),id),
                    Constants.HttpMethods.GET.toString(), Optional.empty());
            return getEmployee(response);
        } catch (Exception e) {
            log.debug("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        return null;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    public Employee createEmployee(Map<String, Object> employeeInput) throws Exception {
        try {

            Optional<String> response =  client.getResponseFromDummyAPI(
                    String.format("%s%s",config.getBaseUrl(),config.getApiVersion(),config.getEntity()),
                    Constants.HttpMethods.POST.toString(), Optional.ofNullable(new JSONObject(employeeInput).toString()));
            return getEmployee(response);
        } catch (Exception e) {
            log.debug("Error occurred while retrieving the all employees data ", e);
            throw new Exception("Error occurred while retrieving the all employees data");
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

    public String deleteEmployeeById(String id) {
        return null;
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
