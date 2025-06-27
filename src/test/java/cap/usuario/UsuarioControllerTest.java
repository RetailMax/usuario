package cap.usuario;

import cap.usuario.controller.UsuarioController;
import cap.usuario.model.Usuario;
import cap.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setPNombre("Juan");
        usuario.setAPaterno("Pérez");
        usuario.setCorreoElectronico("juan.perez@email.com");
        // ...otros campos...
    }

    @Test
    void testGetAllUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1))
                .andExpect(jsonPath("$[0].pNombre").value("Juan"))
                .andExpect(jsonPath("$[0].aPaterno").value("Pérez"))
                .andExpect(jsonPath("$[0].correoElectronico").value("juan.perez@email.com"));
    }

    @Test
    void testGetUsuarioById() throws Exception {
        when(usuarioService.obtenerPorId(1)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.pNombre").value("Juan"))
                .andExpect(jsonPath("$.aPaterno").value("Pérez"))
                .andExpect(jsonPath("$.correoElectronico").value("juan.perez@email.com"));
    }

    @Test
    void testCreateUsuario() throws Exception {
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.pNombre").value("Juan"))
                .andExpect(jsonPath("$.aPaterno").value("Pérez"))
                .andExpect(jsonPath("$.correoElectronico").value("juan.perez@email.com"));
    }

    @Test
    void testUpdateUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(eq(1), any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.correoElectronico").value("juan.perez@email.com"));
    }

    @Test
    void testDeleteUsuario() throws Exception {
        doNothing().when(usuarioService).eliminarUsuario(1);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).eliminarUsuario(1);
    }
}
