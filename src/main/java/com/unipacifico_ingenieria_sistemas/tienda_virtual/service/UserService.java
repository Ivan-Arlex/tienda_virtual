package com.unipacifico_ingenieria_sistemas.tienda_virtual.service;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.RegisterDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.dto.UserDto;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.mapper.UserMapper;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Role;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.RoleType;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.RoleRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public Optional<UserDto> getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Validamos si no hay sesión activa o si es el usuario anónimo por defecto de Spring
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return Optional.empty();
        }

        String username = auth.getName();
        
        // Buscamos en la BD y mapeamos los campos al DTO
       // return userMapper.toDto(userRepository.findByUsername(username));
        return userRepository.findByUsername(username).map(user->userMapper.toDto(Optional.of(user)));
    }
    public UserDto register(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Role clientRole = roleRepository.findByName(RoleType.ROLE_CLIENTE)
            .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Users users = userMapper.toEntity(dto, passwordEncoder.encode(dto.getPassword()), Set.of(clientRole));
        return userMapper.toDto((Optional<Users>) Optional.ofNullable(userRepository.save(users)));
    }

    public UserDto findByUsername(String username) {
        return userMapper.toDto(findEntityByUsername(username));
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }

    public long countClients() {
        return userRepository.findAll().stream()
            .filter(u -> u.getRoles().stream()
                .anyMatch(r -> r.getName() == RoleType.ROLE_CLIENTE))
            .count();
    }

    // package-private: solo para uso interno entre servicios del mismo paquete
    Users findEntityByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }
}
