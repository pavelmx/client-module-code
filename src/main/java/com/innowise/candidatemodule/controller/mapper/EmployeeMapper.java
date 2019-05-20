package com.innowise.candidatemodule.controller.mapper;

import com.innowise.candidatemodule.controller.dto.EmployeeDTO;
import com.innowise.candidatemodule.domain.Employee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface EmployeeMapper {

    @Mappings({})
    EmployeeDTO toDTO(Employee employee);

    @Mappings({})
    List<EmployeeDTO> toListDTO(List<Employee> employees);

    @InheritInverseConfiguration
    Employee fromDTO(EmployeeDTO employeeDTO);

    @InheritInverseConfiguration
    List<Employee> fromListDTO(List<EmployeeDTO> employeeDTOs);
}
