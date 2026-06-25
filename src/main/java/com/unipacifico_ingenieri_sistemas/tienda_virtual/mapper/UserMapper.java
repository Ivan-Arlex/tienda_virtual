package com.unipacifico_ingenieri_sistemas.tienda_virtual.mapper;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.RegisterDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.dto.UserDto;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.Role;
import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.User;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;
        Set<String> roles = user.getRoles().stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toSet());
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .enabled(user.isEnabled())
            .roles(roles)
            .build();
    }

    public User toEntity(RegisterDto dto, String encodedPassword, Set<Role> roles) {
        return User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .password(encodedPassword)
            .fullName(dto.getFullName())
            .enabled(true)
            .roles(roles)
            .build();
    }
}
