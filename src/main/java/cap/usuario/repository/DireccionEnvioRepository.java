package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cap.usuario.model.DireccionEnvio;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Integer> {
    //DireccionEnvio findByUsuarioIdUsuario(Integer idUsuario);
}
