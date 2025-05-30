package cap.usuario.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cap.usuario.model.Usuario;
import cap.usuario.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> mostrarUsuarios() {
        return usuarioService.findAll();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario nuevoUsuario) {
        return usuarioService.registrarUsuario(nuevoUsuario);
    }

    @PutMapping("/perfil/{id}")
    public ResponseEntity<Usuario> actualizarPerfil(
        @PathVariable Integer id,
        @RequestBody Usuario usuarioActualizado) {

    Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
    return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

}

