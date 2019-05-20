package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.Interview;
import com.innowise.candidatemodule.domain.InterviewEmployee;
import com.innowise.candidatemodule.repository.InterviewEmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewRepository;
import com.innowise.candidatemodule.service.api.InterviewService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {

    private InterviewRepository interviewRepository;

    private InterviewEmployeeRepository interviewEmployeeRepository;

    @Autowired
    public InterviewServiceImpl(final InterviewRepository interviewRepository,
                                final InterviewEmployeeRepository interviewEmployeeRepository) {
        this.interviewRepository = interviewRepository;
        this.interviewEmployeeRepository = interviewEmployeeRepository;
    }

    @Override
    @Transactional
    public Interview save(final Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    @Transactional
    public void reset(@NonNull final Interview interview) {

        interviewRepository.delete(interview);
    }

    @Override
    @Transactional
    public Interview update(final Interview interview) {
        Interview result = interviewRepository.getOne(interview.getId());
        result.setCandidate(interview.getCandidate());
        result.setInterviewDate(interview.getInterviewDate());
        result.setStatus(interview.getStatus());
        updateInterviewEmployees(result, interviewEmployeeRepository.findAllByInterviewId(interview.getId()));
        return interviewRepository.save(result);
    }

    @Override
    @Transactional
    public Optional<Interview> getById(final Long id) {
        Interview interview = interviewRepository.findById(id).orElse(null);
        return Optional.ofNullable(interview);
    }

    @Override
    @Transactional
    public List<Interview> getInterviewsByDate(final LocalDateTime interviewDate) {
        return interviewRepository.findAll().stream()
                .filter(interview1 -> interview1.getInterviewDate() == interviewDate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Interview> getInterviewsByCandidateId(final Long candidateId) {

        return interviewRepository.findAllByCandidateId(candidateId);
    }

    @Override
    @Transactional
    public List<Interview> getAll(){
        return interviewRepository.findAll();
    }

    @Override
    @Transactional
    public List<Interview> getPage(int page, int size, String sortColumn, String sortDirection) {
        PageRequest pageRequest;
        if (sortDirection.equals("NULL")) {
            pageRequest = PageRequest.of(page, size);
        } else {
            pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortDirection), sortColumn);
        }
        return interviewRepository.findAll(pageRequest).getContent();
    }
    private void updateInterviewEmployees(final Interview interview, List<InterviewEmployee> interviewEmployees) {

        for(InterviewEmployee interviewEmployee : interviewEmployees) {
            interviewEmployee.setInterview(interview);
        }
        interviewEmployeeRepository.saveAll(interviewEmployees);
    }
}
