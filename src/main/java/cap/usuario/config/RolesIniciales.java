package cap.usuario.config;

import cap.usuario.model.Rol;
import cap.usuario.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolesIniciales implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen roles
        if (rolRepository.count() == 0) {
            // Crear roles según HU-11
            crearRolSiNoExiste("ADMINISTRADOR_GENERAL");
            crearRolSiNoExiste("ENCARGADO_INVENTARIO");
            crearRolSiNoExiste("ADMINISTRADOR_PRODUCTOS");
            crearRolSiNoExiste("ANALISTA_NEGOCIO");
            crearRolSiNoExiste("ADMINISTRADOR_PAGOS");
            crearRolSiNoExiste("ADMINISTRADOR_ORDENES");
            crearRolSiNoExiste("COMPRADOR");
            crearRolSiNoExiste("VENDEDOR");
        }
    }

    private void crearRolSiNoExiste(String nombreRol) {
        Rol rol = new Rol();
        rol.setNombre(nombreRol);
        rolRepository.save(rol);
    }
}
