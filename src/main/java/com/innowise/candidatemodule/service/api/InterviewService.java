package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.Interview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewService {

    Interview save(Interview interview);

    void reset(Interview interview);

    Interview update(Interview interview);

    Optional<Interview> getById(Long id);

    List<Interview> getInterviewsByDate(LocalDateTime interviewDate);

    List<Interview> getInterviewsByCandidateId(Long candidateId);

    List<Interview> getAll();

    List<Interview> getPage(int page, int size, String sortColumn, String sortDirection);
}
