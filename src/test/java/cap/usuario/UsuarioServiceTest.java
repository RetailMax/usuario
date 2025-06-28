package cap.usuario;

import cap.usuario.model.Usuario;
import cap.usuario.model.Rol;
import cap.usuario.repository.UsuarioRepository;
import cap.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.DisplayName;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;
    
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        // Crear rol de prueba
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("ADMIN");
        
        // Crear usuario de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setRun("12345678-9");
        usuario.setPNombre("Juan");
        usuario.setSNombre("Carlos");
        usuario.setAPaterno("Pérez");
        usuario.setAMaterno("González");
        usuario.setFechaNacimiento(Date.valueOf("1990-01-01"));
        usuario.setRolUsuario(rol);
        usuario.setContrasenna("password123");
        usuario.setCorreoElectronico("juan.perez@email.com");
    }

    // ========== TEST DIAGNÓSTICO ==========
    @Test
    @DisplayName("DEBUG - Verificar que el servicio se está ejecutando")
    void testServicioSeEjecuta() {
        // Verificar que el servicio no es null
        assertNotNull(usuarioService, "El servicio debe estar inyectado");
        
        // Verificar que es una instancia real, no un mock
        assertFalse(usuarioService.getClass().getName().contains("Mock"), 
                   "El servicio no debe ser un mock");
        
        System.out.println("Clase del servicio: " + usuarioService.getClass().getName());
    }

    // ========== TESTS BÁSICOS SIN MOCKS COMPLEJOS ==========
    @Test
    @DisplayName("findAll - Test básico")
    void testFindAll() {
        // Configurar mock simple
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Ejecutar método del servicio
        List<Usuario> result = usuarioService.findAll();

        // Verificaciones
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verificar que el mock se llamó
        verify(usuarioRepository, times(1)).findAll();
        
        System.out.println("findAll ejecutado correctamente");
    }

    @Test
    @DisplayName("registrarUsuario - Test básico")
    void testRegistrarUsuario() {
        // Configurar mock
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar
        Usuario result = usuarioService.registrarUsuario(usuario);

        // Verificar
        assertNotNull(result);
        assertEquals("Juan", result.getPNombre());
        verify(usuarioRepository, times(1)).save(usuario);
        
        System.out.println("registrarUsuario ejecutado correctamente");
    }

    @Test
    @DisplayName("obtenerPorId - Caso exitoso")
    void testObtenerPorId_Exitoso() {
        // Configurar mock
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Ejecutar
        Usuario result = usuarioService.obtenerPorId(1);

        // Verificar
        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getIdUsuario());
        verify(usuarioRepository, times(1)).findById(1);
        
        System.out.println("obtenerPorId (exitoso) ejecutado correctamente");
    }

    @Test
    @DisplayName("obtenerPorId - Usuario no encontrado")
    void testObtenerPorId_NoEncontrado() {
        // Configurar mock
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(999);
        });
        
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999);
        
        System.out.println("obtenerPorId (no encontrado) ejecutado correctamente");
    }

    @Test
    @DisplayName("eliminarUsuario - Caso exitoso")
    void testEliminarUsuario_Exitoso() {
        // Configurar mocks
        when(usuarioRepository.existsById(1)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1);

        // Ejecutar
        usuarioService.eliminarUsuario(1);

        // Verificar
        verify(usuarioRepository, times(1)).existsById(1);
        verify(usuarioRepository, times(1)).deleteById(1);
        
        System.out.println("eliminarUsuario (exitoso) ejecutado correctamente");
    }

    @Test
    @DisplayName("eliminarUsuario - Usuario no encontrado")
    void testEliminarUsuario_NoEncontrado() {
        // Configurar mock
        when(usuarioRepository.existsById(999)).thenReturn(false);

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(999);
        });
        
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(999);
        verify(usuarioRepository, never()).deleteById(999);
        
        System.out.println("eliminarUsuario (no encontrado) ejecutado correctamente");
    }

    @Test
    @DisplayName("actualizarUsuario - Caso exitoso")
    void testActualizarUsuario_Exitoso() {
        // Preparar datos
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setPNombre("Pedro");
        usuarioActualizado.setSNombre("Luis");
        usuarioActualizado.setAPaterno("García");
        usuarioActualizado.setAMaterno("Rodríguez");
        usuarioActualizado.setFechaNacimiento(Date.valueOf("1985-05-15"));
        usuarioActualizado.setContrasenna("newpassword456");
        usuarioActualizado.setCorreoElectronico("pedro.garcia@email.com");

        // Configurar mocks
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Ejecutar
        Usuario result = usuarioService.actualizarUsuario(1, usuarioActualizado);

        // Verificar
        assertNotNull(result);
        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).save(usuario);
        
        System.out.println("actualizarUsuario (exitoso) ejecutado correctamente");
    }

    @Test
    @DisplayName("actualizarUsuario - Usuario no encontrado")
    void testActualizarUsuario_NoEncontrado() {
        // Preparar datos
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setPNombre("Pedro");
        
        // Configurar mock
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario(999, usuarioActualizado);
        });
        
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(999);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        
        System.out.println("actualizarUsuario (no encontrado) ejecutado correctamente");
    }

    // ========== TESTS ADICIONALES PARA COBERTURA COMPLETA ==========
    
    @Test
    @DisplayName("findAll - Lista vacía")
    void testFindAll_ListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Usuario> result = usuarioService.findAll();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("findAll - Múltiples usuarios")
    void testFindAll_MultipleUsuarios() {
        Usuario usuario2 = new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setRun("98765432-1");
        usuario2.setPNombre("María");
        usuario2.setSNombre("Elena");
        usuario2.setAPaterno("García");
        usuario2.setAMaterno("López");
        usuario2.setFechaNacimiento(Date.valueOf("1985-05-15"));
        usuario2.setRolUsuario(rol);
        usuario2.setContrasenna("admin456");
        usuario2.setCorreoElectronico("maria.garcia@email.com");
        
        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        
        List<Usuario> result = usuarioService.findAll();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("registrarUsuario - Verificar todos los campos")
    void testRegistrarUsuario_TodosLosCampos() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.registrarUsuario(usuario);

        assertNotNull(result);
        assertEquals("Juan", result.getPNombre());
        assertEquals("Carlos", result.getSNombre());
        assertEquals("Pérez", result.getAPaterno());
        assertEquals("González", result.getAMaterno());
        assertEquals("12345678-9", result.getRun());
        assertEquals("juan.perez@email.com", result.getCorreoElectronico());
        assertEquals("password123", result.getContrasenna());
        assertNotNull(result.getRolUsuario());
        
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("actualizarUsuario - Verificar actualización de campos")
    void testActualizarUsuario_VerificarCampos() {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setPNombre("NuevoNombre");
        usuarioActualizado.setSNombre("NuevoSegundo");
        usuarioActualizado.setAPaterno("NuevoPaterno");
        usuarioActualizado.setAMaterno("NuevoMaterno");
        usuarioActualizado.setFechaNacimiento(Date.valueOf("1995-12-31"));
        usuarioActualizado.setContrasenna("nuevacontrasenna");
        usuarioActualizado.setCorreoElectronico("nuevo@correo.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.actualizarUsuario(1, usuarioActualizado);

        // Verificar que los campos del usuario original se actualizaron
        assertEquals("NuevoNombre", usuario.getPNombre());
        assertEquals("NuevoSegundo", usuario.getSNombre());
        assertEquals("NuevoPaterno", usuario.getAPaterno());
        assertEquals("NuevoMaterno", usuario.getAMaterno());
        assertEquals("nuevacontrasenna", usuario.getContrasenna());
        assertEquals("nuevo@correo.com", usuario.getCorreoElectronico());
        
        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).save(usuario);
    }
}