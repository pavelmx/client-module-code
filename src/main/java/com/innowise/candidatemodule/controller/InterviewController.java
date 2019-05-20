package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.InterviewDTO;
import com.innowise.candidatemodule.controller.mapper.InterviewMapper;
import com.innowise.candidatemodule.service.api.InterviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    private InterviewService interviewService;
    private InterviewMapper interviewMapper;

    @Autowired
    public InterviewController(final InterviewService interviewService,
                               final InterviewMapper interviewMapper) {
        this.interviewService = interviewService;
        this.interviewMapper = interviewMapper;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds interview for given identifier")
    public ResponseEntity<InterviewDTO> getInterview(@PathVariable final Long id) {
        return ResponseEntity.ok(interviewMapper.toDTO(interviewService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found with id " + id))));
    }

    @PutMapping
    @ApiOperation(value = "Save Interview", produces = "application/json")
    public ResponseEntity<InterviewDTO> saveInterview(
            @ApiParam(value = "Json body with the Interview object", required = true)
            @RequestBody final InterviewDTO interviewDTO) {
        return ResponseEntity.ok(interviewMapper.toDTO(
                interviewService.save(interviewMapper.fromDTO(interviewDTO))));
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset Interview", produces = "application/json")
    public void resetInterview(
            @ApiParam(value = "Json body with the Interview object", required = true)
            @RequestBody final InterviewDTO interviewDTO) {
        interviewService.reset(interviewMapper.fromDTO(interviewDTO));
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update Interview", produces = "application/json")
    public ResponseEntity<InterviewDTO> updateInterview(
            @ApiParam(value = "Json body with the Interview object", required = true)
            @RequestBody final InterviewDTO interviewDTO) {
        return ResponseEntity.ok(interviewMapper.toDTO(
                interviewService.update(interviewMapper.fromDTO(interviewDTO))));
    }

    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value = "Finds list of interviews for given candidate identifier")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByCandidateId(@PathVariable final Long candidateId) {
        return ResponseEntity.ok(interviewMapper.toListDTO(interviewService.getInterviewsByCandidateId(candidateId)));
    }

    @GetMapping("/page")
    @ApiOperation(value = "Get page of Interviews")
    public ResponseEntity<List<InterviewDTO>> getInterviewsPage(
            @RequestParam final int page,
            @RequestParam final int size,
            @RequestParam final String sortColumn,
            @RequestParam final String sortDirection) {
        return ResponseEntity.ok(interviewMapper.toListDTO(interviewService.getPage(page, size, sortColumn, sortDirection)));
    }

    @GetMapping("/total")
    @ApiOperation(value = "Get count of Interviews")
    public long getTotal(){
        return interviewService.getAll().size();
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all Interviews")
    public List<InterviewDTO> getAll(){
        return interviewMapper.toListDTO(interviewService.getAll());
    }
}
