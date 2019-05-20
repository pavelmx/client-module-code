package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.InterviewEmployeeDTO;
import com.innowise.candidatemodule.controller.mapper.EmployeeMapper;
import com.innowise.candidatemodule.controller.mapper.InterviewEmployeeMapper;
import com.innowise.candidatemodule.controller.mapper.InterviewMapper;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.domain.Department;
import com.innowise.candidatemodule.domain.Employee;
import com.innowise.candidatemodule.domain.Interview;
import com.innowise.candidatemodule.domain.InterviewEmployee;
import com.innowise.candidatemodule.repository.CandidateRepository;
import com.innowise.candidatemodule.repository.DepartmentRepository;
import com.innowise.candidatemodule.repository.EmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewEmployeeRepository;
import com.innowise.candidatemodule.repository.InterviewRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class InterviewEmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewMapper interviewMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InterviewEmployeeRepository interviewEmployeeRepository;

    @Autowired
    private InterviewEmployeeMapper interviewEmployeeMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private Interview interview;
    private Employee employee;
    private InterviewEmployee interviewEmployee;

    @Before
    public void setUp() {
        buildEmployee();
        buildInterview();
        buildInterviewEmployee();
    }

    @After
    public void tearDown() {
        interviewEmployeeRepository.deleteAll();
        interviewRepository.deleteAll();
        employeeRepository.deleteAll();
        candidateRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    public void getById() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Long id = interviewEmployeeMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<InterviewEmployeeDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, interviewEmployeeMapper.fromDTO(response1.getBody()).getId());
    }

    @Test
    public void save() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        ResponseEntity<InterviewEmployeeDTO> response1 = doGetRequest(restTemplate, interviewEmployeeMapper.fromDTO(response.getBody()).getId());
        InterviewEmployee responseFeedback = interviewEmployeeMapper.fromDTO(response1.getBody());
        Assert.assertEquals(interviewEmployeeMapper.fromDTO(response.getBody()).getId(), responseFeedback.getId());
    }

    @Test
    public void reset() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Long id = interviewEmployeeMapper.fromDTO(response.getBody()).getId();
        buildInterviewEmployee();
        doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
        doPutResetRequest(restTemplate, doGetRequest(restTemplate,id).getBody());
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
    }

    @Test
    public void update() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Long id = interviewEmployeeMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<InterviewEmployeeDTO> response1 = doGetRequest(restTemplate, id);
        InterviewEmployee responseInterviewEmployee = interviewEmployeeMapper.fromDTO(response1.getBody());
        responseInterviewEmployee.setFeedback("bbb");
        ResponseEntity<InterviewEmployeeDTO> response2 = doPutUpdateRequest(restTemplate,interviewEmployeeMapper.toDTO(responseInterviewEmployee));
        Assert.assertEquals("bbb", response2.getBody().getFeedback());
    }

    @Test
    public void getAllByInterviewId() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Long id = interviewEmployeeMapper.fromDTO(response.getBody()).getId();
        Long interviewId = interviewEmployeeMapper.fromDTO(response.getBody()).getInterview().getId();
        ResponseEntity<List<InterviewEmployeeDTO>> response1 = doGetByInterviewIdRequest(restTemplate, interviewEmployeeMapper.fromDTO(response.getBody()).getInterview().getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(id,  interviewEmployeeMapper.fromDTO((response1.getBody().stream().filter(interviewEmployeeDTO -> Double.parseDouble(interviewEmployeeDTO.getInterview().getId())==interviewId).findFirst().get())).getId());
    }

    @Test
    public void getAllByEmployeeId() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Long id = interviewEmployeeMapper.fromDTO(response.getBody()).getId();
        Long employeeId = interviewEmployeeMapper.fromDTO(response.getBody()).getEmployee().getId();
        ResponseEntity<List<InterviewEmployeeDTO>> response1 = doGetByEmployeeIdRequest(restTemplate, interviewEmployeeMapper.fromDTO(response.getBody()).getEmployee().getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(id,  interviewEmployeeMapper.fromDTO((response1.getBody().stream().filter(interviewEmployeeDTO -> Double.parseDouble(interviewEmployeeDTO.getEmployee().getId())==employeeId).findFirst().get())).getId());
    }

    @Test
    public void getAll() {
        ResponseEntity<InterviewEmployeeDTO> response = doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
        buildInterviewEmployee();
        doPutRequest(restTemplate, interviewEmployeeMapper.toDTO(interviewEmployee));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
    }
    private <T> ResponseEntity<InterviewEmployeeDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                                  final InterviewEmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/feedback", HttpMethod.PUT,
                new HttpEntity<InterviewEmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewEmployeeDTO>() {
                });
    }
    private <T> ResponseEntity<InterviewEmployeeDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                                        final InterviewEmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/feedback/update", HttpMethod.PUT,
                new HttpEntity<InterviewEmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewEmployeeDTO>() {
                });
    }

    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                   final InterviewEmployeeDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/feedback/reset", HttpMethod.PUT,
                new HttpEntity<InterviewEmployeeDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewEmployeeDTO>() {
                });
    }

    private ResponseEntity<InterviewEmployeeDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/feedback/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<InterviewEmployeeDTO>() {
                }, id);
    }

    private ResponseEntity<List<InterviewEmployeeDTO>> doGetAllRequest(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/feedback/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InterviewEmployeeDTO>>() {
                });
    }

    private ResponseEntity<List<InterviewEmployeeDTO>> doGetByInterviewIdRequest(final TestRestTemplate restTemplate, final Long interviewId) {
        return restTemplate.exchange("/api/v1/feedback/interview/{interviewId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InterviewEmployeeDTO>>() {
                }, interviewId);
    }

    private ResponseEntity<List<InterviewEmployeeDTO>> doGetByEmployeeIdRequest(final TestRestTemplate restTemplate, final Long employeeId) {
        return restTemplate.exchange("/api/v1/feedback/employee/{employeeId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InterviewEmployeeDTO>>() {
                }, employeeId);
    }


    private void assertStatusOk(final ResponseEntity<InterviewEmployeeDTO> interviewEmployeeDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, interviewEmployeeDTOResponseEntity.getStatusCode());
    }

    private void buildEmployee() {
        Department department = Department.builder().departmentName("Java").build();
        departmentRepository.save(department);
        employee = Employee.builder().firstName("Aleksandr").lastName("Petrou").department(department).build();
        employeeRepository.save(employee);
    }

    private void buildInterview() {
        Candidate candidate = Candidate.builder().firstName("Aleksei").lastName("Petrou").mail("abc@mail.ru").skype("1111").description("aaa").telephone("3752912345550").build();
        candidateRepository.save(candidate);
        interview = Interview.builder().candidate(candidate).interviewDate(LocalDateTime.of(2019,4,15,15,0,0)).status("aaa").build();
        interviewRepository.save(interview);
    }

    private void buildInterviewEmployee() {
        interviewEmployee = InterviewEmployee.builder().interview(interview).employee(employee).feedback("ccc").build();
    }
}