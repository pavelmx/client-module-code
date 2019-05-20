package com.innowise.candidatemodule.repository;

import com.innowise.candidatemodule.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

   Optional<Attachment> findById(Long id);

   List<Attachment> findAllByCandidateId(Long candidateId);
}
