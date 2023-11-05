package com.connectravel.domain.dto;

import com.connectravel.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesDTO {

    private String id;

    private String resourceName;

    private String httpMethod;

    private int orderNum;

    private String resourceType;

    private String roleName;

    private Set<Role> roleSet;

}