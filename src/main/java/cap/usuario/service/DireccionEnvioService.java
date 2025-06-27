package cap.usuario.service;

import cap.usuario.model.DireccionEnvio;
import cap.usuario.repository.DireccionEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DireccionEnvioService {

    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;

    //Buscar direccion por id de usuario
    public List<DireccionEnvio> obtenerDireccionesPorUsuario(Integer idUsuario) {
        return direccionEnvioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    //Listar todas las direcciones
    public List<DireccionEnvio> obtenerTodasLasDirecciones() {
        return direccionEnvioRepository.findAll();
    }

    //Agregar Direccion
    public DireccionEnvio agregarDireccion(DireccionEnvio direccion) {
        return direccionEnvioRepository.save(direccion);
    }

    //Actualizar dirección
    public DireccionEnvio actualizarDireccion(Integer id, DireccionEnvio direccion) {
        direccion.setIdDireccion(id);
        return direccionEnvioRepository.save(direccion);
    }

    //Eliminar dirección
    public void eliminarDireccion(Integer id) {
        direccionEnvioRepository.deleteById(id);
    }
}
