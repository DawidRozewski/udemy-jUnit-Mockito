package com.example.udemyjunitmockito.repository;

import com.example.udemyjunitmockito.model.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
    }

    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // when
        Employee savedEmployee = employeeRepository.save(employee);
        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeesList() {
        // given
        Employee employee1 = Employee.builder()
                .firstName("Julia")
                .lastName("Wronek")
                .email("wronek@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        // when
        List<Employee> employeeList = employeeRepository.findAll();
        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
        // when
        Employee employeeFromDB = employeeRepository.findById(employee.getId()).get();
        // then
        assertThat(employeeFromDB).isNotNull();
    }

    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
        // when
        Employee employeeFromDB = employeeRepository.findByEmail(employee.getEmail()).get();
        // then
        assertThat(employeeFromDB).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        employeeRepository.save(employee);
        // when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("updatedEmail@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);
        // then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("updatedEmail@gmail.com");
    }

    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given
        employeeRepository.save(employee);
        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeById = employeeRepository.findById(employee.getId());
        // then
        assertThat(employeeById).isEmpty();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
              // when
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());
        // then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLParams_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
        // when
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());
        // then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
        // when
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
        // then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        // given
        employeeRepository.save(employee);
        // when
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());
        // then
        assertThat(savedEmployee).isNotNull();
    }

}