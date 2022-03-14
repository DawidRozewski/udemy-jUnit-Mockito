package com.example.udemyjunitmockito.repository;

import com.example.udemyjunitmockito.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
