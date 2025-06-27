package cap.usuario;

import cap.usuario.controller.DireccionEnvioController;
import cap.usuario.model.DireccionEnvio;
import cap.usuario.service.DireccionEnvioService;
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

@WebMvcTest(DireccionEnvioController.class)
public class DireccionEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DireccionEnvioService direccionEnvioService;

    @Autowired
    private ObjectMapper objectMapper;

    private DireccionEnvio direccionEnvio;

    @BeforeEach
    void setUp() {
        direccionEnvio = new DireccionEnvio();
        direccionEnvio.setIdDireccion(1);
        direccionEnvio.setDireccion("Calle Falsa 123");
        direccionEnvio.setCiudad("Santiago");
        direccionEnvio.setRegion("Metropolitana");
        direccionEnvio.setComuna("Maipú");
        direccionEnvio.setCodigoPostal(12345);
        // ...otros campos si tienes...
    }

    @Test
    void testGetAllDirecciones() throws Exception {
        when(direccionEnvioService.obtenerTodasLasDirecciones()).thenReturn(List.of(direccionEnvio));

        mockMvc.perform(get("/api/v1/direcciones-envio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.direccionEnvioList[0].idDireccion").value(1))
                .andExpect(jsonPath("$._embedded.direccionEnvioList[0].direccion").value("Calle Falsa 123"));
    }

    @Test
    void testGetDireccionById() throws Exception {
        when(direccionEnvioService.obtenerDireccionPorId(1)).thenReturn(direccionEnvio);

        mockMvc.perform(get("/api/v1/direcciones-envio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDireccion").value(1))
                .andExpect(jsonPath("$.direccion").value("Calle Falsa 123"));
    }

    @Test
    void testCrearDireccion() throws Exception {
        when(direccionEnvioService.agregarDireccion(any(DireccionEnvio.class))).thenReturn(direccionEnvio);

        mockMvc.perform(post("/api/v1/direcciones-envio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(direccionEnvio)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDireccion").value(1))
                .andExpect(jsonPath("$.direccion").value("Calle Falsa 123"));
    }

    @Test
    void testUpdateDireccion() throws Exception {
        when(direccionEnvioService.actualizarDireccion(eq(1), any(DireccionEnvio.class))).thenReturn(direccionEnvio);

        mockMvc.perform(put("/api/direcciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(direccionEnvio)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDireccion").value(1));
    }

    @Test
    void testDeleteDireccion() throws Exception {
        doNothing().when(direccionEnvioService).eliminarDireccion(1);

        mockMvc.perform(delete("/api/direcciones/1"))
                .andExpect(status().isOk());

        verify(direccionEnvioService, times(1)).eliminarDireccion(1);
    }
}
