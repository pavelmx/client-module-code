package com.innowise.candidatemodule.controller.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private String id;
    private String firstName;
    private String lastName;
    private boolean deleted;
    private DepartmentDTO department;
}
