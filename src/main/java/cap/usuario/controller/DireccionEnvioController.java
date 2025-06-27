package cap.usuario.controller;

import cap.usuario.assemblers.DireccionEnvioModelAssembler;
import cap.usuario.model.DireccionEnvio;
import cap.usuario.service.DireccionEnvioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/direcciones-envio")
public class DireccionEnvioController {

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @Autowired
    private DireccionEnvioModelAssembler assembler;

    //Listar direcciones de envio
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<DireccionEnvio>> listarDirecciones() {
        List<EntityModel<DireccionEnvio>> direcciones = direccionEnvioService.obtenerTodasLasDirecciones().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(direcciones,
                linkTo(methodOn(DireccionEnvioController.class).listarDirecciones()).withSelfRel());
    }

    //Buscar dirección de envio por ID
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<DireccionEnvio> buscarDireccion(@PathVariable Integer id) {
        DireccionEnvio direccion = direccionEnvioService.obtenerDireccionPorId(id);
        return assembler.toModel(direccion);
    }

    //Crear direccion de envio
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionEnvio>> agregarDireccion(@RequestBody DireccionEnvio direccion) {
        DireccionEnvio direccionCreada = direccionEnvioService.agregarDireccion(direccion);
        return ResponseEntity
                .created(linkTo(methodOn(DireccionEnvioController.class).buscarDireccion(direccionCreada.getIdDireccion())).toUri())
                .body(assembler.toModel(direccionCreada));
    }

    //Actualizar dirección de envio
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DireccionEnvio>> actualizarDireccion(@PathVariable Integer id, @RequestBody DireccionEnvio direccion) {
        direccion.setIdDireccion(id);
        DireccionEnvio direccionFinal = direccionEnvioService.actualizarDireccion(id, direccion);
        return ResponseEntity.ok(assembler.toModel(direccionFinal));
    }

    //Eliminar dirección de envio
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarDireccion(@PathVariable Integer id) {
        direccionEnvioService.eliminarDireccion(id);
        return ResponseEntity.noContent().build();
    }
}
