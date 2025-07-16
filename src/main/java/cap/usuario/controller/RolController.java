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
    RolService rolService;

    @Autowired
    RolModelAssembler assembler;

    //Crear un nuevo rol
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> crearRol(@RequestBody Rol rol) {
        try {
            // Validar que el rol tenga los campos requeridos
            if (rol == null || rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Rol rolCreado = rolService.guardarRol(rol);
            return ResponseEntity
                .created(linkTo(methodOn(RolController.class).buscarRol(rolCreado.getId())).toUri())
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(rolCreado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Listar todos los roles
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Rol>>> listarRoles() {
        try {
            List<EntityModel<Rol>> roles = rolService.listarRoles().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(CollectionModel.of(roles,
                            linkTo(methodOn(RolController.class).listarRoles()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Buscar por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> buscarRol(@PathVariable Integer id) {
        try {
            Rol rol = rolService.buscarRolPorId(id);
            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(assembler.toModel(rol));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Actualizar un rol
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Rol>> actualizarRol(@PathVariable Integer id, @RequestBody Rol rolActualizado) {
        try {
            rolActualizado.setId(id);
            Rol rolFinal = rolService.actualizarRol(id, rolActualizado);
            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(assembler.toModel(rolFinal));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Eliminar un rol
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarRol(@PathVariable Integer id) {
        try {
            rolService.eliminarRol(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}