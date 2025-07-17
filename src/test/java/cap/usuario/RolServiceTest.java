package cap.usuario;

import cap.usuario.model.Rol;
import cap.usuario.repository.RolRepository;
import cap.usuario.service.RolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol rol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rol = new Rol();
        rol.setId(1); // CORRECTO según tu modelo
        rol.setNombre("ADMIN");
    }

    @Test
    void testListarRoles() {
        when(rolRepository.findAll()).thenReturn(List.of(rol));
        List<Rol> roles = rolService.listarRoles(); // CORREGIDO
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getNombre());
    }

    @Test
    void testBuscarRolPorId() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        Rol found = rolService.buscarRolPorId(1); // CORREGIDO
        assertNotNull(found);
        assertEquals("ADMIN", found.getNombre());
    }

    @Test
    void testGuardarRol() {
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);
        Rol saved = rolService.guardarRol(rol); // CORREGIDO
        assertNotNull(saved);
        assertEquals("ADMIN", saved.getNombre());
    }

    @Test
    void testEliminarRol() {
        doNothing().when(rolRepository).deleteById(1);
        when(rolRepository.existsById(1)).thenReturn(true);
        rolService.eliminarRol(1); // CORREGIDO
        verify(rolRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarRolNoExistente() {
        // Configurar mock para simular que el rol NO existe
        when(rolRepository.existsById(999)).thenReturn(false);
        
        // Ejecutar y verificar que se lanza la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rolService.eliminarRol(999);
        });
        
        assertEquals("Rol no encontrado", exception.getMessage());
        verify(rolRepository, times(1)).existsById(999);
        verify(rolRepository, never()).deleteById(999);
    }
}
