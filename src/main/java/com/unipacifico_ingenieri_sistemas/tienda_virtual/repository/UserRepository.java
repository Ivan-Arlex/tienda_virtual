package com.unipacifico_ingenieri_sistemas.tienda_virtual.repository;

import com.unipacifico_ingenieri_sistemas.tienda_virtual.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
