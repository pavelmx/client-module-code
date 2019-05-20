package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.InterviewEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewEmployeeRepository extends JpaRepository<InterviewEmployee, Long> {

    List<InterviewEmployee> findAllByEmployeeId(Long employeeId);

    List<InterviewEmployee> findAllByInterviewId(Long interviewId);
}
