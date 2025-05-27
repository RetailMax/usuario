package cap.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cap.usuario.model.Usuario;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


}
