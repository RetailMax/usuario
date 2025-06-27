package cap.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cap.usuario.model.Usuario;
import cap.usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

//Listar usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

//Agregar Usuario
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

//Obtener usuario por ID
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

// Eliminar Usuario
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

//Actualizar Usuario

    public Usuario actualizarUsuario(Integer id, Usuario usuario) {
        Usuario u = obtenerPorId(id);
        u.setPNombre(usuario.getPNombre());
        u.setSNombre(usuario.getSNombre());
        u.setAPaterno(usuario.getAPaterno());
        u.setAMaterno(usuario.getAMaterno());
        u.setFechaNacimiento(usuario.getFechaNacimiento());
        u.setContrasenna(usuario.getContrasenna());
        u.setCorreoElectronico(usuario.getCorreoElectronico());
    
        return usuarioRepository.save(u);
    }

}
