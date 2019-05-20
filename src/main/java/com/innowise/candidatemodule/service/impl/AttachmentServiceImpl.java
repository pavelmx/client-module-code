package com.innowise.candidatemodule.service.impl;

import com.innowise.candidatemodule.domain.Attachment;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.repository.AttachmentRepository;
import com.innowise.candidatemodule.service.api.AttachmentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private AttachmentRepository attachmentRepository;
    private ServletContext servletContext;
    private static final String PATH = "./src/main/resources/attachments";
    private String fileSeparator = System.getProperty("file.separator");

    @Autowired
    public AttachmentServiceImpl(final AttachmentRepository attachmentRepository,
                                 final ServletContext servletContext) {
        this.attachmentRepository = attachmentRepository;
        this.servletContext = servletContext;
    }

    @Override
    @Transactional
    public Attachment save(final Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Override
    @Transactional
    public void reset(@NonNull final Attachment attachment) {
        attachmentRepository.delete(attachment);
    }

    @Override
    @Transactional
    public Attachment update(final Attachment attachment) {
        Attachment result = attachmentRepository.getOne(attachment.getId());
        result.setFileName(attachment.getFileName());
        result.setPath(attachment.getPath());
        result.setCandidate(attachment.getCandidate());
        return attachmentRepository.save(result);
    }

    @Override
    @Transactional
    public Optional<Attachment> getById(final Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElse(null);
        return Optional.ofNullable(attachment);
    }

    @Override
    @Transactional
    public List<Attachment> getAttachmentsByCandidateId(final Long candidateId) {
        return attachmentRepository.findAllByCandidateId(candidateId);
    }

    @Override
    @Transactional
    public Attachment create(MultipartFile file, Attachment attachmentFromCandidate) {
        Attachment attachment = attachmentRepository.save(createFile(file, attachmentFromCandidate));
        String path = PATH + fileSeparator + attachment.getCandidate().getLastName() + "_" + attachment.getCandidate().getFirstName();
        File existFile = new File(path, attachment.getFileName());
        attachment.setFileName(attachment.getId() + "#" + attachment.getFileName());
        attachment.setPath(path + fileSeparator + attachment.getFileName());
        existFile.renameTo(new File(PATH + fileSeparator + attachment.getCandidate().getLastName() + "_" +
                attachment.getCandidate().getFirstName(), attachment.getFileName()));
        attachmentRepository.saveAndFlush(attachment);
        return attachment;
    }

    @Override
    @Transactional
    public ResponseEntity<ByteArrayResource> downloadById(Long id) {
        String fileName = attachmentRepository.findById(id).get().getFileName();
        String mineType = servletContext.getMimeType(fileName);
        MediaType mediaType = MediaType.parseMediaType(mineType);
        Path path = Paths.get(PATH + fileSeparator + attachmentRepository.findById(id).get().getCandidate().getLastName() + "_" + attachmentRepository.findById(id).get().getCandidate().getFirstName() + fileSeparator + fileName);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                // Content-Type
                .contentType(mediaType) //
                // Content-Lengh
                .contentLength(data.length) //
                .body(resource);
    }

    public Attachment createFile(MultipartFile file, Attachment attachmentFromCandidate) {
        Attachment attachment = new Attachment();
        String name = file.getOriginalFilename();
        attachment.setFileName(name);
        Candidate currentCandidate = attachmentFromCandidate.getCandidate();
        attachment.setCandidate(currentCandidate);
        if (file.getSize() > 1024*1024*20) throw new  RuntimeException("Unfortunately file size is over 20MB");
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File dir = new File(PATH + fileSeparator +
                        attachment.getCandidate().getLastName() + "_" +
                        attachment.getCandidate().getFirstName());
                dir.mkdirs();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(PATH + fileSeparator +
                                attachment.getCandidate().getLastName() + "_" +
                                attachment.getCandidate().getFirstName() + fileSeparator + name)));
                stream.write(bytes);
                stream.close();
                attachment.setPath(PATH + fileSeparator + attachment.getCandidate().getLastName() + "_"
                        + attachment.getCandidate().getFirstName() + fileSeparator + name);
                return attachment;
            } catch (Exception e) {
                //return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            throw new NullPointerException("File is empty");
        }
        throw new RuntimeException("Error creating file");
    }
}
