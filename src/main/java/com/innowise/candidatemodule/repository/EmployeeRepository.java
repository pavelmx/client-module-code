package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByDepartmentId(Long departmentId);
}
