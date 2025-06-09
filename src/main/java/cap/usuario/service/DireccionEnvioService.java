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

    public List<DireccionEnvio> obtenerDireccionesPorUsuario(Integer idUsuario) {
        return direccionEnvioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public DireccionEnvio agregarDireccion(DireccionEnvio direccion) {
        return direccionEnvioRepository.save(direccion);
    }

    public DireccionEnvio actualizarDireccion(Integer id, DireccionEnvio direccion) {
        direccion.setIdDireccion(id);
        return direccionEnvioRepository.save(direccion);
    }

    public void eliminarDireccion(Integer id) {
        direccionEnvioRepository.deleteById(id);
    }
}
