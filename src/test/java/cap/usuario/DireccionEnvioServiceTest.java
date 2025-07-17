package cap.usuario;

import cap.usuario.model.DireccionEnvio;
import cap.usuario.repository.DireccionEnvioRepository;
import cap.usuario.service.DireccionEnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DireccionEnvioServiceTest {

    @Mock
    private DireccionEnvioRepository direccionEnvioRepository;

    @InjectMocks
    private DireccionEnvioService direccionEnvioService;

    private DireccionEnvio direccionEnvio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        direccionEnvio = new DireccionEnvio();
        direccionEnvio.setIdDireccion(1);
        direccionEnvio.setDireccion("Calle Falsa 123");
        direccionEnvio.setCiudad("Santiago");
        direccionEnvio.setRegion("Metropolitana");
        direccionEnvio.setComuna("Maipú");
        direccionEnvio.setCodigoPostal(12345); // <-- CORREGIDO
    }

    @Test
    void testFindAll() {
        when(direccionEnvioRepository.findAll()).thenReturn(List.of(direccionEnvio));
        List<DireccionEnvio> direcciones = direccionEnvioService.obtenerTodasLasDirecciones(); // <-- CORREGIDO
        assertEquals(1, direcciones.size());
        assertEquals("Calle Falsa 123", direcciones.get(0).getDireccion());
    }

    @Test
    void testObtenerPorId() {
        when(direccionEnvioRepository.findById(1)).thenReturn(Optional.of(direccionEnvio));
        DireccionEnvio found = direccionEnvioService.obtenerDireccionPorId(1); // <-- CORREGIDO
        assertNotNull(found);
        assertEquals("Calle Falsa 123", found.getDireccion());
    }

    @Test
    void testRegistrarDireccion() {
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenReturn(direccionEnvio);
        DireccionEnvio saved = direccionEnvioService.agregarDireccion(direccionEnvio); // <-- CORREGIDO
        assertNotNull(saved);
        assertEquals("Calle Falsa 123", saved.getDireccion());
    }

    @Test
    void testEliminarDireccion() {
        doNothing().when(direccionEnvioRepository).deleteById(1);
        when(direccionEnvioRepository.existsById(1)).thenReturn(true);
        direccionEnvioService.eliminarDireccion(1);
        verify(direccionEnvioRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarDireccionNoExistente() {
        // Configurar mock para simular que la dirección NO existe
        when(direccionEnvioRepository.existsById(999)).thenReturn(false);
        
        // Ejecutar y verificar que se lanza la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            direccionEnvioService.eliminarDireccion(999);
        });
        
        assertEquals("Dirección no encontrada", exception.getMessage());
        verify(direccionEnvioRepository, times(1)).existsById(999);
        verify(direccionEnvioRepository, never()).deleteById(999);
    }

    @Test
    void testActualizarDireccion() {
        DireccionEnvio nuevaDireccion = new DireccionEnvio();
        nuevaDireccion.setDireccion("Avenida Siempre Viva 742");
        nuevaDireccion.setCiudad("Santiago");
        nuevaDireccion.setRegion("Metropolitana");
        nuevaDireccion.setComuna("Maipú");
        nuevaDireccion.setCodigoPostal(54321); // <-- CORREGIDO

        when(direccionEnvioRepository.findById(1)).thenReturn(Optional.of(direccionEnvio));
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenReturn(nuevaDireccion);

        DireccionEnvio actualizada = direccionEnvioService.actualizarDireccion(1, nuevaDireccion);

        assertNotNull(actualizada);
        assertEquals("Avenida Siempre Viva 742", actualizada.getDireccion());
    }
}
