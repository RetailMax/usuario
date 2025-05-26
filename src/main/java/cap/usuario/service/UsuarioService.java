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

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // 👇 Este método permite guardar usuarios
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
