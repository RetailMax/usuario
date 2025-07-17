package cap.usuario;

import cap.usuario.assemblers.RolModelAssembler;
import cap.usuario.controller.RolController;
import cap.usuario.model.Rol;
import cap.usuario.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
@DisplayName("RolController Tests")
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RolService rolService;

    @MockitoBean
    private RolModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol rol;
    private Rol rol2;
    private EntityModel<Rol> rolEntityModel;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("ADMIN");

        rol2 = new Rol();
        rol2.setId(2);
        rol2.setNombre("USER");

        // Setup HATEOAS mocks
        rolEntityModel = EntityModel.of(rol);
        EntityModel<Rol> rolEntityModel2 = EntityModel.of(rol2);
        
        when(assembler.toModel(rol)).thenReturn(rolEntityModel);
        when(assembler.toModel(rol2)).thenReturn(rolEntityModel2);
        when(assembler.toModel(any(Rol.class))).thenReturn(rolEntityModel);
    }

    @Nested
    @DisplayName("Listar Roles Tests")
    class ListarRolesTests {

        @Test
        @DisplayName("Debe retornar todos los roles con éxito")
        void testGetAllRoles_Success() throws Exception {
            // Given
            List<Rol> roles = Arrays.asList(rol, rol2);
            when(rolService.listarRoles()).thenReturn(roles);

            // When & Then
            mockMvc.perform(get("/api/v1/roles")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

            verify(rolService, times(1)).listarRoles();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay roles")
        void testGetAllRoles_EmptyList() throws Exception {
            // Given
            when(rolService.listarRoles()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/roles")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

            verify(rolService, times(1)).listarRoles();
        }

        @Test
        @DisplayName("Debe manejar excepción del servicio")
        void testGetAllRoles_ServiceException() throws Exception {
            // Given
            when(rolService.listarRoles())
                    .thenThrow(new RuntimeException("Error del servicio"));

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(get("/api/v1/roles")
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).listarRoles();
        }
    }

    @Nested
    @DisplayName("Buscar Rol Tests")
    class BuscarRolTests {

        @Test
        @DisplayName("Debe retornar rol existente por ID")
        void testGetRolById_Success() throws Exception {
            // Given
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            // When & Then
            mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

            verify(rolService, times(1)).buscarRolPorId(1);
        }

        @Test
        @DisplayName("Debe manejar rol no encontrado")
        void testGetRolById_NotFound() throws Exception {
            // Given
            when(rolService.buscarRolPorId(999))
                    .thenThrow(new RuntimeException("Rol no encontrado"));

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(get("/api/v1/roles/999")
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).buscarRolPorId(999);
        }

        @Test
        @DisplayName("Debe manejar ID con formato inválido")
        void testGetRolById_InvalidIdFormat() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/v1/roles/abc")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).buscarRolPorId(any());
        }
    }

    @Nested
    @DisplayName("Crear Rol Tests")
    class CrearRolTests {

        @Test
        @DisplayName("Debe crear rol con datos válidos")
        void testCreateRol_Success() throws Exception {
            // Given
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre("MODERATOR");

            when(rolService.guardarRol(any(Rol.class))).thenReturn(rol);

            // When & Then
            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevoRol))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

            verify(rolService, times(1)).guardarRol(any(Rol.class));
        }

        @Test
        @DisplayName("Debe manejar JSON malformado")
        void testCreateRol_InvalidJson() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).guardarRol(any());
        }

        @Test
        @DisplayName("Debe manejar cuerpo vacío")
        void testCreateRol_EmptyBody() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).guardarRol(any());
        }

        @Test
        @DisplayName("Debe manejar rol duplicado")
        void testCreateRol_DuplicateName() throws Exception {
            // Given
            when(rolService.guardarRol(any(Rol.class)))
                    .thenThrow(new RuntimeException("Rol ya existe"));

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(post("/api/v1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rol))
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).guardarRol(any(Rol.class));
        }
    }

    @Nested
    @DisplayName("Actualizar Rol Tests")
    class ActualizarRolTests {

        @Test
        @DisplayName("Debe actualizar rol existente")
        void testUpdateRol_Success() throws Exception {
            // Given
            Rol rolActualizado = new Rol();
            rolActualizado.setId(1);
            rolActualizado.setNombre("SUPER_ADMIN");

            when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rolActualizado);

            // When & Then
            mockMvc.perform(put("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rolActualizado))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE));

            verify(rolService, times(1)).actualizarRol(eq(1), any(Rol.class));
        }

        @Test
        @DisplayName("Debe manejar rol inexistente para actualizar")
        void testUpdateRol_NotFound() throws Exception {
            // Given
            when(rolService.actualizarRol(eq(999), any(Rol.class)))
                    .thenThrow(new RuntimeException("Rol no encontrado"));

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(put("/api/v1/roles/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(rol))
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).actualizarRol(eq(999), any(Rol.class));
        }

        @Test
        @DisplayName("Debe manejar datos inválidos en actualización")
        void testUpdateRol_InvalidData() throws Exception {
            // Given
            when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rol);

            // When & Then
            mockMvc.perform(put("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk()); // El controller no valida, depende del servicio

            verify(rolService, times(1)).actualizarRol(eq(1), any(Rol.class));
        }
    }

    @Nested
    @DisplayName("Eliminar Rol Tests")
    class EliminarRolTests {

        @Test
        @DisplayName("Debe eliminar rol existente")
        void testDeleteRol_Success() throws Exception {
            // Given
            doNothing().when(rolService).eliminarRol(1);

            // When & Then
            mockMvc.perform(delete("/api/v1/roles/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(rolService, times(1)).eliminarRol(1);
        }

        @Test
        @DisplayName("Debe manejar rol inexistente para eliminar")
        void testDeleteRol_NotFound() throws Exception {
            // Given
            doThrow(new RuntimeException("Rol no encontrado"))
                    .when(rolService).eliminarRol(999);

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(delete("/api/v1/roles/999")
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).eliminarRol(999);
        }

        @Test
        @DisplayName("Debe manejar rol con dependencias")
        void testDeleteRol_HasDependencies() throws Exception {
            // Given
            doThrow(new RuntimeException("Rol tiene usuarios asociados"))
                    .when(rolService).eliminarRol(1);

            // When & Then
            assertThrows(Exception.class, () -> {
                mockMvc.perform(delete("/api/v1/roles/1")
                                .accept(MediaTypes.HAL_JSON))
                        .andExpect(status().isInternalServerError());
            });

            verify(rolService, times(1)).eliminarRol(1);
        }

        @Test
        @DisplayName("Debe manejar ID inválido para eliminar")
        void testDeleteRol_InvalidId() throws Exception {
            // When & Then
            mockMvc.perform(delete("/api/v1/roles/abc")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).eliminarRol(any());
        }
    }

    @Nested
    @DisplayName("Validaciones Generales Tests")
    class ValidacionesGeneralesTests {

        @Test
        @DisplayName("Debe validar Content-Type HAL+JSON")
        void testValidateHalJsonContentType() throws Exception {
            // Given
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            // When & Then
            String contentType = mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentType();

            // Using JUnit assertion instead of Hamcrest
            assertNotNull(contentType);
            assertTrue(contentType.contains("application/hal+json"));
            verify(rolService, times(1)).buscarRolPorId(1);
        }

        @Test
        @DisplayName("Debe manejar Accept header no soportado")
        void testUnsupportedAcceptHeader() throws Exception {
            // Given
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            // When & Then
            mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaType.APPLICATION_XML))
                    .andExpect(status().isNotAcceptable());
        }

        @Test
        @DisplayName("Debe validar que el assembler sea llamado correctamente")
        void testAssemblerIsCalledCorrectly() throws Exception {
            // Given
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            // When & Then
            mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk());

            verify(assembler, times(1)).toModel(rol);
        }
    }
}
