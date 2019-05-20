package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.EmployeeDTO;
import com.innowise.candidatemodule.controller.mapper.EmployeeMapper;
import com.innowise.candidatemodule.service.api.EmployeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeController(final EmployeeService employeeService,
                              final EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds employee for given identifier")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable final Long id) {
        return ResponseEntity.ok(employeeMapper.toDTO(employeeService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id))));
    }

    @GetMapping("/department/{departmentId}")
    @ApiOperation(value = "Finds list of employees for given department identifier")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeByDepartmentId(@PathVariable final Long departmentId) {
        return ResponseEntity.ok(employeeMapper.toListDTO(employeeService.getEmployeesByDepartmentId(departmentId)));
    }

    @GetMapping("/page")
    @ApiOperation(value = "Get page of Employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesPage(
            @RequestParam final int page,
            @RequestParam final int size,
            @RequestParam final String sortColumn,
            @RequestParam final String sortDirection) {
        return ResponseEntity.ok(employeeMapper.toListDTO(employeeService.getPage(page, size, sortColumn, sortDirection)));
    }

    @GetMapping("/total")
    @ApiOperation(value = "Get count of Employees")
    public long getTotal(){
        return employeeService.getAll().size();
    }

    @GetMapping("/all")
    @ApiOperation(value = "Get all Employees")
    public List<EmployeeDTO> getAll(){
        return employeeMapper.toListDTO(employeeService.getAll());
    }

    @PutMapping
    @ApiOperation(value = "Save Employee", produces = "application/json")
    public ResponseEntity<EmployeeDTO> save(
            @ApiParam(value = "Json body with the Employee object", required = true)
            @RequestBody final EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeMapper.toDTO(
                employeeService.save(employeeMapper.fromDTO(employeeDTO))));
    }

    @PutMapping("/reset")
    @ApiOperation(value = "Reset Employee", produces = "application/json")
    public void reset(
            @ApiParam(value = "Json body with the Employee object", required = true)
            @RequestBody final EmployeeDTO employeeDTO) {
        employeeService.reset(employeeMapper.fromDTO(employeeDTO));
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update Employee", produces = "application/json")
    public ResponseEntity<EmployeeDTO> update(
            @ApiParam(value = "Json body with the Employee object", required = true)
            @RequestBody final EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeMapper.toDTO(
                employeeService.update(employeeMapper.fromDTO(employeeDTO))));
    }

}
