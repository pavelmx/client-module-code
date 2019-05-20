package com.innowise.candidatemodule.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewDTO {

    private String id;
    private CandidateDTO candidate;
    private LocalDateTime interviewDateTime;
    private String status;
}
