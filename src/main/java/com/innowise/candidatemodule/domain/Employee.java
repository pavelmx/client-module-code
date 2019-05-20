package com.innowise.candidatemodule.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
@Table(name = "employee")
public class Employee extends StoredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", schema = "candidate_module_db", sequenceName = "sq_employee")
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private boolean deleted;
    @ManyToOne(fetch = FetchType.EAGER)
    private Department department;

}