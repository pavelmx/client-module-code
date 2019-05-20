package com.innowise.candidatemodule.service.api;

import com.innowise.candidatemodule.domain.Attachment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AttachmentService {

    Attachment save(Attachment attachment);

    void reset(Attachment attachment);

    Attachment update(Attachment attachment);

    Optional<Attachment> getById(Long id);

    List<Attachment> getAttachmentsByCandidateId(Long candidateId);

    Attachment create(MultipartFile file, Attachment attachment);

    ResponseEntity<ByteArrayResource> downloadById(Long id);
}
