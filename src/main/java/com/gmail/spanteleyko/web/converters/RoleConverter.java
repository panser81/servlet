package com.gmail.spanteleyko.web.converters;

import com.gmail.spanteleyko.web.models.RoleDTO;
import com.gmail.spanteleyko.web.repositories.models.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleConverter {
    public static List<RoleDTO> convertToDTO(List<Role> roles) {
        if (roles == null) {
            return new ArrayList<>();
        }

        List<RoleDTO> roleDTO = new ArrayList<>();

        roles.forEach(role -> {
            roleDTO.add(convert(role));
        });

        return roleDTO;
    }

    public static List<Role> convert(List<RoleDTO> roleDTOS) {
        if (roleDTOS == null) {
            return new ArrayList<>();
        }

        List<Role> roles = new ArrayList<>();

        roleDTOS.forEach(role -> {
            roles.add(convert(role));
        });

        return roles;
    }

    public static RoleDTO convert(Role role) {
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());

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
