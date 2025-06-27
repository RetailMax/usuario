package cap.usuario;

import cap.usuario.model.Usuario;
import cap.usuario.repository.UsuarioRepository;
import cap.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setPNombre("Juan");
        usuario.setAPaterno("Pérez");
        usuario.setCorreoElectronico("juan.perez@email.com");
    }

    @Test
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        List<Usuario> usuarios = usuarioService.findAll();
        assertEquals(1, usuarios.size());
        assertEquals("Juan", usuarios.get(0).getPNombre());
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Usuario found = usuarioService.findById(1);
        assertNotNull(found);
        assertEquals("Juan", found.getPNombre());
    }

    @Test
    void testSave() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario saved = usuarioService.save(usuario);
        assertNotNull(saved);
        assertEquals("Juan", saved.getPNombre());
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(1);
        usuarioService.deleteById(1);
        verify(usuarioRepository, times(1)).deleteById(1);
    }
}