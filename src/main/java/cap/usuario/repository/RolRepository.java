package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cap.usuario.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
}
