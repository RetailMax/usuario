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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@Valid @RequestBody Usuario nuevoUsuario) {
        // Validación manual para campos críticos
        if (nuevoUsuario.getRun() == null || nuevoUsuario.getRun().isEmpty()) {
            throw new IllegalArgumentException("El campo RUN es obligatorio");
        }
        if (nuevoUsuario.getCorreoElectronico() == null || nuevoUsuario.getCorreoElectronico().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        // Si es un Comprador, validar ciudad, region y comuna
        if (nuevoUsuario instanceof cap.usuario.model.Comprador comprador) {
            if (comprador.getCiudad() == null || comprador.getRegion() == null || comprador.getComuna() == null) {
                throw new IllegalArgumentException("Ciudad, región y comuna son obligatorios para un comprador");
            }
        }
        Usuario usuarioRegistrado = usuarioService.registrarUsuario(nuevoUsuario);
        return ResponseEntity
            .created(linkTo(methodOn(UsuarioController.class).mostrarUsuarioPorId(usuarioRegistrado.getIdUsuario())).toUri())
            .body(assembler.toModel(usuarioRegistrado));
    }

    //Actualizar usuario
    @PutMapping(value = "/perfil/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Usuario>> actualizarPerfil(
        @PathVariable Integer id,
        @Valid @RequestBody Usuario usuarioActualizado) {

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
}