package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.InterviewDTO;
import com.innowise.candidatemodule.domain.Interview;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CandidateMapper.class, Interview.class})
public interface InterviewMapper {

    @Mappings({@Mapping(source = "interviewDate", target = "interviewDateTime")})
    InterviewDTO toDTO(Interview interview);

    @Mappings({})
    List<InterviewDTO> toListDTO(List<Interview> interviews);

    @InheritInverseConfiguration
    Interview fromDTO(InterviewDTO interviewDTO);

    @InheritInverseConfiguration
    List<Interview> fromListDTO(List<InterviewDTO> interviewDTOs);
}
