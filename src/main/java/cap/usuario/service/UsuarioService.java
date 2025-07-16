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
    UsuarioRepository usuarioRepository;

//Listar usuarios
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

//Agregar Usuario
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar que no exista otro usuario con el mismo correo
        if (usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            throw new RuntimeException("Ya existe un usuario con este correo electrónico");
        }
        
        // Validar que no exista otro usuario con el mismo RUN
        if (usuarioRepository.existsByRun(usuario.getRun())) {
            throw new RuntimeException("Ya existe un usuario con este RUN");
        }
        
        return usuarioRepository.save(usuario);
    }

    //Buscar usuario por correo electrónico
    public Usuario buscarPorCorreo(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    //Buscar usuario por RUN
    public Usuario buscarPorRun(String run) {
        return usuarioRepository.findByRun(run)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
        u.setTelefono(usuario.getTelefono());
        u.setDireccionPrincipal(usuario.getDireccionPrincipal());
        u.setComuna(usuario.getComuna());
        u.setCiudad(usuario.getCiudad());
        u.setRegion(usuario.getRegion());
    
        return usuarioRepository.save(u);
    }

}
