package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "interview_employee")
public class InterviewEmployee extends StoredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_interview_employee")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;
    @ManyToOne(fetch = FetchType.EAGER)
    private Interview interview;
    private String feedback;

}
