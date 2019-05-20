package com.innowise.candidatemodule.controller;

import com.innowise.candidatemodule.controller.dto.AttachmentDTO;
import com.innowise.candidatemodule.controller.mapper.AttachmentMapper;
import com.innowise.candidatemodule.domain.Attachment;
import com.innowise.candidatemodule.domain.Candidate;
import com.innowise.candidatemodule.repository.AttachmentRepository;
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
public class AttachmentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    private Attachment attachment;

    private Candidate candidate;


    @Before
    public void setUp() {
        buildAttachment();
    }

    @After
    public void tearDown() {
        attachmentRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    @Test
    public void testGetAttachmentById() {
        ResponseEntity<AttachmentDTO> response = doPutRequest(restTemplate, attachmentMapper.toDTO(attachment));
        Long id = attachmentMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<AttachmentDTO> response1 = doGetRequest(restTemplate, id);
        assertStatusOk(response1);
        Assert.assertEquals(id, attachmentMapper.fromDTO(response1.getBody()).getId());
    }


    @Test
    public void getAttachmentsByCandidateId() {
        ResponseEntity<AttachmentDTO> response = doPutRequest(restTemplate, attachmentMapper.toDTO(attachment));
        Long id = attachmentMapper.fromDTO(response.getBody()).getId();
        Long candidateId = attachmentMapper.fromDTO(response.getBody()).getCandidate().getId();
        ResponseEntity<List<AttachmentDTO>> response1 = doGetByCandidateIdRequest(restTemplate, attachmentMapper.fromDTO(response.getBody()).getCandidate().getId());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(id,  attachmentMapper.fromDTO((response1.getBody().stream().filter(attachmentDTO -> Double.parseDouble(attachmentDTO.getCandidate().getId())==candidateId).findFirst().get())).getId());
    }

    @Test
    public void save() {
        ResponseEntity<AttachmentDTO> response = doPutRequest(restTemplate, attachmentMapper.toDTO(attachment));
        ResponseEntity<AttachmentDTO> response1 = doGetRequest(restTemplate, attachmentMapper.fromDTO(response.getBody()).getId());
        Attachment responseAttachment = attachmentMapper.fromDTO(response1.getBody());
        Assert.assertEquals(attachmentMapper.fromDTO(response.getBody()).getId(), responseAttachment.getId());
    }

    @Test
    public void reset() {
        ResponseEntity<AttachmentDTO> response = doPutRequest(restTemplate, attachmentMapper.toDTO(attachment));
        Long candidateId = attachmentMapper.fromDTO(response.getBody()).getCandidate().getId();
        Assert.assertEquals(1, doGetByCandidateIdRequest(restTemplate, candidateId).getBody().size());
        doPutResetRequest(restTemplate, response.getBody());
        Assert.assertEquals(0, doGetByCandidateIdRequest(restTemplate, candidateId).getBody().size());
    }

    @Test
    public void update() {
        ResponseEntity<AttachmentDTO> response = doPutRequest(restTemplate, attachmentMapper.toDTO(attachment));
        Long id = attachmentMapper.fromDTO(response.getBody()).getId();
        ResponseEntity<AttachmentDTO> response1 = doGetRequest(restTemplate, id);
        Attachment responseAttachment = attachmentMapper.fromDTO(response1.getBody());
        responseAttachment.setPath("zzzz");
        ResponseEntity<AttachmentDTO> response2 = doPutUpdateRequest(restTemplate,attachmentMapper.toDTO(responseAttachment));
        Assert.assertEquals("zzzz", response2.getBody().getPath());
    }

    private <T> ResponseEntity<AttachmentDTO> doPutRequest(final TestRestTemplate restTemplate,
                                                           final AttachmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/attachment", HttpMethod.PUT,
                new HttpEntity<AttachmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<AttachmentDTO>() {
                });
    }
    private <T> ResponseEntity<AttachmentDTO> doPutUpdateRequest(final TestRestTemplate restTemplate,
                                                                 final AttachmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        return restTemplate.exchange("/api/v1/attachment/update", HttpMethod.PUT,
                new HttpEntity<AttachmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<AttachmentDTO>() {
                });
    }
    private void doPutResetRequest(final TestRestTemplate restTemplate,
                                                           final AttachmentDTO body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json");
        restTemplate.exchange("/api/v1/attachment/reset", HttpMethod.PUT,
                new HttpEntity<AttachmentDTO>(body, httpHeaders),
                new ParameterizedTypeReference<AttachmentDTO>() {
                });
    }

    private ResponseEntity<AttachmentDTO> doGetRequest(final TestRestTemplate restTemplate, final Long id) {
        return restTemplate.exchange("/api/v1/attachment/{id}",
                HttpMethod.GET, null, new ParameterizedTypeReference<AttachmentDTO>() {
                }, id);
    }
    private ResponseEntity<List<AttachmentDTO>> doGetByCandidateIdRequest(final TestRestTemplate restTemplate, final Long candidateId) {
        return restTemplate.exchange("/api/v1/attachment/candidate/{candidateId}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<AttachmentDTO>>() {
                }, candidateId);
    }

    private void assertStatusOk(final ResponseEntity<AttachmentDTO> attachmentDTOResponseEntity) {
        Assert.assertEquals(HttpStatus.OK, attachmentDTOResponseEntity.getStatusCode());
    }

    private void buildAttachment() {
        candidate = Candidate.builder().firstName("Aleksei").lastName("Petrou").mail("abc@mail.ru").skype("1111").description("aaa").telephone("3752912345550").build();
        candidateRepository.save(candidate);
        attachment = Attachment.builder().fileName("abc").path("home/aaa/abc").candidate(candidate).build();
    }
}
