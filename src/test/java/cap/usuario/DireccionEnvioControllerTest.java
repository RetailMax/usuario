package cap.usuario;

import cap.usuario.assemblers.DireccionEnvioModelAssembler;
import cap.usuario.controller.DireccionEnvioController;
import cap.usuario.model.DireccionEnvio;
import cap.usuario.model.Usuario;
import cap.usuario.service.DireccionEnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.ArgumentMatchers;

@WebMvcTest(DireccionEnvioController.class)
@DisplayName("DireccionEnvioController Tests")
public class DireccionEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionEnvioService direccionEnvioService;

    @MockitoBean
    private DireccionEnvioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private DireccionEnvio direccionEnvio;
    private DireccionEnvio direccionEnvio2;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setRun("12345678-9");
        usuario.setPNombre("Juan");
        usuario.setSNombre("Carlos");
        usuario.setAPaterno("Pérez");
        usuario.setAMaterno("González");
        usuario.setCorreoElectronico("juan.perez@email.com");
        usuario.setContrasenna("password123");
        usuario.setFechaNacimiento(java.sql.Date.valueOf("1990-01-01"));

        direccionEnvio = new DireccionEnvio();
        direccionEnvio.setIdDireccion(1);
        direccionEnvio.setDireccion("Calle Falsa 123");
        direccionEnvio.setCiudad("Santiago");
        direccionEnvio.setRegion("Metropolitana");
        direccionEnvio.setComuna("Maipú");
        direccionEnvio.setCodigoPostal(12345);
        direccionEnvio.setUsuario(usuario);

        direccionEnvio2 = new DireccionEnvio();
        direccionEnvio2.setIdDireccion(2);
        direccionEnvio2.setDireccion("Avenida Libertador 456");
        direccionEnvio2.setCiudad("Valparaíso");
        direccionEnvio2.setRegion("Valparaíso");
        direccionEnvio2.setComuna("Viña del Mar");
        direccionEnvio2.setCodigoPostal(54321);
        direccionEnvio2.setUsuario(usuario);
    }

    @Nested
    @DisplayName("GET /api/v1/direcciones-envio - Listar direcciones")
    class ListarDirecciones {

        @Test
        @DisplayName("Debe retornar todas las direcciones con éxito")
        void debeRetornarTodasLasDirecciones() throws Exception {
            // Given
            List<DireccionEnvio> direcciones = Arrays.asList(direccionEnvio, direccionEnvio2);
            when(direccionEnvioService.obtenerTodasLasDirecciones()).thenReturn(direcciones);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON))
                    .andExpect(header().string("Content-Type", containsString("application/hal+json")));

            verify(direccionEnvioService, times(1)).obtenerTodasLasDirecciones();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay direcciones")
        void debeRetornarListaVaciaCuandoNoHayDirecciones() throws Exception {
            // Given
            when(direccionEnvioService.obtenerTodasLasDirecciones()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON));

            verify(direccionEnvioService, times(1)).obtenerTodasLasDirecciones();
        }

        @Test
        @DisplayName("Debe manejar excepción del servicio")
        void debeManjarExcepcionDelServicio() throws Exception {
            // Given
            when(direccionEnvioService.obtenerTodasLasDirecciones())
                    .thenThrow(new RuntimeException("Error interno"));

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(direccionEnvioService, times(1)).obtenerTodasLasDirecciones();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/direcciones-envio/{id} - Buscar dirección por ID")
    class BuscarDireccionPorId {

        @Test
        @DisplayName("Debe retornar dirección existente por ID")
        void debeRetornarDireccionExistentePorId() throws Exception {
            // Given
            when(direccionEnvioService.obtenerDireccionPorId(1)).thenReturn(direccionEnvio);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON));

            verify(direccionEnvioService, times(1)).obtenerDireccionPorId(1);
        }

        @Test
        @DisplayName("Debe manejar ID inexistente")
        void debeManjarIdInexistente() throws Exception {
            // Given
            when(direccionEnvioService.obtenerDireccionPorId(999))
                    .thenThrow(new RuntimeException("Dirección no encontrada"));

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/999")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(direccionEnvioService, times(1)).obtenerDireccionPorId(999);
        }

        @Test
        @DisplayName("Debe manejar ID con formato inválido")
        void debeManjarIdConFormatoInvalido() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/abc")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(direccionEnvioService, never()).obtenerDireccionPorId(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/direcciones-envio - Crear dirección")
    class CrearDireccion {

        @Test
        @DisplayName("Debe crear dirección con datos válidos")
        void debeCrearDireccionConDatosValidos() throws Exception {
            // Given
            DireccionEnvio nuevaDireccion = new DireccionEnvio();
            nuevaDireccion.setDireccion("Nueva Calle 789");
            nuevaDireccion.setCiudad("Concepción");
            nuevaDireccion.setRegion("Biobío");
            nuevaDireccion.setComuna("Concepción");
            nuevaDireccion.setCodigoPostal(67890);
            nuevaDireccion.setUsuario(usuario);

            when(direccionEnvioService.agregarDireccion(ArgumentMatchers.any(DireccionEnvio.class)))
                    .thenReturn(direccionEnvio);

            // When & Then
            mockMvc.perform(post("/api/v1/direcciones-envio")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevaDireccion))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(content().contentType(MediaTypes.HAL_JSON));

            verify(direccionEnvioService, times(1)).agregarDireccion(ArgumentMatchers.any(DireccionEnvio.class));
        }

        @Test
        @DisplayName("Debe manejar JSON malformado")
        void debeManjarJsonMalformado() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/v1/direcciones-envio")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json}")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(direccionEnvioService, never()).agregarDireccion(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Debe manejar cuerpo vacío")
        void debeManjarCuerpoVacio() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/v1/direcciones-envio")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(direccionEnvioService, never()).agregarDireccion(ArgumentMatchers.any());
        }

        @Test
        @DisplayName("Debe manejar error en el servicio durante creación")
        void debeManjarErrorEnServicioDuranteCreacion() throws Exception {
            // Given
            when(direccionEnvioService.agregarDireccion(ArgumentMatchers.any(DireccionEnvio.class)))
                    .thenThrow(new RuntimeException("Error al guardar"));

            // When & Then
            mockMvc.perform(post("/api/v1/direcciones-envio")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(direccionEnvio))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(direccionEnvioService, times(1)).agregarDireccion(ArgumentMatchers.any(DireccionEnvio.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/direcciones-envio/{id} - Actualizar dirección")
    class ActualizarDireccion {

        @Test
        @DisplayName("Debe actualizar dirección existente")
        void debeActualizarDireccionExistente() throws Exception {
            // Given
            DireccionEnvio direccionActualizada = new DireccionEnvio();
            direccionActualizada.setIdDireccion(1);
            direccionActualizada.setDireccion("Calle Actualizada 123");
            direccionActualizada.setCiudad("Santiago");
            direccionActualizada.setRegion("Metropolitana");
            direccionActualizada.setComuna("Las Condes");
            direccionActualizada.setCodigoPostal(11111);
            direccionActualizada.setUsuario(usuario);

            when(direccionEnvioService.actualizarDireccion(ArgumentMatchers.eq(1), ArgumentMatchers.any(DireccionEnvio.class)))
                    .thenReturn(direccionActualizada);

            // When & Then
            mockMvc.perform(put("/api/v1/direcciones-envio/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(direccionActualizada))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON));

            verify(direccionEnvioService, times(1)).actualizarDireccion(ArgumentMatchers.eq(1), ArgumentMatchers.any(DireccionEnvio.class));
        }

        @Test
        @DisplayName("Debe manejar dirección inexistente para actualizar")
        void debeManjarDireccionInexistenteParaActualizar() throws Exception {
            // Given
            when(direccionEnvioService.actualizarDireccion(ArgumentMatchers.eq(999), ArgumentMatchers.any(DireccionEnvio.class)))
                    .thenThrow(new RuntimeException("Dirección no encontrada"));

            // When & Then
            mockMvc.perform(put("/api/v1/direcciones-envio/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(direccionEnvio))
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(direccionEnvioService, times(1)).actualizarDireccion(ArgumentMatchers.eq(999), ArgumentMatchers.any(DireccionEnvio.class));
        }

        @Test
        @DisplayName("Debe manejar datos inválidos en actualización")
        void debeManjarDatosInvalidosEnActualizacion() throws Exception {
            // When & Then
            mockMvc.perform(put("/api/v1/direcciones-envio/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk()); // El controller no valida, depende del servicio

            verify(direccionEnvioService, times(1)).actualizarDireccion(ArgumentMatchers.eq(1), ArgumentMatchers.any(DireccionEnvio.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/direcciones-envio/{id} - Eliminar dirección")
    class EliminarDireccion {

        @Test
        @DisplayName("Debe eliminar dirección existente")
        void debeEliminarDireccionExistente() throws Exception {
            // Given
            doNothing().when(direccionEnvioService).eliminarDireccion(1);

            // When & Then
            mockMvc.perform(delete("/api/v1/direcciones-envio/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(direccionEnvioService, times(1)).eliminarDireccion(1);
        }

        @Test
        @DisplayName("Debe manejar dirección inexistente para eliminar")
        void debeManjarDireccionInexistenteParaEliminar() throws Exception {
            // Given
            doThrow(new RuntimeException("Dirección no encontrada"))
                    .when(direccionEnvioService).eliminarDireccion(999);

            // When & Then
            mockMvc.perform(delete("/api/v1/direcciones-envio/999")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isInternalServerError());

            verify(direccionEnvioService, times(1)).eliminarDireccion(999);
        }

        @Test
        @DisplayName("Debe manejar ID inválido para eliminar")
        void debeManjarIdInvalidoParaEliminar() throws Exception {
            // When & Then
            mockMvc.perform(delete("/api/v1/direcciones-envio/abc")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isBadRequest());

            verify(direccionEnvioService, never()).eliminarDireccion(ArgumentMatchers.anyInt());
        }
    }

    @Nested
    @DisplayName("Tests de Content-Type y Headers")
    class ContentTypeYHeaders {

        @Test
        @DisplayName("Debe aceptar y producir HAL+JSON")
        void debeAceptarYProducirHalJson() throws Exception {
            // Given
            when(direccionEnvioService.obtenerDireccionPorId(1)).thenReturn(direccionEnvio);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON));
        }

        @Test
        @DisplayName("Debe manejar Accept header no soportado")
        void debeManjarAcceptHeaderNoSoportado() throws Exception {
            // Given
            when(direccionEnvioService.obtenerDireccionPorId(1)).thenReturn(direccionEnvio);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/1")
                            .accept(MediaType.APPLICATION_XML))
                    .andExpect(status().isNotAcceptable());
        }
    }

    @Nested
    @DisplayName("Tests de integración y edge cases")
    class IntegracionYEdgeCases {

        @Test
        @DisplayName("Debe manejar múltiples direcciones del mismo usuario")
        void debeManjarMultiplesDireccionesDelMismoUsuario() throws Exception {
            // Given
            List<DireccionEnvio> direccionesUsuario = Arrays.asList(direccionEnvio, direccionEnvio2);
            when(direccionEnvioService.obtenerTodasLasDirecciones()).thenReturn(direccionesUsuario);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk());

            verify(direccionEnvioService, times(1)).obtenerTodasLasDirecciones();
        }

        @Test
        @DisplayName("Debe validar que el assembler sea llamado correctamente")
        void debeValidarQueElAssemblerSeaLlamadoCorrectamente() throws Exception {
            // Given
            when(direccionEnvioService.obtenerDireccionPorId(1)).thenReturn(direccionEnvio);

            // When & Then
            mockMvc.perform(get("/api/v1/direcciones-envio/1")
                            .accept(MediaTypes.HAL_JSON))
                    .andExpect(status().isOk());

            verify(assembler, times(1)).toModel(direccionEnvio);
        }
    }
}