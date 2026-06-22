package com.example.ms_usuario.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class usuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
}