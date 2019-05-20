package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.DepartmentDTO;
import com.innowise.candidatemodule.domain.Department;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mappings({})
    DepartmentDTO toDTO(Department department);

    @Mappings({})
    List<DepartmentDTO> toListDTO(List<Department> departments);

    @InheritInverseConfiguration
    Department fromDTO(DepartmentDTO departmentDTO);

    @InheritInverseConfiguration
    List<Department> fromListDTO(List<DepartmentDTO> departmentDTOs);
}
