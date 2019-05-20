package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.InterviewEmployeeDTO;
import com.innowise.candidatemodule.domain.InterviewEmployee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InterviewMapper.class, EmployeeMapper.class})
public interface InterviewEmployeeMapper {

    @Mappings({})
    InterviewEmployeeDTO toDTO(InterviewEmployee interviewEmployee);

    @Mappings({})
    List<InterviewEmployeeDTO> toListDTO(List<InterviewEmployee> interviewEmployees);

    @InheritInverseConfiguration
    InterviewEmployee fromDTO(InterviewEmployeeDTO interviewEmployeeDTO);

    @InheritInverseConfiguration
    List<InterviewEmployee> fromListDTO(List<InterviewEmployeeDTO> interviewEmployeeDTOs);
}
