package com.innowise.candidatemodule.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
abstract class StoredEntity implements Serializable {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;

}
