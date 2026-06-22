package com.example.ms_usuario.service;

import com.example.ms_usuario.dto.usuarioRequest;
import com.example.ms_usuario.dto.usuarioResponse;
import com.example.ms_usuario.exception.ResourceNotFoundException;
import com.example.ms_usuario.model.usuario;
import com.example.ms_usuario.repository.usuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios de UsuarioService")
class UsuarioServiceTest {

    @Mock
    private usuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private usuario usuarioEjemplo;
    private usuarioRequest requestEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = usuario.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan@duoc.cl")
                .password("clave123")
                .rol("ESTUDIANTE")
                .build();

        requestEjemplo = new usuarioRequest();
        requestEjemplo.setNombre("Juan Pérez");
        requestEjemplo.setEmail("juan@duoc.cl");
        requestEjemplo.setPassword("clave123");
        requestEjemplo.setRol("ESTUDIANTE");
    }

    // ---------- listar() ----------

    @Test
    @DisplayName("Dado que existen usuarios, cuando se listan, entonces retorna la lista mapeada correctamente")
    void listar_conUsuariosExistentes_retornaListaMapeada() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        // When
        List<usuarioResponse> resultado = usuarioService.listar();

        // Then
        assertThat(resultado).hasSize(1);
        usuarioResponse response = resultado.get(0);
        assertEquals(1L, response.getId());
        assertEquals("Juan Pérez", response.getNombre());
        assertEquals("juan@duoc.cl", response.getEmail());
        assertEquals("ESTUDIANTE", response.getRol());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dado que no existen usuarios, cuando se listan, entonces retorna una lista vacía")
    void listar_sinUsuarios_retornaListaVacia() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(List.of());

        // When
        List<usuarioResponse> resultado = usuarioService.listar();

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository, times(1)).findAll();
    }

    // ---------- guardar() ----------

    @Test
    @DisplayName("Dado un usuario válido, cuando se guarda, entonces se persiste y retorna el response correcto")
    void guardar_conDatosValidos_retornaUsuarioGuardado() {
        // Given
        when(usuarioRepository.save(any(usuario.class))).thenReturn(usuarioEjemplo);

        // When
        usuarioResponse resultado = usuarioService.guardar(requestEjemplo);

        // Then
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@duoc.cl", resultado.getEmail());
        assertEquals("ESTUDIANTE", resultado.getRol());
        verify(usuarioRepository, times(1)).save(any(usuario.class));
    }

    @Test
    @DisplayName("Dado un request, cuando se guarda, entonces el repository recibe una entidad con los mismos datos del DTO")
    void guardar_conDatosValidos_construyeEntidadConDatosDelRequest() {
        // Given
        when(usuarioRepository.save(any(usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        usuarioResponse resultado = usuarioService.guardar(requestEjemplo);

        // Then
        verify(usuarioRepository).save(argThat(u ->
                u.getNombre().equals("Juan Pérez") &&
                        u.getEmail().equals("juan@duoc.cl") &&
                        u.getPassword().equals("clave123") &&
                        u.getRol().equals("ESTUDIANTE")
        ));
        assertEquals("juan@duoc.cl", resultado.getEmail());
    }

    // ---------- buscarEntidadPorEmail() ----------

    @Test
    @DisplayName("Dado un email existente, cuando se busca, entonces retorna la entidad usuario")
    void buscarEntidadPorEmail_conEmailExistente_retornaUsuario() {
        // Given
        when(usuarioRepository.findByEmail("juan@duoc.cl"))
                .thenReturn(Optional.of(usuarioEjemplo));

        // When
        usuario resultado = usuarioService.buscarEntidadPorEmail("juan@duoc.cl");

        // Then
        assertEquals("juan@duoc.cl", resultado.getEmail());
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository, times(1)).findByEmail("juan@duoc.cl");
    }

    @Test
    @DisplayName("Dado un email inexistente, cuando se busca, entonces lanza ResourceNotFoundException")
    void buscarEntidadPorEmail_conEmailInexistente_lanzaExcepcion() {
        // Given
        when(usuarioRepository.findByEmail("noexiste@duoc.cl"))
                .thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarEntidadPorEmail("noexiste@duoc.cl")
        );

        assertEquals("Usuario no encontrado", excepcion.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("noexiste@duoc.cl");
    }
}