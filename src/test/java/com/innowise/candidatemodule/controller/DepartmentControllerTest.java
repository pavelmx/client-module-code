package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.DepartmentDTO;
import com.innowise.candidatemodule.controller.mapper.DepartmentMapper;
import com.innowise.candidatemodule.domain.Department;
import com.innowise.candidatemodule.repository.DepartmentRepository;
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
public class DepartmentControllerTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private Department department;

    @Before
    public void setUp() {
        buildDepartment();
    }

    @After
    public void tearDown() {
        departmentRepository.deleteAll();
    }

    @Test
    public void getDepartment() {
        ResponseEntity<DepartmentDTO> response = doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Long id = departmentMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<DepartmentDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, departmentMapper.fromDTO(response1.getBody()).getId());
    }

    @Test
    public void save() {
        ResponseEntity<DepartmentDTO> response = doPutRequest(restTemplate, departmentMapper.toDTO(department));
        ResponseEntity<DepartmentDTO> response1 = doGetRequest(restTemplate, departmentMapper.fromDTO(response.getBody()).getId());
        Department responseDepartment = departmentMapper.fromDTO(response1.getBody());
        Assert.assertEquals(departmentMapper.fromDTO(response.getBody()).getId(), responseDepartment.getId());
    }

    @Test
    public void reset() {
        ResponseEntity<DepartmentDTO> response = doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Long depId = departmentMapper.fromDTO(response.getBody()).getId();
        buildDepartment();
        doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
        doPutResetRequest(restTemplate, doGetRequest(restTemplate,depId).getBody());
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
    }

    @Test
    public void update() {
        ResponseEntity<DepartmentDTO> response = doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Long id = departmentMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<DepartmentDTO> response1 = doGetRequest(restTemplate, id);
        Department responseDepartment = departmentMapper.fromDTO(response1.getBody());
        responseDepartment.setDepartmentName(".NET");
        ResponseEntity<DepartmentDTO> response2 = doPutUpdateRequest(restTemplate,departmentMapper.toDTO(responseDepartment));
        Assert.assertEquals(".NET", response2.getBody().getDepartmentName());
    }

    @Test
    public void getAll() {
        ResponseEntity<DepartmentDTO> response = doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
        buildDepartment();
        doPutRequest(restTemplate, departmentMapper.toDTO(department));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
    }
    private <T> ResponseEntity<DepartmentDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                           final DepartmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/department", HttpMethod.PUT,
                new HttpEntity<DepartmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<DepartmentDTO>() {
                });
    }
    private <T> ResponseEntity<DepartmentDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                                 final DepartmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/department/update", HttpMethod.PUT,
                new HttpEntity<DepartmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<DepartmentDTO>() {
                });
    }

    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                   final DepartmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/department/reset", HttpMethod.PUT,
                new HttpEntity<DepartmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<DepartmentDTO>() {
                });
    }
    private ResponseEntity<DepartmentDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/department/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<DepartmentDTO>() {
                }, id);
    }

    private ResponseEntity<List<DepartmentDTO>> doGetAllRequest(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/department/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<DepartmentDTO>>() {
                });
    }

    private void assertStatusOk(final ResponseEntity<DepartmentDTO> departmentDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, departmentDTOResponseEntity.getStatusCode());
    }

    private void buildDepartment() {
        department = Department.builder().departmentName("Java").build();
    }
}
