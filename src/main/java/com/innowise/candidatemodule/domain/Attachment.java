package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "attachment")
public class Attachment extends StoredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_attachment")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Candidate candidate;
    private String fileName;
    private String path;

}
