package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.InterviewEmployee;

import java.util.List;
import java.util.Optional;

public interface InterviewEmployeeService {

    Optional<InterviewEmployee> getById(Long id);

    InterviewEmployee save(InterviewEmployee interviewEmployee);

    void reset(InterviewEmployee interviewEmployee);

    InterviewEmployee update(InterviewEmployee interviewEmployee);

    List<InterviewEmployee> getAllByEmployeeId(Long employeeId);

    List<InterviewEmployee> getAllByInterviewId(Long interviewId);

    List<InterviewEmployee> getAll();
}
