package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.CandidateDTO;
import com.innowise.candidatemodule.controller.mapper.CandidateMapper;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.repository.CandidateRepository;
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
public class CandidateControllerTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private Candidate candidate;

    @Before
    public void setUp() {
        buildCandidate();
    }

    @After
    public void tearDown() {
        candidateRepository.deleteAll();
    }



    @Test
    public void getCandidate() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Long id = candidateMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<CandidateDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, candidateMapper.fromDTO(response1.getBody()).getId());
    }

    @Test
    public void save() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        ResponseEntity<CandidateDTO> response1 = doGetRequest(restTemplate, candidateMapper.fromDTO(response.getBody()).getId());
        Candidate responseCandidate = candidateMapper.fromDTO(response1.getBody());
        Assert.assertEquals(candidateMapper.fromDTO(response.getBody()).getId(), responseCandidate.getId());
    }

    @Test
    public void reset() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Long candidateId = candidateMapper.fromDTO(response.getBody()).getId();
        buildCandidate();
        doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());
        doPutResetRequest(restTemplate, doGetRequest(restTemplate,candidateId).getBody());
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
    }

    @Test
    public void update() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Long id = candidateMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<CandidateDTO> response1 = doGetRequest(restTemplate, id);
        Candidate responseCandidate = candidateMapper.fromDTO(response1.getBody());
        responseCandidate.setFirstName("Petr");
        ResponseEntity<CandidateDTO> response2 = doPutUpdateRequest(restTemplate,candidateMapper.toDTO(responseCandidate));
        Assert.assertEquals("Petr", response2.getBody().getFirstName());
    }

    @Test
    public void getTotal() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        buildCandidate();
        doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Assert.assertEquals(2L, doGetTotal(restTemplate).getBody().longValue());
    }

    @Test
    public void getAll() {
        ResponseEntity<CandidateDTO> response = doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Assert.assertEquals(1, doGetAllRequest(restTemplate).getBody().size());
        buildCandidate();
        doPutRequest(restTemplate, candidateMapper.toDTO(candidate));
        Assert.assertEquals(2, doGetAllRequest(restTemplate).getBody().size());

    }

    private <T> ResponseEntity<CandidateDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                          final CandidateDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/candidate", HttpMethod.PUT,
                new HttpEntity<CandidateDTO>(body, httpHeaders),
                new ParameterizedTypeReference<CandidateDTO>() {
                });
    }
    private <T> ResponseEntity<CandidateDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                                final CandidateDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/candidate/update", HttpMethod.PUT,
                new HttpEntity<CandidateDTO>(body, httpHeaders),
                new ParameterizedTypeReference<CandidateDTO>() {
                });
    }
    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                   final CandidateDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/candidate/reset", HttpMethod.PUT,
                new HttpEntity<CandidateDTO>(body, httpHeaders),
                new ParameterizedTypeReference<CandidateDTO>() {
                });
    }

    private ResponseEntity<CandidateDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/candidate/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<CandidateDTO>() {
                }, id);
    }

    private ResponseEntity<List<CandidateDTO>> doGetAllRequest(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/candidate/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<CandidateDTO>>() {
                });
    }

    private ResponseEntity<Long> doGetTotal(final TestRestTemplate restTemplate) {
        return restTemplate.exchange("/api/v1/candidate/total",
                HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {
                });
    }


    private void assertStatusOk(final ResponseEntity<CandidateDTO> candidateDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, candidateDTOResponseEntity.getStatusCode());
    }

    private void buildCandidate() {
        candidate = Candidate.builder().firstName("Aleksei").lastName("Petrou").mail("abc@mail.ru").skype("1111").description("aaa").telephone("3752912345550").build();
    }
}