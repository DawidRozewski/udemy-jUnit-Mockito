package com.example.udemyjunitmockito.integration;

import com.example.udemyjunitmockito.model.Employee;
import com.example.udemyjunitmockito.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EmployeeControllerITWithTestContainerTest {

    public static final long NON_EXISTING_ID = 99L;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

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
        employeeRepository.saveAll(listOfEmployees);
        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));
        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenEmployeeObject() throws Exception {
        // given
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeId_thenReturnEmpty() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", NON_EXISTING_ID));
        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenUpdateEmployeeObject() throws Exception {
        // given
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("BlaBla")
                .email("updatedRamesh@gmail.com")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
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
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        // when
        ResultActions response = mockMvc.perform(put("/api/employee/{id}", NON_EXISTING_ID));
        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenRemoveEmployeeObject() throws Exception {
        // given
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
        // then
        response.andExpect(status().isOk())
                .andDo(print());
    }


}

