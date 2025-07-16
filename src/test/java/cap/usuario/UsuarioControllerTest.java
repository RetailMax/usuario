package cap.usuario;

import cap.usuario.assemblers.UsuarioModelAssembler;
import cap.usuario.controller.UsuarioController;
import cap.usuario.model.Rol;
import cap.usuario.model.Usuario;
import cap.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@DisplayName("Tests para UsuarioController")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuarioNuevo;
    private Rol rolUsuario;

    @BeforeEach
    void setUp() {
        // Configuración de rol de prueba
        rolUsuario = new Rol();
        // Asumiendo que Rol tiene estos campos básicos
        // rolUsuario.setId(1);
        // rolUsuario.setNombre("USUARIO");

        // Configuración de datos de prueba basado en la estructura completa
        usuario1 = new Usuario();
        usuario1.setIdUsuario(1);
        usuario1.setRun("12345678-9");
        usuario1.setPNombre("Juan");
        usuario1.setSNombre("Carlos");
        usuario1.setAPaterno("Pérez");
        usuario1.setAMaterno("González");
        usuario1.setFechaNacimiento(Date.valueOf(LocalDate.of(1990, 5, 15)));
        usuario1.setRolUsuario(rolUsuario);
        usuario1.setContrasenna("password123");
        usuario1.setCorreoElectronico("juan.perez@email.com");

        usuario2 = new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setRun("98765432-1");
        usuario2.setPNombre("María");
        usuario2.setSNombre("Elena");
        usuario2.setAPaterno("López");
        usuario2.setAMaterno("Martínez");
        usuario2.setFechaNacimiento(Date.valueOf(LocalDate.of(1985, 8, 22)));
        usuario2.setRolUsuario(rolUsuario);
        usuario2.setContrasenna("password456");
        usuario2.setCorreoElectronico("maria.lopez@email.com");

        usuarioNuevo = new Usuario();
        usuarioNuevo.setRun("11223344-5");
        usuarioNuevo.setPNombre("Pedro");
        usuarioNuevo.setSNombre("Antonio");
        usuarioNuevo.setAPaterno("Ramírez");
        usuarioNuevo.setAMaterno("Silva");
        usuarioNuevo.setFechaNacimiento(Date.valueOf(LocalDate.of(1992, 12, 10)));
        usuarioNuevo.setRolUsuario(rolUsuario);
        usuarioNuevo.setContrasenna("password789");
        usuarioNuevo.setCorreoElectronico("pedro.ramirez@email.com");
    }

    @Test
    @DisplayName("GET /api/v1/usuarios - Debería retornar lista de usuarios")
    void testGetAllUsuarios() throws Exception {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.findAll()).thenReturn(usuarios);
        when(assembler.toModel(any(Usuario.class))).thenReturn(EntityModel.of(usuario1));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/usuarios - Debería retornar lista vacía cuando no hay usuarios")
    void testGetAllUsuarios_ListaVacia() throws Exception {
        // Given
        when(usuarioService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Debería retornar usuario por ID")
    void testGetUsuarioById() throws Exception {
        // Given
        when(usuarioService.obtenerPorId(1)).thenReturn(usuario1);
        when(assembler.toModel(usuario1)).thenReturn(EntityModel.of(usuario1));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/1")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).obtenerPorId(1);
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Debería manejar usuario no encontrado")
    void testGetUsuarioById_NoEncontrado() throws Exception {
        // Given
        when(usuarioService.obtenerPorId(999))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/999")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).obtenerPorId(999);
    }

    @Test
    @DisplayName("POST /api/v1/usuarios - Debería crear usuario exitosamente")
    void testCreateUsuario() throws Exception {
        // Given
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setIdUsuario(3);
        usuarioCreado.setPNombre("Pedro");
        usuarioCreado.setAPaterno("Ramírez");
        usuarioCreado.setAMaterno("Silva");
        usuarioCreado.setCorreoElectronico("pedro.ramirez@email.com");

        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuarioCreado);
        when(assembler.toModel(usuarioCreado)).thenReturn(EntityModel.of(usuarioCreado));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioNuevo))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().exists("Location"));

        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("POST /api/v1/usuarios - Debería manejar error al crear usuario")
    void testCreateUsuario_Error() throws Exception {
        // Given
        when(usuarioService.registrarUsuario(any(Usuario.class)))
            .thenThrow(new RuntimeException("Error al registrar usuario"));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioNuevo))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("POST /api/v1/usuarios - Debería manejar JSON malformado")
    void testCreateUsuario_JsonMalformado() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/usuarios/perfil/{id} - Debería actualizar usuario exitosamente")
    void testUpdateUsuario() throws Exception {
        // Given
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setIdUsuario(1);
        usuarioActualizado.setRun("12345678-9");
        usuarioActualizado.setPNombre("Juan Carlos");
        usuarioActualizado.setSNombre("Manuel");
        usuarioActualizado.setAPaterno("Pérez");
        usuarioActualizado.setAMaterno("González");
        usuarioActualizado.setFechaNacimiento(Date.valueOf(LocalDate.of(1990, 5, 15)));
        usuarioActualizado.setRolUsuario(rolUsuario);
        usuarioActualizado.setContrasenna("newpassword123");
        usuarioActualizado.setCorreoElectronico("juan.carlos.perez@email.com");

        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class)))
            .thenReturn(usuarioActualizado);
        when(assembler.toModel(usuarioActualizado)).thenReturn(EntityModel.of(usuarioActualizado));

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/perfil/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).actualizarUsuario(eq(1), any(Usuario.class));
    }

    @Test
    @DisplayName("PUT /api/v1/usuarios/perfil/{id} - Debería manejar error al actualizar")
    void testUpdateUsuario_Error() throws Exception {
        // Given
        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class)))
            .thenThrow(new RuntimeException("Error al actualizar usuario"));

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/perfil/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).actualizarUsuario(eq(1), any(Usuario.class));
    }

    @Test
    @DisplayName("PUT /api/v1/usuarios/perfil/{id} - Debería manejar JSON malformado en actualización")
    void testUpdateUsuario_JsonMalformado() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/perfil/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería eliminar usuario exitosamente")
    void testDeleteUsuario() throws Exception {
        // Given
        doNothing().when(usuarioService).eliminarUsuario(1);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería manejar error al eliminar")
    void testDeleteUsuario_Error() throws Exception {
        // Given
        doThrow(new RuntimeException("Error al eliminar usuario"))
            .when(usuarioService).eliminarUsuario(1);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).eliminarUsuario(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería manejar eliminación de usuario no existente")
    void testDeleteUsuario_NoExistente() throws Exception {
        // Given
        doThrow(new RuntimeException("Usuario no encontrado"))
            .when(usuarioService).eliminarUsuario(999);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/999")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).eliminarUsuario(999);
    }

    @Test
    @DisplayName("Debería manejar parámetros de path inválidos")
    void testParametrosPathInvalidos() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/invalid-id")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería validar content-type requerido para POST")
    void testValidarContentTypePost() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .content(objectMapper.writeValueAsString(usuarioNuevo)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Debería validar todos los campos requeridos del usuario")
    void testValidarCamposRequeridosUsuario() throws Exception {
        // Given - Usuario con campos faltantes
        Usuario usuarioIncompleto = new Usuario();
        usuarioIncompleto.setPNombre("Juan");
        // Faltan campos requeridos

        when(usuarioService.registrarUsuario(any(Usuario.class)))
            .thenThrow(new RuntimeException("Campos requeridos faltantes"));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioIncompleto))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería validar RUN único")
    void testValidarRunUnico() throws Exception {
        // Given
        when(usuarioService.registrarUsuario(any(Usuario.class)))
            .thenThrow(new RuntimeException("RUN ya existe"));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioNuevo))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isInternalServerError());

        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería validar content-type requerido para PUT")
    void testValidarContentTypePut() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/perfil/1")
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isUnsupportedMediaType());
    }

}
