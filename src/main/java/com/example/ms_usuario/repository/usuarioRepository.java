package com.example.ms_usuario.repository;

import com.example.ms_usuario.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usuarioRepository
        extends JpaRepository<usuario, Long> {

    Optional<usuario> findByEmail(
            String email
    );
}