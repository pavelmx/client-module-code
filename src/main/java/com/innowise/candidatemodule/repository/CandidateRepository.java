package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

}
