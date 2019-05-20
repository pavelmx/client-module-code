package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee save(Employee employee);

    void reset(Employee employee);

    Employee update(Employee employee);

    List<Employee> getEmployeesByDepartmentId(Long departmentId);

    Optional<Employee> getById(Long id);

    List<Employee> getPage(int page, int size, String sortColumn, String sortDirection);

    List<Employee> getAll();
}
