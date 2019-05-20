package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository  extends JpaRepository<Interview, Long> {

    List<Interview> findAllByCandidateId(Long candidateId);
}
