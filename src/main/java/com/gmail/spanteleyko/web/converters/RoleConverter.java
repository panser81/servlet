package com.gmail.spanteleyko.web.converters;

import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.repositories.models.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleConverter {
    public static List<RoleDTO> convertToDTO(List<Role> roles) {

        if (roles == null) {
            return Collections.emptyList();
        }

        List<RoleDTO> rolesDTOs = roles.stream()
                .map((Role role) -> new RoleDTO(role.getId(), role.getName(), role.getDescription()))
                .collect(Collectors.toList());

        return rolesDTOs;
    }

    public static List<Role> convert(List<RoleDTO> roleDTOS) {
        if (roleDTOS == null) {
            return Collections.emptyList();
        }

        List<Role> roles = new ArrayList<>();

        roleDTOS.forEach(role -> {
            roles.add(convert(role));
        });

        return roles;
    }

    public static RoleDTO convert(Role role) {
        RoleDTO roleDTO = new RoleDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
        );

        return roleDTO;
    }

    public static Role convert(RoleDTO roleDTO) {
        Role role = new Role();

        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        return role;
    }
}
