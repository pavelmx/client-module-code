package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Table(name = "department")
public class Department extends StoredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_department")
    private Long id;
    @Column(nullable = false)
    private String departmentName;

}
