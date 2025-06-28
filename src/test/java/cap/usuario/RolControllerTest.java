package cap.usuario;

import cap.usuario.controller.RolController;
import cap.usuario.model.Rol;
import cap.usuario.service.RolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1); // Usa setId si tu modelo tiene 'id', o setIdRol si es 'idRol'
        rol.setNombre("ADMIN");
    }

    @Test
    void testGetAllRoles() throws Exception {
        when(rolService.listarRoles()).thenReturn(List.of(rol));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.rolList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.rolList[0].nombre").value("ADMIN"));
    }

    @Test
    void testGetRolById() throws Exception {
        when(rolService.buscarRolPorId(1)).thenReturn(rol);

        mockMvc.perform(get("/api/v1/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"));
    }

    @Test
    void testCreateRol() throws Exception {
        when(rolService.guardarRol(any(Rol.class))).thenReturn(rol);

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"));
    }

    @Test
    void testUpdateRol() throws Exception {
        when(rolService.actualizarRol(eq(1), any(Rol.class))).thenReturn(rol);

        mockMvc.perform(put("/api/v1/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ADMIN"));
    }

    @Test
    void testDeleteRol() throws Exception {
        doNothing().when(rolService).eliminarRol(1);

        mockMvc.perform(delete("/api/v1/roles/1"))
                .andExpect(status().isNoContent());

        verify(rolService, times(1)).eliminarRol(1);
    }
}
