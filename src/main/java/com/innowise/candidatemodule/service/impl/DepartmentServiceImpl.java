package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.Department;
import com.innowise.candidatemodule.domain.Employee;
import com.innowise.candidatemodule.repository.DepartmentRepository;
import com.innowise.candidatemodule.repository.EmployeeRepository;
import com.innowise.candidatemodule.service.api.DepartmentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    private EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentServiceImpl (final DepartmentRepository departmentRepository,
                                  final EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Department save(final Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void reset(@NonNull final Department department) {

        departmentRepository.delete(department);
    }

    @Override
    @Transactional
    public Department update(final Department department) {
        Department result = departmentRepository.getOne(department.getId());
        result.setDepartmentName(department.getDepartmentName());
        updateEmployees(result, employeeRepository.findAllByDepartmentId(department.getId()));
        return departmentRepository.save(result);
    }

    @Override
    @Transactional
    public Optional<Department> getById(final Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        return Optional.ofNullable(department);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    private void updateEmployees(final Department department, final List<Employee> employees) {
        for(Employee employee : employees) {
           employee.setDepartment(department);
        }
        employeeRepository.saveAll(employees);
    }
}
