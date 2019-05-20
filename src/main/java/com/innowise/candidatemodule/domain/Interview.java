package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "interview")
public class Interview extends StoredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_interview")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Candidate candidate;
    private LocalDateTime interviewDate;
    private String status;

}
