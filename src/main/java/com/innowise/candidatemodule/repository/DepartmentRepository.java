package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
