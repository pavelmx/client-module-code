package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.CandidateDTO;
import com.innowise.candidatemodule.domain.Candidate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mappings({})
    CandidateDTO toDTO(Candidate candidate);

    @Mappings({})
    List<CandidateDTO> toListDTO(List<Candidate> candidates);

    @InheritInverseConfiguration
    Candidate fromDTO(CandidateDTO candidateDTO);

    @InheritInverseConfiguration
    List<Candidate> fromListDTO(List<CandidateDTO> candidateDTOs);
}
