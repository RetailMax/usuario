package cap.usuario.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import cap.usuario.controller.ServicioController;
import cap.usuario.model.Servicio;

@Component
public class ServicioModelAssembler implements RepresentationModelAssembler<Servicio, EntityModel<Servicio>>{

    @Override
    @NonNull
    public EntityModel<Servicio> toModel(@NonNull Servicio servicio) {
        return EntityModel.of(servicio,
                linkTo(methodOn(ServicioController.class).buscarServicio(servicio.getId())).withSelfRel(),
                linkTo(methodOn(ServicioController.class).listarServicios()).withRel("servicios"));
    }
}
