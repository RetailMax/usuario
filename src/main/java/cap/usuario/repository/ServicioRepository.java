package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cap.usuario.model.Servicio;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByHabilitado(Boolean habilitado);
    Servicio findByNombre(String nombre);
}
