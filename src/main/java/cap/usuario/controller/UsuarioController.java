package cap.usuario.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cap.usuario.assemblers.UsuarioModelAssembler;
import cap.usuario.model.Usuario;
import cap.usuario.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioModelAssembler assembler;

    //Listar usuarios
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> mostrarUsuarios() {
        try {
            List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(CollectionModel.of(usuarios,
                            linkTo(methodOn(UsuarioController.class).mostrarUsuarios()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Buscar por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> mostrarUsuarioPorId(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(assembler.toModel(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Crear usuario
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        try {
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);
            return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).mostrarUsuarioPorId(usuarioRegistrado.getIdUsuario())).toUri())
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(usuarioRegistrado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Actualizar usuario
    @PutMapping(value = "/perfil/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarPerfil(
        @PathVariable Integer id,
        @RequestBody Usuario usuarioActualizado) {

        try {
            usuarioActualizado.setIdUsuario(id);
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Borrar usuario
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}