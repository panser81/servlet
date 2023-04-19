package com.gmail.spanteleyko.web.converters;

import com.gmail.spanteleyko.web.models.UserDTO;
import com.gmail.spanteleyko.web.repositories.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static List<UserDTO> convert(List<User> users) {
        List<UserDTO> userDTO = new ArrayList<>();

        users.forEach(role -> {
            userDTO.add(convert(role));
        });

        return userDTO;
    }

    public static User convert(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        user.setCreatedBy(userDTO.getCreatedBy());
        user.setRoles(RoleConverter.convert(userDTO.getRoles()));
        return user;
    }

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setPassword(user.getPassword());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(RoleConverter.convertToDTO(user.getRoles()));

        return userDTO;
    }


}
