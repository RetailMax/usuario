package cap.usuario.controller;

import cap.usuario.assemblers.RolModelAssembler;
import cap.usuario.model.Rol;
import cap.usuario.service.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Autowired
    private RolModelAssembler assembler;

    //Crear un nuevo rol
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> crearRol(@RequestBody Rol rol) {
        Rol rolCreado = rolService.guardarRol(rol);
        return ResponseEntity
        .created(linkTo(methodOn(RolController.class).buscarRol(rolCreado.getId())).toUri())
        .body(assembler.toModel(rolCreado));
    }

    //Listar todos los roles
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Rol>> listarRoles() {
        List<EntityModel<Rol>> roles = rolService.listarRoles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(roles,
                linkTo(methodOn(RolController.class).listarRoles()).withSelfRel());
    }

    //Buscar por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Rol> buscarRol(@PathVariable Integer id) {
        Rol rol = rolService.buscarRolPorId(id);
        return assembler.toModel(rol);
    }

    //Actualizar un rol
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> actualizarRol(@PathVariable Integer id, @RequestBody Rol rolActualizado) {
        rolActualizado.setId(id);
        Rol rolFinal = rolService.actualizarRol(id, rolActualizado);
        return ResponseEntity.ok(assembler.toModel(rolFinal));
    }

    //Eliminar un rol
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarRol(@PathVariable Integer id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}