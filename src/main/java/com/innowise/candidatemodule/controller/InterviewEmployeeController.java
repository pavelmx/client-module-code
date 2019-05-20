package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.InterviewEmployeeDTO;
import com.innowise.candidatemodule.controller.mapper.InterviewEmployeeMapper;
import com.innowise.candidatemodule.service.api.InterviewEmployeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class InterviewEmployeeController {

    private InterviewEmployeeService interviewEmployeeService;
    private InterviewEmployeeMapper interviewEmployeeMapper;

    @Autowired
    public InterviewEmployeeController(final InterviewEmployeeService interviewEmployeeService,
                                       final InterviewEmployeeMapper interviewEmployeeMapper) {
        this.interviewEmployeeService = interviewEmployeeService;
        this.interviewEmployeeMapper = interviewEmployeeMapper;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds interviewEmployee for given identifier")
    public ResponseEntity<InterviewEmployeeDTO> getById(@PathVariable final Long id) {
        return ResponseEntity.ok(interviewEmployeeMapper.toDTO(interviewEmployeeService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found with id " + id))));
    }

    @PutMapping
    @ApiOperation(value = "Save InterviewEmployee", produces = "application/json")
    public ResponseEntity<InterviewEmployeeDTO> save(
            @ApiParam(value = "Json body with the InterviewEmployee object", required = true)
            @RequestBody final InterviewEmployeeDTO interviewEmployeeDTO) {
        return ResponseEntity.ok(interviewEmployeeMapper.toDTO(
                interviewEmployeeService.save(interviewEmployeeMapper.fromDTO(interviewEmployeeDTO))));
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset InterviewEmployee", produces = "application/json")
    public void reset(
            @ApiParam(value = "Json body with the InterviewEmployee object", required = true)
            @RequestBody final InterviewEmployeeDTO interviewEmployeeDTO) {
        interviewEmployeeService.reset(interviewEmployeeMapper.fromDTO(interviewEmployeeDTO));
    }

    @PutMapping(value = "/update", produces = "application/json")
    @ApiOperation("Update InterviewEmployee")
    public ResponseEntity<InterviewEmployeeDTO> update(
            @ApiParam(value = "Json body with the InterviewEmployee object", required = true)
            @RequestBody final InterviewEmployeeDTO interviewEmployeeDTO) {
        return ResponseEntity.ok(interviewEmployeeMapper.toDTO(
                interviewEmployeeService.update(interviewEmployeeMapper.fromDTO(interviewEmployeeDTO))));
    }

    @GetMapping("/interview/{interviewId}")
    @ApiOperation(value = "Finds list of  InterviewEmployees for given interview identifier")
    public ResponseEntity<List<InterviewEmployeeDTO>> getAllByInterviewId(@PathVariable final Long interviewId) {
        return ResponseEntity.ok(interviewEmployeeMapper.toListDTO(interviewEmployeeService.getAllByInterviewId(interviewId)));
    }

    @GetMapping("/employee/{employeeId}")
    @ApiOperation(value = "Finds list of InterviewEmployees for given employee identifier")
    public ResponseEntity<List<InterviewEmployeeDTO>> getAllByEmployeeId(@PathVariable final Long employeeId) {
        return ResponseEntity.ok(interviewEmployeeMapper.toListDTO(interviewEmployeeService.getAllByEmployeeId(employeeId)));
    }
    @GetMapping("/all")
    @ApiOperation(value = "Get all Interviews")
    public List<InterviewEmployeeDTO> getAll(){
        return interviewEmployeeMapper.toListDTO(interviewEmployeeService.getAll());
    }
}
