package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.CandidateDTO;
import com.innowise.candidatemodule.controller.mapper.CandidateMapper;
import com.innowise.candidatemodule.service.api.CandidateService;
import com.innowise.candidatemodule.service.impl.CandidateServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private CandidateService candidateService;
    private CandidateMapper candidateMapper;
    private CandidateServiceImpl candidateServiceImpl;

    @Autowired
    public CandidateController(final CandidateService candidateService, final CandidateMapper candidateMapper,
                               final CandidateServiceImpl candidateServiceImpl){
        this.candidateService = candidateService;
        this.candidateMapper = candidateMapper;
        this.candidateServiceImpl = candidateServiceImpl;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds candidate for given identifier")
    public ResponseEntity<CandidateDTO> getCandidate(@PathVariable final Long id) {
        return ResponseEntity.ok(candidateMapper.toDTO(candidateService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + id))));
    }

    @PutMapping
    @ApiOperation(value = "Save Candidate", produces = "application/json")
    public ResponseEntity<CandidateDTO> save(
            @ApiParam(value = "Json body with the Candidate object", required = true)
            @RequestBody final CandidateDTO candidateDTO) {
        return ResponseEntity.ok(candidateMapper.toDTO(
                candidateService.save(candidateMapper.fromDTO(candidateDTO))));
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset candidate", produces = "application/json")
    public void reset(
            @ApiParam(value = "Json body with the Candidate object", required = true)
            @RequestBody final CandidateDTO candidateDTO) {
        candidateService.reset(candidateMapper.fromDTO(candidateDTO));
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update candidate", produces = "application/json")
    public ResponseEntity<CandidateDTO> update(
            @ApiParam(value = "Json body with the Candidate object", required = true)
            @RequestBody final CandidateDTO candidateDTO) {
        return ResponseEntity.ok(candidateMapper.toDTO(
                candidateService.update(candidateMapper.fromDTO(candidateDTO))));
    }

    @PutMapping("/fill")
    @ApiOperation("Fill database")
    public void fill() {
        candidateServiceImpl.fillDatabase();
    }

    @GetMapping("/page")
    @ApiOperation(value = "Get page of Candidates")
    public ResponseEntity<List<CandidateDTO>> getEmployeesPage(
            @RequestParam final int page,
            @RequestParam final int size,
            @RequestParam final String sortColumn,
            @RequestParam final String sortDirection) {
        return ResponseEntity.ok(candidateMapper.toListDTO(candidateService.getPage(page, size, sortColumn, sortDirection)));
    }

    @GetMapping("/total")
    @ApiOperation(value = "Get count of Candidates")
    public long getTotal(){
        return candidateService.getAll().size();
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all Departments")
    public List<CandidateDTO> getAll(){
        return candidateMapper.toListDTO(candidateService.getAll());
    }
}
