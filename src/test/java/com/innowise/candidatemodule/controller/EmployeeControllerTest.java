package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.EmployeeDTO;
import com.innowise.candidatemodule.controller.mapper.DepartmentMapper;
import com.innowise.candidatemodule.controller.mapper.EmployeeMapper;
import com.innowise.candidatemodule.domain.Department;
import com.innowise.candidatemodule.domain.Employee;
import com.innowise.candidatemodule.repository.DepartmentRepository;
import com.innowise.candidatemodule.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    private Department department;

    private Employee employee;

    @Before
    public void setUp() {

        buildEmployee();
    }

    @After
    public void tearDown() {

        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void getEmployee() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Long id = employeeMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<EmployeeDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, employeeMapper.fromDTO(response1.getBody()).getId());
    }

    @Test
    public void getEmployeeByDepartmentId() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Long id = employeeMapper.fromDTO(response.getBody()).getId();
        Long departmentId = employeeMapper.fromDTO(response.getBody()).getDepartment().getId();
        ResponseEntity<List<EmployeeDTO>> response1 = doGetByDepartmentIdRequest(restTemplate, employeeMapper.fromDTO(response.getBody()).getDepartment().getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(id,  employeeMapper.fromDTO((response1.getBody().stream().filter(employeeDTO -> Double.parseDouble(employeeDTO.getDepartment().getId())==departmentId).findFirst().get())).getId());
    }

    @Test
    public void getTotal() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        buildEmployee();
        doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Assert.assertEquals(2L, doGetTotal(restTemplate).getBody().longValue());
    }

    @Test
    public void save() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        ResponseEntity<EmployeeDTO> response1 = doGetRequest(restTemplate, employeeMapper.fromDTO(response.getBody()).getId());
        Employee responseEmployee = employeeMapper.fromDTO(response1.getBody());
        Assert.assertEquals(employeeMapper.fromDTO(response.getBody()).getId(), responseEmployee.getId());
    }

    @Test
    public void reset() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        doPutResetRequest(restTemplate, response.getBody());
        ResponseEntity<EmployeeDTO> response1 = doGetRequest(restTemplate, employeeMapper.fromDTO(response.getBody()).getId());
        Employee responseEmployee = employeeMapper.fromDTO(response1.getBody());
        Assert.assertTrue(responseEmployee.isDeleted());
    }

    @Test
    public void update() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Long id = employeeMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<EmployeeDTO> response1 = doGetRequest(restTemplate, id);
        Employee responseEmployee = employeeMapper.fromDTO(response1.getBody());
        responseEmployee.setFirstName("Ilya");
        ResponseEntity<EmployeeDTO> response2 = doPutUpdateRequest(restTemplate,employeeMapper.toDTO(responseEmployee));
        Assert.assertEquals("Ilya", response2.getBody().getFirstName());
    }

    @Test
    public void getAll() {
        ResponseEntity<EmployeeDTO> response = doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
        buildEmployee();
        doPutRequest(restTemplate, employeeMapper.toDTO(employee));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
    }

    private <T> ResponseEntity<EmployeeDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                         final EmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/employee", HttpMethod.PUT,
                new HttpEntity<EmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<EmployeeDTO>() {
                });
    }
    private <T> ResponseEntity<EmployeeDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                               final EmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/employee/update", HttpMethod.PUT,
                new HttpEntity<EmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<EmployeeDTO>() {
                });
    }
    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                   final EmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/employee/reset", HttpMethod.PUT,
                new HttpEntity<EmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<EmployeeDTO>() {
                });
    }

    private ResponseEntity<EmployeeDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/employee/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<EmployeeDTO>() {
                }, id);
    }

    private ResponseEntity<List<EmployeeDTO>> doGetAllRequest(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/employee/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<EmployeeDTO>>() {
                });
    }

    private ResponseEntity<List<EmployeeDTO>> doGetByDepartmentIdRequest(final TestRestTemplate restTemplate, final Long departmentId) {
        return restTemplate.exchange("/api/v1/employee/department/{departmentId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<EmployeeDTO>>() {
                }, departmentId);
    }

    private ResponseEntity<Long> doGetTotal(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/employee/total",
                HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {
                });
    }

    private void assertStatusOk(final ResponseEntity<EmployeeDTO> employeeDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, employeeDTOResponseEntity.getStatusCode());
    }

    private void buildEmployee() {
        department = Department.builder().departmentName("Java").build();
        departmentRepository.save(department);
        employee = Employee.builder().firstName("Aleksandr").lastName("Petrou").department(department).build();
    }
}