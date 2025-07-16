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

import cap.usuario.assemblers.ServicioModelAssembler;
import cap.usuario.model.Servicio;
import cap.usuario.service.ServicioService;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    @Autowired
    ServicioService servicioService;

    @Autowired
    ServicioModelAssembler assembler;

    //Listar servicios
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Servicio>>> listarServicios() {
        try {
            List<EntityModel<Servicio>> servicios = servicioService.listarServicios().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(CollectionModel.of(servicios,
                            linkTo(methodOn(ServicioController.class).listarServicios()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Listar servicios habilitados
    @GetMapping(value = "/habilitados", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Servicio>>> listarServiciosHabilitados() {
        try {
            List<EntityModel<Servicio>> servicios = servicioService.listarServiciosHabilitados().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(CollectionModel.of(servicios,
                            linkTo(methodOn(ServicioController.class).listarServiciosHabilitados()).withSelfRel()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Buscar por id
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> buscarServicio(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.obtenerPorId(id);
            return ResponseEntity.ok()
                    .contentType(MediaTypes.HAL_JSON)
                    .body(assembler.toModel(servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Crear servicio
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> crearServicio(@RequestBody Servicio nuevoServicio) {
        try {
            // Validar que el servicio tenga los campos requeridos
            if (nuevoServicio == null || nuevoServicio.getNombre() == null || 
                nuevoServicio.getNombre().trim().isEmpty() ||
                nuevoServicio.getDescripcion() == null || 
                nuevoServicio.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Servicio servicioCreado = servicioService.crearServicio(nuevoServicio);
            return ResponseEntity
                .created(linkTo(methodOn(ServicioController.class).buscarServicio(servicioCreado.getId())).toUri())
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(servicioCreado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Actualizar servicio
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> actualizarServicio(
        @PathVariable Integer id,
        @RequestBody Servicio servicioActualizado) {

        try {
            servicioActualizado.setId(id);
            Servicio actualizado = servicioService.actualizarServicio(id, servicioActualizado);
            return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(actualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Habilitar/Deshabilitar servicio
    @PatchMapping(value = "/{id}/estado", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Servicio>> cambiarEstadoServicio(
        @PathVariable Integer id,
        @RequestParam Boolean habilitado) {

        try {
            Servicio servicioActualizado = servicioService.cambiarEstadoServicio(id, habilitado);
            return ResponseEntity
                .ok()
                .contentType(MediaTypes.HAL_JSON)
                .body(assembler.toModel(servicioActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Eliminar servicio
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarServicio(@PathVariable Integer id) {
        try {
            servicioService.eliminarServicio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
