package com.example.ms_usuario.assemblers;


import com.example.ms_usuario.controller.UsuarioControllerV2;
import com.example.ms_usuario.dto.usuarioResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler
        implements RepresentationModelAssembler<usuarioResponse, EntityModel<usuarioResponse>> {

    @Override
    public EntityModel<usuarioResponse> toModel(usuarioResponse usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioControllerV2.class)
                        .buscarPorEmail(usuario.getEmail())).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class)
                        .listar()).withRel("usuarios"));
    }
}