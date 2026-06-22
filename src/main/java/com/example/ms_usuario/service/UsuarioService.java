package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.*;

import com.example.ms_usuario.exception.ResourceNotFoundException;

import com.example.ms_usuario.model.usuario;

import com.example.ms_usuario.repository.usuarioRepository;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    UsuarioService.class
            );

    private final usuarioRepository usuarioRepository;

    public List<usuarioResponse> listar() {

        logger.info("Listando usuarios");

        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public usuarioResponse guardar(
            usuarioRequest dto) {

        logger.info(
                "Guardando usuario"
        );

        usuario usuario = com.example.ms_usuario.model.usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();

        return mapToResponse(
                usuarioRepository.save(usuario)
        );
    }

    public usuario buscarEntidadPorEmail(
            String email) {

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        ));
    }

    private usuarioResponse mapToResponse(
            usuario usuario) {

        return usuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}
