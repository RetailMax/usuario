package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cap.usuario.model.DireccionEnvio;
import java.util.List;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Integer> {
    List<DireccionEnvio> findByUsuarioIdUsuario(Integer idUsuario);
}
