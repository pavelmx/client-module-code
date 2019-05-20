package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department save(Department department);

    void reset(Department department);

    Department update(Department department);

    Optional<Department> getById(Long id);

    List<Department> getAll();
}
