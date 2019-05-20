package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "candidate")
public class Candidate extends StoredEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_candidate")
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String telephone;
    private String skype;
    private String mail;
    private String description;

}
