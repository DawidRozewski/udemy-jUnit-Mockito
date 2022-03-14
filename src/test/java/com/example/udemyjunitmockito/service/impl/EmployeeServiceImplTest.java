package com.example.udemyjunitmockito.service.impl;

import com.example.udemyjunitmockito.exception.ResourceNotFoundException;
import com.example.udemyjunitmockito.model.Employee;
import com.example.udemyjunitmockito.repository.EmployeeRepository;
import com.example.udemyjunitmockito.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    public static final long EMPLOYEE_ID = 1L;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);
        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(1L);
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        // when
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("kowalski@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        // when
        List<Employee> allEmployees = employeeService.getAllEmployees();
        // then
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(2);
    }

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        // when
        List<Employee> allEmployees = employeeService.getAllEmployees();
        // then
        assertThat(allEmployees).isEmpty();
        assertThat(allEmployees.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findById(EMPLOYEE_ID)).willReturn(Optional.of(employee));
        // when
        Employee savedEmployee = employeeService.getEmployeeById(EMPLOYEE_ID).get();
        // then
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }

    @Test
    public void givenEmployee_whenUpdateEmployee_theReturnUpdatedEmployee() {
        // given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("updatedEmail@gmail.com");
        // when
        Employee updatedEmployee = employeeService.updateEmployee(this.employee);
        // then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("updatedEmail@gmail.com");
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given
        willDoNothing().given(employeeRepository).deleteById(EMPLOYEE_ID);
        // when
        employeeService.deleteEmployee(EMPLOYEE_ID);
        // then
        verify(employeeRepository, times(1)).deleteById(EMPLOYEE_ID);
    }

}