package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    Candidate save(Candidate candidate);

    void reset(Candidate candidate);

    Candidate update(Candidate candidate);

    Optional<Candidate> getById(Long id);

    List<Candidate> getAll();

    List<Candidate> getPage(int page, int size, String sortColumn, String sortDirection);

}
