package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.Employee;
import com.innowise.candidatemodule.domain.InterviewEmployee;
import com.innowise.candidatemodule.repository.DepartmentRepository;
import com.innowise.candidatemodule.repository.EmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewEmployeeRepository;
import com.innowise.candidatemodule.service.api.EmployeeService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl  implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private DepartmentRepository departmentRepository;

    private InterviewEmployeeRepository interviewEmployeeRepository;

    @Autowired
    public EmployeeServiceImpl(final EmployeeRepository employeeRepository,
                               final DepartmentRepository departmentRepository,
                               final InterviewEmployeeRepository interviewEmployeeRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.interviewEmployeeRepository = interviewEmployeeRepository;
    }


    @Override
    @Transactional
    public Employee save(final Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void reset(@NonNull final  Employee employee) {
        Employee result = employeeRepository.getOne(employee.getId());
        result.setDeleted(true);
        updateInterviewEmployees(result, interviewEmployeeRepository.findAllByEmployeeId(employee.getId()));
        employeeRepository.save(result);
    }

    @Override
    @Transactional
    public Employee update(final Employee employee) {
        Employee result = employeeRepository.getOne(employee.getId());
        result.setFirstName(employee.getFirstName());
        result.setLastName(employee.getLastName());
        result.setDepartment(employee.getDepartment());
        updateInterviewEmployees(result, interviewEmployeeRepository.findAllByEmployeeId(employee.getId()));
        return employeeRepository.save(result);
    }

    @Override
    @Transactional
    public List<Employee> getEmployeesByDepartmentId(final Long departmentId) {
        return employeeRepository.findAllByDepartmentId(departmentId);
    }

    @Override
    @Transactional
    public Optional<Employee> getById(final Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        return Optional.ofNullable(employee);
    }

    @Override
    @Transactional
    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public List<Employee> getPage(int page, int size, String sortColumn, String sortDirection) {
        PageRequest pageRequest;
        if (sortDirection.equals("NULL")) {
            pageRequest = PageRequest.of(page, size);
        } else {
            pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortDirection), sortColumn);
        }
        return employeeRepository.findAll(pageRequest).getContent();
    }

    private void updateInterviewEmployees(final Employee employee, List<InterviewEmployee> interviewEmployees) {
        for(InterviewEmployee interviewEmployee : interviewEmployees) {
            interviewEmployee.setEmployee(employee);
        }
        interviewEmployeeRepository.saveAll(interviewEmployees);
    }
}
