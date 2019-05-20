package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.InterviewDTO;
import com.innowise.candidatemodule.controller.mapper.CandidateMapper;
import com.innowise.candidatemodule.controller.mapper.InterviewMapper;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.domain.Interview;
import com.innowise.candidatemodule.repository.CandidateRepository;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class InterviewControllerTest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewMapper interviewMapper;
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private Candidate candidate;
    private Interview interview;

    @Before
    public void setUp() {
        buildInterview();
    }

    @After
    public void tearDown() {
        interviewRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    @Test
    public void getInterview() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Long id = interviewMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<InterviewDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, interviewMapper.fromDTO(response1.getBody()).getId());
    }

    @Test
    public void saveInterview() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        ResponseEntity<InterviewDTO> response1 = doGetRequest(restTemplate, interviewMapper.fromDTO(response.getBody()).getId());
        Interview responseInterview = interviewMapper.fromDTO(response1.getBody());
        Assert.assertEquals(interviewMapper.fromDTO(response.getBody()).getId(), responseInterview.getId());
    }

    @Test
    public void resetInterview() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Long interviewId = interviewMapper.fromDTO(response.getBody()).getId();
        buildInterview();
        doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
        doPutResetRequest(restTemplate, doGetRequest(restTemplate,interviewId).getBody());
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
    }

    @Test
    public void updateInterview() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Long id = interviewMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<InterviewDTO> response1 = doGetRequest(restTemplate, id);
        Interview responseInterview= interviewMapper.fromDTO(response1.getBody());
        responseInterview.setStatus("bbb");
        ResponseEntity<InterviewDTO> response2 = doPutUpdateRequest(restTemplate,interviewMapper.toDTO(responseInterview));
        Assert.assertEquals("bbb", response2.getBody().getStatus());
    }

    @Test
    public void getInterviewsByCandidateId() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Long id = interviewMapper.fromDTO(response.getBody()).getId();
        Long candidateId = interviewMapper.fromDTO(response.getBody()).getCandidate().getId();
        ResponseEntity<List<InterviewDTO>> response1 = doGetByCandidateIdRequest(restTemplate, interviewMapper.fromDTO(response.getBody()).getCandidate().getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(id,  interviewMapper.fromDTO((response1.getBody().stream().filter(interviewDTO -> Double.parseDouble(interviewDTO.getCandidate().getId())==candidateId).findFirst().get())).getId());
    }

    @Test
    public void getTotal() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        buildInterview();
        doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Assert.assertEquals(2L, doGetTotal(restTemplate).getBody().longValue());
    }

    @Test
    public void getAll() {
        ResponseEntity<InterviewDTO> response = doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
        buildInterview();
        doPutRequest(restTemplate, interviewMapper.toDTO(interview));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
    }

    private <T> ResponseEntity<InterviewDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                          final InterviewDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/interview", HttpMethod.PUT,
                new HttpEntity<InterviewDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewDTO>() {
                });
    }
    private <T> ResponseEntity<InterviewDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                                final InterviewDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/interview/update", HttpMethod.PUT,
                new HttpEntity<InterviewDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewDTO>() {
                });
    }
    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                   final InterviewDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/interview/reset", HttpMethod.PUT,
                new HttpEntity<InterviewDTO>(body, httpHeaders),
                new ParameterizedTypeReference<InterviewDTO>() {
                });
    }

    private ResponseEntity<InterviewDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/interview/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<InterviewDTO>() {
                }, id);
    }

    private ResponseEntity<List<InterviewDTO>> doGetAllRequest(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/interview/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InterviewDTO>>() {
                });
    }

    private ResponseEntity<List<InterviewDTO>> doGetByCandidateIdRequest(final TestRestTemplate restTemplate, final Long candidateId) {
        return restTemplate.exchange("/api/v1/interview/candidate/{candidateId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<InterviewDTO>>() {
                }, candidateId);
    }

    private ResponseEntity<Long> doGetTotal(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/interview/total",
                HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {
                });
    }

    private void assertStatusOk(final ResponseEntity<InterviewDTO> interviewDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, interviewDTOResponseEntity.getStatusCode());
    }

    private void buildInterview() {
        candidate = Candidate.builder().firstName("Aleksei").lastName("Petrou").mail("abc@mail.ru").skype("1111").description("aaa").telephone("3752912345550").build();
        candidateRepository.save(candidate);
        interview = Interview.builder().candidate(candidate).interviewDate(LocalDateTime.of(2019,4,15,15,0,0)).status("aaa").build();
    }
}