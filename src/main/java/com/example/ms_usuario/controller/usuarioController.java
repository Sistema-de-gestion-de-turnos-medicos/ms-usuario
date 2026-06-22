package com.example.ms_usuario.controller;

import com.example.ms_usuario.dto.*;

import com.example.ms_usuario.model.usuario;

import com.example.ms_usuario.service.UsuarioService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class usuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<usuarioResponse>>
    listar() {

        return ResponseEntity.ok(
                usuarioService.listar()
        );
    }

    @PostMapping
    public ResponseEntity<usuarioResponse>
    guardar(
            @Valid
            @RequestBody
            usuarioRequest dto) {

        return new ResponseEntity<>(
                usuarioService.guardar(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<usuario>
    buscarPorEmail(
            @PathVariable String email) {

        return ResponseEntity.ok(
                usuarioService.buscarEntidadPorEmail(
                        email
                )
        );
    }
}