package com.example.udemyjunitmockito.controller;
import com.example.udemyjunitmockito.model.Employee;
import com.example.udemyjunitmockito.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class EmployeeControllerTest {

    public static final long EMPLOYEE_ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));
        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));

    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        // given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().id(1L).firstName("Ramesh").lastName("Ramesh").email("ramesh@gmail.com").build());
        listOfEmployees.add(Employee.builder().id(1L).firstName("Ramesh").lastName("Ramesh").email("ramesh@gmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName(value = "positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenEmployeeObject() throws Exception {
        // given
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        given(employeeService.getEmployeeById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", EMPLOYEE_ID));
        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));
    }

    @DisplayName(value = "negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeId_thenReturnEmpty() throws Exception {
        // given
        given(employeeService.getEmployeeById(EMPLOYEE_ID)).willReturn(Optional.empty());
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", EMPLOYEE_ID));
        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenUpdateEmployeeObject() throws Exception {
        // given
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("BlaBla")
                .email("ramesh@gmail.com")
                .build();
        given(employeeService.getEmployeeById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", EMPLOYEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void givenNonExistingEmployee_whenUpdateEmployee_then404() throws Exception {
        // given
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        given(employeeService.getEmployeeById(EMPLOYEE_ID)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", EMPLOYEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenRemoveEmployeeObject() throws Exception {
        // given
        willDoNothing().given(employeeService).deleteEmployee(EMPLOYEE_ID);
        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", EMPLOYEE_ID));
        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }

}