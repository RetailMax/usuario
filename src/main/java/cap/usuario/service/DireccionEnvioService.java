package cap.usuario.service;

import cap.usuario.model.DireccionEnvio;
import cap.usuario.repository.DireccionEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DireccionEnvioService {

    @Autowired
    DireccionEnvioRepository direccionEnvioRepository;

    //Buscar direccion por id
    public DireccionEnvio obtenerDireccionPorId(Integer idDireccion) {
        return direccionEnvioRepository.findById(idDireccion)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
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
        // Verificar que la dirección existe
        DireccionEnvio direccionExistente = obtenerDireccionPorId(id);
        
        // Actualizar campos
        direccionExistente.setDireccion(direccion.getDireccion());
        direccionExistente.setCiudad(direccion.getCiudad());
        direccionExistente.setRegion(direccion.getRegion());
        direccionExistente.setComuna(direccion.getComuna());
        direccionExistente.setCodigoPostal(direccion.getCodigoPostal());
        direccionExistente.setUsuario(direccion.getUsuario());
        
        return direccionEnvioRepository.save(direccionExistente);
    }

    //Eliminar dirección
    public void eliminarDireccion(Integer id) {
        // Verificar que la dirección existe antes de eliminar
        if (!direccionEnvioRepository.existsById(id)) {
            throw new RuntimeException("Dirección no encontrada");
        }
        direccionEnvioRepository.deleteById(id);
    }
}
