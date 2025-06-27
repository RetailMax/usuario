package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cap.usuario.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{
}
