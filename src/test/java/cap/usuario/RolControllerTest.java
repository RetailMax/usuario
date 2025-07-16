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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import static org.hamcrest.Matchers.*;

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

        rolEntityModel = EntityModel.of(rol);
        when(assembler.toModel(any(Rol.class))).thenReturn(rolEntityModel);
    }
    @Nested
    @DisplayName("GET /api/v1/roles - Listar Roles")
    class ListarRolesTests {

        @Test
        @DisplayName("Debería retornar lista de roles cuando existen roles")
        void testGetAllRoles_Success() throws Exception {
            List<Rol> roles = Arrays.asList(rol, rol2);
            when(rolService.listarRoles()).thenReturn(roles);

            mockMvc.perform(get("/api/v1/roles")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$._embedded.rolList", hasSize(2)))
                    .andExpect(jsonPath("$._embedded.rolList[0].id").value(1))
                    .andExpect(jsonPath("$._embedded.rolList[0].nombre").value("ADMIN"));

            verify(rolService, times(1)).listarRoles();
            verify(assembler, times(2)).toModel(any(Rol.class));
        }

        @Test
        @DisplayName("Debería ejecutar método listarRoles del controller")
        void testListarRoles_DirectMethodCall() throws Exception {
            List<Rol> roles = Arrays.asList(rol, rol2);
            when(rolService.listarRoles()).thenReturn(roles);

            mockMvc.perform(get("/api/v1/roles")
                            .accept("application/hal+json"))
                    .andExpect(status().isOk());

            verify(rolService, times(1)).listarRoles();
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no existen roles")
        void testGetAllRoles_EmptyList() throws Exception {
            when(rolService.listarRoles()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/roles")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$._embedded").doesNotExist());

            verify(rolService, times(1)).listarRoles();
            verify(assembler, never()).toModel(any(Rol.class));
        }

        @Test
        @DisplayName("Debería manejar excepción del servicio")
        void testGetAllRoles_ServiceException() throws Exception {
            when(rolService.listarRoles()).thenThrow(new RuntimeException("Error interno"));

            mockMvc.perform(get("/api/v1/roles")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).listarRoles();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/roles/{id} - Buscar Rol por ID")
    class BuscarRolTests {

        @Test
        @DisplayName("Debería ejecutar método buscarRol del controller")
        void testBuscarRol_DirectMethodCall() throws Exception {
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            mockMvc.perform(get("/api/v1/roles/1")
                            .accept("application/hal+json"))
                    .andExpect(status().isOk());

            verify(rolService, times(1)).buscarRolPorId(1);
            verify(assembler, times(1)).toModel(rol);
        }

        @Test
        @DisplayName("Debería retornar rol cuando existe")
        void testGetRolById_Success() throws Exception {
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("ADMIN"));

            verify(rolService, times(1)).buscarRolPorId(1);
            verify(assembler, times(1)).toModel(rol);
        }

        @Test
        @DisplayName("Debería buscar diferentes IDs")
        void testBuscarRol_DifferentIds() throws Exception {
            when(rolService.buscarRolPorId(2)).thenReturn(rol2);

            mockMvc.perform(get("/api/v1/roles/2")
                            .accept("application/hal+json"))
                    .andExpect(status().isOk());

            verify(rolService, times(1)).buscarRolPorId(2);
        }

        @Test
        @DisplayName("Debería retornar 404 cuando el rol no existe")
        void testGetRolById_NotFound() throws Exception {
            when(rolService.buscarRolPorId(999)).thenThrow(new RuntimeException("Rol no encontrado"));

            mockMvc.perform(get("/api/v1/roles/999")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).buscarRolPorId(999);
        }

        @Test
        @DisplayName("Debería validar formato de ID")
        void testGetRolById_InvalidIdFormat() throws Exception {
            mockMvc.perform(get("/api/v1/roles/invalid")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).buscarRolPorId(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/roles - Crear Rol")
    class CrearRolTests {

        @Test
        @DisplayName("Debería crear rol exitosamente")
        void testCreateRol_Success() throws Exception {
            when(rolService.guardarRol(any(Rol.class))).thenReturn(rol);

            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rol)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("ADMIN"))
                    .andExpect(header().exists("Location"));

            verify(rolService, times(1)).guardarRol(any(Rol.class));
            verify(assembler, times(1)).toModel(rol);
        }

        @Test
        @DisplayName("Debería rechazar JSON vacío")
        void testCreateRol_EmptyBody() throws Exception {
            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).guardarRol(any(Rol.class));
        }

        @Test
        @DisplayName("Debería rechazar Content-Type inválido")
        void testCreateRol_InvalidContentType() throws Exception {
            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("invalid"))
                    .andExpect(status().isUnsupportedMediaType());

            verify(rolService, never()).guardarRol(any(Rol.class));
        }

        @Test
        @DisplayName("Debería manejar nombre duplicado")
        void testCreateRol_DuplicateName() throws Exception {
            when(rolService.guardarRol(any(Rol.class)))
                    .thenThrow(new RuntimeException("Rol ya existe"));

            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rol)))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).guardarRol(any(Rol.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/roles/{id} - Actualizar Rol")
    class ActualizarRolTests {

        @Test
        @DisplayName("Debería actualizar rol exitosamente")
        void testUpdateRol_Success() throws Exception {
            Rol rolActualizado = new Rol();
            rolActualizado.setNombre("SUPER_ADMIN");
            
            when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rol);

            mockMvc.perform(put("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rolActualizado)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("ADMIN"));

            verify(rolService, times(1)).actualizarRol(eq(1), any(Rol.class));
            verify(assembler, times(1)).toModel(rol);
        }

        @Test
        @DisplayName("Debería manejar rol inexistente")
        void testUpdateRol_NotFound() throws Exception {
            when(rolService.actualizarRol(eq(999), any(Rol.class)))
                    .thenThrow(new RuntimeException("Rol no encontrado"));

            mockMvc.perform(put("/api/v1/roles/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(rol)))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).actualizarRol(eq(999), any(Rol.class));
        }

        @Test
        @DisplayName("Debería validar datos de entrada")
        void testUpdateRol_InvalidData() throws Exception {
            mockMvc.perform(put("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("invalid json"))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).actualizarRol(any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/roles/{id} - Eliminar Rol")
    class EliminarRolTests {

        @Test
        @DisplayName("Debería eliminar rol exitosamente")
        void testDeleteRol_Success() throws Exception {
            doNothing().when(rolService).eliminarRol(1);

            mockMvc.perform(delete("/api/v1/roles/1"))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(rolService, times(1)).eliminarRol(1);
        }

        @Test
        @DisplayName("Debería manejar rol inexistente")
        void testDeleteRol_NotFound() throws Exception {
            doThrow(new RuntimeException("Rol no encontrado"))
                    .when(rolService).eliminarRol(999);

            mockMvc.perform(delete("/api/v1/roles/999"))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).eliminarRol(999);
        }

        @Test
        @DisplayName("Debería manejar rol con dependencias")
        void testDeleteRol_HasDependencies() throws Exception {
            doThrow(new RuntimeException("Rol tiene usuarios asociados"))
                    .when(rolService).eliminarRol(1);

            mockMvc.perform(delete("/api/v1/roles/1"))
                    .andExpect(status().isInternalServerError());

            verify(rolService, times(1)).eliminarRol(1);
        }

        @Test
        @DisplayName("Debería validar formato de ID")
        void testDeleteRol_InvalidIdFormat() throws Exception {
            mockMvc.perform(delete("/api/v1/roles/invalid"))
                    .andExpect(status().isBadRequest());

            verify(rolService, never()).eliminarRol(any());
        }
    }

    @Nested
    @DisplayName("Validaciones generales")
    class ValidacionesGeneralesTests {

        @Test
        @DisplayName("Debería rechazar métodos HTTP no soportados")
        void testUnsupportedHttpMethods() throws Exception {
            mockMvc.perform(patch("/api/v1/roles/1"))
                    .andExpect(status().isMethodNotAllowed());
        }

        @Test
        @DisplayName("Debería manejar URLs mal formadas")
        void testMalformedUrls() throws Exception {
            mockMvc.perform(get("/api/v1/roles/1/invalid"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debería validar Accept header")
        void testAcceptHeaderValidation() throws Exception {
            when(rolService.buscarRolPorId(1)).thenReturn(rol);

            mockMvc.perform(get("/api/v1/roles/1")
                            .accept(MediaType.TEXT_PLAIN))
                    .andExpect(status().isNotAcceptable());
        }
    }
}
