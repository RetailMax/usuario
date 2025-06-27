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
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    //Listar usuarios
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Usuario>> mostrarUsuarios() {
    List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(usuarios,
            linkTo(methodOn(UsuarioController.class).mostrarUsuarios()).withSelfRel());
    }

    //Buscar por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Usuario> mostrarUsuarioPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return assembler.toModel(usuario);
    }

    //Crear usuario
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);
        return ResponseEntity
            .created(linkTo(methodOn(UsuarioController.class).mostrarUsuarioPorId(usuarioRegistrado.getIdUsuario())).toUri())
            .body(assembler.toModel(usuarioRegistrado));
    }

    //Actualizar usuario
    @PutMapping(value = "/perfil/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarPerfil(
        @PathVariable Integer id,
        @RequestBody Usuario usuarioActualizado) {

            usuarioActualizado.setIdUsuario(id);
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity
                .ok(assembler.toModel(actualizado));
    }

    //Borrar usuario
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Usuario> asignarRoles(
            @PathVariable Integer id,
            @RequestBody List<Integer> idsRoles) {
        Usuario usuarioActualizado = usuarioService.asignarRoles(id, idsRoles);
        return ResponseEntity.ok(usuarioActualizado);
    }
}