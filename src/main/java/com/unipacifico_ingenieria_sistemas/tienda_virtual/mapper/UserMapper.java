package com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.RegisterDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.UserDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Role;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(Users users) {
        if (users == null) return null;
        Set<String> roles = users.getRoles().stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toSet());
        return UserDto.builder()
            .id(users.getId())
            .username(users.getUsername())
            .email(users.getEmail())
            .fullName(users.getFullName())
            .enabled(users.isEnabled())
            .roles(roles)
            .build();
    }

    public Users toEntity(RegisterDto dto, String encodedPassword, Set<Role> roles) {
        return Users.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .password(encodedPassword)
            .fullName(dto.getFullName())
            .enabled(true)
            .roles(roles)
            .build();
    }
}
