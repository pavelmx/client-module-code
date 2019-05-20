package com.innowise.candidatemodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.candidatemodule.controller.dto.AttachmentDTO;
import com.innowise.candidatemodule.controller.mapper.AttachmentMapper;
import com.innowise.candidatemodule.controller.mapper.CandidateMapper;
import com.innowise.candidatemodule.domain.Attachment;
import com.innowise.candidatemodule.service.api.AttachmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    private AttachmentService attachmentService;

    private AttachmentMapper attachmentMapper;

    private CandidateMapper candidateMapper;

    @Autowired
    public AttachmentController(final AttachmentService attachmentService,
                                final AttachmentMapper attachmentMapper,
                                final CandidateMapper candidateMapper) {
        this.attachmentService = attachmentService;
        this.attachmentMapper = attachmentMapper;
        this.candidateMapper = candidateMapper;
    }

    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value = "Finds list of attachments for given candidate identifier")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByCandidateId(@PathVariable final Long candidateId) {
        return ResponseEntity.ok(attachmentMapper.toListDTO(attachmentService.getAttachmentsByCandidateId(candidateId)));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds attachment for given identifier")
    public ResponseEntity<AttachmentDTO> getAttachmentById(@PathVariable final Long id) {
        return ResponseEntity.ok(attachmentMapper.toDTO(attachmentService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id " + id))));
    }

    @PutMapping
    @ApiOperation(value = "Save Attachment", produces = "application/json")
    public ResponseEntity<AttachmentDTO> save(
            @ApiParam(value = "Json body with the Attachment object", required = true)
            @RequestBody final AttachmentDTO attachmentDTO) {
        return ResponseEntity.ok(attachmentMapper.toDTO(
                attachmentService.save(attachmentMapper.fromDTO(attachmentDTO))));
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset Attachment", produces = "application/json")
    public void reset(
            @ApiParam(value = "Json body with the Attachment object", required = true)
            @RequestBody final AttachmentDTO attachmentDTO) {
        attachmentService.reset(attachmentMapper.fromDTO(attachmentDTO));
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update Attachment", produces = "application/json")
    public ResponseEntity<AttachmentDTO> update(
            @ApiParam(value = "Json body with the Attachment object", required = true)
            @RequestBody final AttachmentDTO attachmentDTO) {
        return ResponseEntity.ok(attachmentMapper.toDTO(
                attachmentService.update(attachmentMapper.fromDTO(attachmentDTO))));
    }

    @PostMapping("/create")
    public ResponseEntity<AttachmentDTO> addAttachment(@RequestParam("attachment") String attachment,
                                                       @RequestParam("uploadFile") MultipartFile file) {
        Attachment attachment1 = new Attachment();
        attachment1.setFileName(file.getOriginalFilename());
        try {
            AttachmentDTO attachment2 = new ObjectMapper().readValue(attachment, AttachmentDTO.class);
            attachment1.setCandidate(candidateMapper.fromDTO(attachment2.getCandidate()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(attachmentMapper.toDTO(attachmentService.create(file, attachment1)));
    }

    @GetMapping("/download/{id}")
    ResponseEntity<ByteArrayResource> downloadAttachmentById(@PathVariable Long id) {
        return attachmentService.downloadById(id);
    }
}
