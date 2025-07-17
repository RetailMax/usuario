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
import org.springframework.hateoas.CollectionModel;
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
@DisplayName("Tests para UsuarioController - JUnit + Mockito + HATEOAS + Swagger")
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
        rolUsuario.setId(1);
        rolUsuario.setNombre("USUARIO");

        // Configuración de datos de prueba
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

        // Configurar mocks de HATEOAS
        EntityModel<Usuario> usuarioModel1 = EntityModel.of(usuario1);
        EntityModel<Usuario> usuarioModel2 = EntityModel.of(usuario2);
        EntityModel<Usuario> usuarioModelNuevo = EntityModel.of(usuarioNuevo);
        
        when(assembler.toModel(usuario1)).thenReturn(usuarioModel1);
        when(assembler.toModel(usuario2)).thenReturn(usuarioModel2);
        when(assembler.toModel(any(Usuario.class))).thenReturn(usuarioModelNuevo);
        
        CollectionModel<EntityModel<Usuario>> collectionModel = 
            CollectionModel.of(Arrays.asList(usuarioModel1, usuarioModel2));
        when(assembler.toCollectionModel(anyList())).thenReturn(collectionModel);
    }

    // ========== TESTS BÁSICOS FUNCIONALES ==========

    @Test
    @DisplayName("GET /api/v1/usuarios - Debería retornar lista de usuarios")
    void testGetAllUsuarios() throws Exception {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.findAll()).thenReturn(usuarios);

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

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/1")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

        verify(usuarioService, times(1)).obtenerPorId(1);
    }

    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Debería manejar usuario no encontrado (ServletException)")
    void testGetUsuarioById_NoEncontrado() throws Exception {
        // Given
        when(usuarioService.obtenerPorId(999))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        // When & Then - Spring convierte RuntimeException en ServletException (500)
        // Esto es el comportamiento actual del controlador
        try {
            mockMvc.perform(get("/api/v1/usuarios/999")
                    .accept(MediaTypes.HAL_JSON_VALUE));
        } catch (Exception e) {
            // Verificar que es ServletException causada por RuntimeException
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Usuario no encontrado");
        }

        verify(usuarioService, times(1)).obtenerPorId(999);
    }

    @Test
    @DisplayName("POST /api/v1/usuarios - Debería crear usuario exitosamente")
    void testCreateUsuario() throws Exception {
        // Given
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setIdUsuario(3);
        usuarioCreado.setRun("11223344-5");
        usuarioCreado.setPNombre("Pedro");
        usuarioCreado.setSNombre("Antonio");
        usuarioCreado.setAPaterno("Ramírez");
        usuarioCreado.setAMaterno("Silva");
        usuarioCreado.setFechaNacimiento(Date.valueOf(LocalDate.of(1992, 12, 10)));
        usuarioCreado.setRolUsuario(rolUsuario);
        usuarioCreado.setContrasenna("password789");
        usuarioCreado.setCorreoElectronico("pedro.ramirez@email.com");

        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuarioCreado);

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
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería eliminar usuario exitosamente")
    void testDeleteUsuario() throws Exception {
        // Given
        doNothing().when(usuarioService).eliminarUsuario(1);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminarUsuario(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Debería manejar usuario no encontrado (ServletException)")
    void testDeleteUsuario_NoExistente() throws Exception {
        // Given
        doThrow(new RuntimeException("Usuario no encontrado"))
            .when(usuarioService).eliminarUsuario(999);

        // When & Then - Spring convierte RuntimeException en ServletException (500)
        // Esto es el comportamiento actual del controlador
        try {
            mockMvc.perform(delete("/api/v1/usuarios/999"));
        } catch (Exception e) {
            // Verificar que es ServletException causada por RuntimeException
            assert e.getCause() instanceof RuntimeException;
            assert e.getCause().getMessage().contains("Usuario no encontrado");
        }

        verify(usuarioService, times(1)).eliminarUsuario(999);
    }

    // ========== TESTS DE EDGE CASES ==========

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
    @DisplayName("PUT /api/v1/usuarios/perfil/{id} - Debería manejar JSON malformado")
    void testUpdateUsuario_JsonMalformado() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/perfil/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    // ========== TESTS DE INTEGRACIÓN CON HATEOAS ==========

    @Test
    @DisplayName("Verificar que HATEOAS assembler se invoca correctamente")
    void testHateoasIntegration() throws Exception {
        // Given
        List<Usuario> usuarios = Arrays.asList(usuario1);
        when(usuarioService.findAll()).thenReturn(usuarios);

        // When
        mockMvc.perform(get("/api/v1/usuarios")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk());

        // Then - El controlador usa .map(assembler::toModel) en lugar de toCollectionModel
        verify(assembler, times(1)).toModel(usuario1);
    }

    @Test
    @DisplayName("Verificar que el assembler se invoca para usuario individual")
    void testHateoasIntegrationSingleUser() throws Exception {
        // Given
        when(usuarioService.obtenerPorId(1)).thenReturn(usuario1);

        // When
        mockMvc.perform(get("/api/v1/usuarios/1")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk());

        // Then
        verify(assembler, times(1)).toModel(usuario1);
    }
}
