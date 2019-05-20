package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.InterviewEmployee;
import com.innowise.candidatemodule.repository.InterviewEmployeeRepository;
import com.innowise.candidatemodule.service.api.InterviewEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewEmployeeServiceImpl implements InterviewEmployeeService {

    private InterviewEmployeeRepository interviewEmployeeRepository;


    @Autowired
    public InterviewEmployeeServiceImpl(final InterviewEmployeeRepository interviewEmployeeRepository) {
        this.interviewEmployeeRepository = interviewEmployeeRepository;
    }

    @Override
    public Optional<InterviewEmployee> getById(Long id) {
        InterviewEmployee interviewEmployee = interviewEmployeeRepository.findById(id).orElse(null);
        return Optional.ofNullable(interviewEmployee);
    }

    @Override
    @Transactional
    public InterviewEmployee save(final InterviewEmployee interviewEmployee) {
        return interviewEmployeeRepository.save(interviewEmployee);
    }

    @Override
    @Transactional
    public void reset(final InterviewEmployee interviewEmployee) {
        interviewEmployeeRepository.delete(interviewEmployee);
    }

    @Override
    @Transactional
    public InterviewEmployee update(final InterviewEmployee interviewEmployee) {
        InterviewEmployee result = interviewEmployeeRepository.getOne(interviewEmployee.getId());
        result.setInterview(interviewEmployee.getInterview());
        result.setEmployee(interviewEmployee.getEmployee());
        result.setFeedback(interviewEmployee.getFeedback());
        return interviewEmployeeRepository.save(result);

    }

    @Override
    @Transactional
    public List<InterviewEmployee> getAllByEmployeeId(final Long employeeId) {
        return interviewEmployeeRepository.findAllByEmployeeId(employeeId);
    }

    @Override
    @Transactional
    public List<InterviewEmployee> getAllByInterviewId(final Long interviewId) {
        return interviewEmployeeRepository.findAllByInterviewId(interviewId);
    }

    @Override
    @Transactional
    public List<InterviewEmployee> getAll(){
        return interviewEmployeeRepository.findAll();
    }
}
