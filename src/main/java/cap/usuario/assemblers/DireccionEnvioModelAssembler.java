package cap.usuario.assemblers;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;

import org.springframework.stereotype.Component;

import cap.usuario.controller.DireccionEnvioController;
import cap.usuario.model.DireccionEnvio;

@Component
public class DireccionEnvioModelAssembler implements RepresentationModelAssembler<DireccionEnvio, EntityModel<DireccionEnvio>> {

    @Override
    @NonNull
    public EntityModel<DireccionEnvio> toModel(@NonNull DireccionEnvio direccionEnvio) {
        return EntityModel.of(direccionEnvio,
                linkTo(methodOn(DireccionEnvioController.class).buscarDireccion(direccionEnvio.getIdDireccion())).withSelfRel(),
                linkTo(methodOn(DireccionEnvioController.class).listarDirecciones()).withRel("direcciones"));
    }

}
