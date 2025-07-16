package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cap.usuario.model.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    Optional<Usuario> findByRun(String run);
    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByRun(String run);
}
