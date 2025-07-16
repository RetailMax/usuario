package cap.usuario.service;

import cap.usuario.model.Servicio;
import cap.usuario.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioService {

    @Autowired
    ServicioRepository servicioRepository;

    //Listar todos los servicios
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    //Buscar servicio por id
    public Servicio obtenerPorId(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    //Buscar servicio por nombre
    public Servicio obtenerPorNombre(String nombre) {
        Servicio servicio = servicioRepository.findByNombre(nombre);
        if (servicio == null) {
            throw new RuntimeException("Servicio no encontrado");
        }
        return servicio;
    }

    //Listar servicios habilitados
    public List<Servicio> listarServiciosHabilitados() {
        return servicioRepository.findByHabilitado(true);
    }

    //Crear servicio
    public Servicio crearServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    //Actualizar servicio
    public Servicio actualizarServicio(Integer id, Servicio servicio) {
        servicio.setId(id);
        return servicioRepository.save(servicio);
    }

    //Habilitar/Deshabilitar servicio
    public Servicio cambiarEstadoServicio(Integer id, Boolean habilitado) {
        Servicio servicio = obtenerPorId(id);
        servicio.setHabilitado(habilitado);
        return servicioRepository.save(servicio);
    }

    //Eliminar servicio
    public void eliminarServicio(Integer id) {
        servicioRepository.deleteById(id);
    }
}
