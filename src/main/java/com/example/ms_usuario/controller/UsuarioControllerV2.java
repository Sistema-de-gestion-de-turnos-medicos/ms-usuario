package com.example.ms_usuario.controller;

import com.example.ms_usuario.assemblers.UsuarioModelAssembler;
import com.example.ms_usuario.dto.usuarioRequest;
import com.example.ms_usuario.dto.usuarioResponse;
import com.example.ms_usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios V2", description = "Gestión de usuarios con HATEOAS")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Listar todos los usuarios",
        description = "Retorna la lista completa de usuarios registrados en el sistema con enlaces HATEOAS"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Lista de usuarios",
                    value = "{ \"_embedded\": { \"usuarioResponseList\": [ { \"id\": 1, \"nombre\": \"Juan Pérez\", \"email\": \"juan@duoc.cl\", \"rol\": \"ESTUDIANTE\", \"_links\": { \"self\": { \"href\": \"/api/v2/usuarios/email/juan@duoc.cl\" } } } ] }, \"_links\": { \"self\": { \"href\": \"/api/v2/usuarios\" } } }"
                )
            )
        )
    })
    public CollectionModel<EntityModel<usuarioResponse>> listar() {
        List<EntityModel<usuarioResponse>> usuarios = usuarioService.listar()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Registra un nuevo usuario en el sistema. El email debe ser único."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Usuario creado",
                    value = "{ \"id\": 1, \"nombre\": \"Juan Pérez\", \"email\": \"juan@duoc.cl\", \"rol\": \"ESTUDIANTE\", \"_links\": { \"self\": { \"href\": \"/api/v2/usuarios/email/juan@duoc.cl\" }, \"usuarios\": { \"href\": \"/api/v2/usuarios\" } } }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos en el request",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = "{ \"timestamp\": \"2026-06-21T10:15:30\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"El email es obligatorio\" }"
                )
            )
        )
    })
    public ResponseEntity<EntityModel<usuarioResponse>> guardar(
            @Valid @RequestBody usuarioRequest dto) {
        usuarioResponse response = usuarioService.guardar(dto);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @GetMapping(value = "/email/{email}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
        summary = "Buscar usuario por email",
        description = "Busca un usuario específico utilizando su dirección de email como identificador único"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Usuario encontrado",
                    value = "{ \"id\": 1, \"nombre\": \"Juan Pérez\", \"email\": \"juan@duoc.cl\", \"rol\": \"ESTUDIANTE\", \"_links\": { \"self\": { \"href\": \"/api/v2/usuarios/email/juan@duoc.cl\" } } }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Usuario no encontrado",
                    value = "{ \"timestamp\": \"2026-06-21T10:15:30\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuario no encontrado\" }"
                )
            )
        )
    })
    public EntityModel<usuarioResponse> buscarPorEmail(@PathVariable String email) {
        usuarioResponse response = usuarioService.listar()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return assembler.toModel(response);
    }
}

