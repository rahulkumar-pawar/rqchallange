package com.example.rqchallenge;

import com.example.rqchallenge.config.CommonConfig;
import com.example.rqchallenge.employees.Employee;
import com.example.rqchallenge.employees.EmployeeService;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.util.CommonAPIClient;
import com.example.rqchallenge.util.SearchEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class RqChallengeApplicationTests {

    @Mock
    CommonAPIClient client;
    @Mock
    CommonConfig config;
    @Mock
    SearchEngine engine;
    @Spy
    @InjectMocks
    EmployeeService service;

        /* Sample data for reference
        {"id":2, "employee_name":"Garrett Winters","employee_salary":170750,"employee_age":63,"profile_image":""},
        {"id":3, "employee_name":"Ashton Cox","employee_salary":86000,"employee_age":66,"profile_image":""},
        {"id":4, "employee_name":"Cedric Kelly","employee_salary":433060,"employee_age":22,"profile_image":""}
        */
    @Test
    void getAllEmployees() throws Exception {


        List<JSONObject> list = new ArrayList<>();
        JSONObject empOne = new JSONObject("{\"id\":2, \"employee_name\":\"Garrett Winters\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"}");
        JSONObject empTwo = new JSONObject("{\"id\":3, \"employee_name\":\"Ashton Cox\",\"employee_salary\":86000,\"employee_age\":66,\"profile_image\":\"\"}");
        JSONObject empThree = new JSONObject("{\"id\":4, \"employee_name\":\"Cedric Kelly\",\"employee_salary\":433060,\"employee_age\":22,\"profile_image\":\"\"}");

        list.add(empOne);
        list.add(empTwo);
        list.add(empThree);

        JSONArray array = new JSONArray(list);
        JSONObject responseData = new JSONObject();
        responseData.put("data",array);
        responseData.put("status", "Success");

        Optional response = Optional.ofNullable(responseData.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(response);

        //test
        List<Employee> employees = service.getAllEmployees();

        assertEquals(3, employees.size());
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {


        List<JSONObject> list = new ArrayList<>();
        JSONObject empOne = new JSONObject("{\"id\":2, \"employee_name\":\"Garrett cox\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"}");
        list.add(empOne);


        ObjectMapper mapper =  new ObjectMapper();
        Employee emp = mapper.readValue(empOne.toString(), Employee.class);
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(emp);

        JSONArray array = new JSONArray(list);
        JSONObject responseData = new JSONObject();
        responseData.put("data",array);
        responseData.put("status", "Success");

        Optional response = Optional.ofNullable(responseData.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(response);
        when(engine.searchByName(any(),anyString())).thenReturn(mockEmployeeList);

        //test
        List<Employee> employees = service.getEmployeesByNameSearch("cox");

        assertEquals(1, employees.size());
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }
    @Test
    void getEmployeeById() throws Exception {
        JSONObject empOne = new JSONObject("{\"id\":2, \"employee_name\":\"Garrett cox\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"}");

        ObjectMapper mapper =  new ObjectMapper();
        Employee mockEmp = mapper.readValue(empOne.toString(), Employee.class);
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(mockEmp);

        JSONObject responseData = new JSONObject();
        responseData.put("data",empOne.toString());
        responseData.put("status", "Success");

        Optional response = Optional.ofNullable(responseData.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(response);

        //test
        Employee employee = service.getEmployeeById("2");

        assertEquals(mockEmp.getEmployeeName(), employee.getEmployeeName());
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        List<JSONObject> list = new ArrayList<>();
        JSONObject empOne = new JSONObject("{\"id\":2, \"employee_name\":\"Garrett cox\",\"employee_salary\":735000,\"employee_age\":63,\"profile_image\":\"\"}");
        list.add(empOne);

        JSONObject responseData = new JSONObject();
        responseData.put("data",list.toString());
        responseData.put("status", "Success");

        Optional response = Optional.ofNullable(responseData.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(response);
        when(engine.getHighestSalaryOfEmployees(any())).thenReturn(735000);
        //test
        Integer salary = service.getHighestSalaryOfEmployees();

        assertEquals(735000, salary);
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        List<JSONObject> list = new ArrayList<>();
        JSONObject empOne = new JSONObject("{\"id\":2, \"employee_name\":\"Garrett cox\",\"employee_salary\":735000,\"employee_age\":63,\"profile_image\":\"\"}");
        list.add(empOne);
        String listOfEmployees = "Paul Byrd, Yuri Berry, Charde Marshall, Cedric Kelly, Tatyana Fitzpatrick, Brielle Williamson, Jenette Caldwell, Quinn Flynn, Rhona Davidson, Tiger Nixon";
        List<String> items = Arrays.asList(listOfEmployees.split("\\s*,\\s*"));

        JSONObject responseData = new JSONObject();
        responseData.put("data",list.toString());
        responseData.put("status", "Success");

        Optional response = Optional.ofNullable(responseData.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(response);
        when(engine.getTopRecordsBasedOnSalary(any(),anyInt())).thenReturn(items);
        //test
        List<String> names = service.getTopTenHighestEarningEmployeeNames();

        assertEquals(items.size(), names.size());
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void createEmployee() throws Exception {
        JSONObject empOne = new JSONObject("{\"employee_name\":\"Garrett cox\",\"employee_salary\":735000,\"employee_age\":63,\"profile_image\":\"\"}");

        JSONObject response = new JSONObject();
        response.put("data",empOne.toString());

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(Optional.ofNullable(response.toString()));
        //test
        Employee employee = service.createEmployee(empOne.toMap());

        assertEquals(employee.getEmployeeName(), empOne.get("employee_name"));
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        JSONObject empOne = new JSONObject("{\"id\":\"1\",\"employee_name\":\"Garrett cox\",\"employee_salary\":735000,\"employee_age\":63,\"profile_image\":\"\"}");
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("status", "success");
        String mockResult= String.format("Employee record naming '%s' is deleted", empOne.get("employee_name"));

        ObjectMapper mapper =  new ObjectMapper();
        Employee mockEmp = mapper.readValue(empOne.toString(), Employee.class);

        when(client.getResponseFromDummyAPI(anyString(),anyString(),any())).thenReturn(Optional.ofNullable(mockResponse.toString()));
        doReturn(Optional.of(mockEmp)).when(service).getEmployeeIfValid(anyString());

        //test
        String result = service.deleteEmployeeById("1");

        assertEquals(mockResult, result);
        verify(client, times(1)).getResponseFromDummyAPI(anyString(),anyString(),any());
    }

    @Test
    void contextLoads() {
    }

}
