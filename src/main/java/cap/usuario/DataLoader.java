package cap.usuario;

import cap.usuario.model.*;
import cap.usuario.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CompradorRepository compradorRepository;
    @Autowired
    private PaisRepository paisRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private ComunaRepository comunaRepository;
    @Autowired
    private DireccionEnvioRepository direccionEnvioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Comentado temporalmente para evitar errores en Docker
        // TODO: Habilitar cuando se resuelvan los problemas de base de datos
        /*
        // SOLO PARA DESARROLLO: borrar todos los datos antes de cargar nuevos
        direccionEnvioRepository.deleteAll();
        compradorRepository.deleteAll();
        usuarioRepository.deleteAll();
        comunaRepository.deleteAll();
        ciudadRepository.deleteAll();
        regionRepository.deleteAll();
        paisRepository.deleteAll();
        rolRepository.deleteAll();

        // Crear roles
        Rol rolComprador = new Rol();
        rolComprador.setNombre("COMPRADOR");
        rolRepository.save(rolComprador);

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");
        rolRepository.save(rolAdmin);

        // Crear país
        Pais chile = new Pais();
        chile.setNombre("Chile");
        paisRepository.save(chile);

        // Crear región
        Region region = new Region();
        region.setNombre("Región Metropolitana");
        region.setPais(chile);
        regionRepository.save(region);

        // Crear ciudad y comuna
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre("Santiago");
        ciudad.setRegion(region);
        ciudad.setCodigoPostal(String.valueOf(faker.number().numberBetween(1000, 9999)));
        ciudadRepository.save(ciudad);

        Comuna comuna = new Comuna();
        comuna.setNombre("Maipú");
        comuna.setCiudad(ciudad);
        comunaRepository.save(comuna);

        // Crear usuario administrador (no comprador)
        Comprador adminUser = new Comprador();
        adminUser.setPNombre("Admin");
        adminUser.setSNombre("User");
        adminUser.setAPaterno("Administrador");
        adminUser.setAMaterno("Sistema");
        adminUser.setFechaNacimiento(new java.sql.Date(new java.util.Date().getTime()));
        // Generar run y correo únicos para evitar duplicados
        adminUser.setRun("10000000-" + faker.number().digit());
        adminUser.setContrasenna("admin123");
        adminUser.setCorreoElectronico("admin" + faker.number().numberBetween(1000, 9999) + "@sistema.com");
        adminUser.setRolUsuario(rolAdmin);
        adminUser.setTelefono("+569" + faker.number().numberBetween(10000000, 99999999));
        adminUser.setDireccion("Calle Admin 123");
        adminUser.setRegion(region);
        adminUser.setCiudad(ciudad);
        adminUser.setComuna(comuna);
        adminUser.setCodigoPostal(faker.number().numberBetween(1000, 9999));
        compradorRepository.save(adminUser);

        // Crear compradores de prueba
        for (int i = 0; i < 10; i++) {
            Comprador comprador = new Comprador();
            comprador.setPNombre(faker.name().firstName());
            comprador.setSNombre(faker.name().firstName());
            comprador.setAPaterno(faker.name().lastName());
            comprador.setAMaterno(faker.name().lastName());
            comprador.setFechaNacimiento(new java.sql.Date(faker.date().birthday().getTime()));
            comprador.setRun(faker.number().numberBetween(10000000, 99999999) + "-" + faker.number().digit());
            comprador.setContrasenna("password123");
            comprador.setCorreoElectronico(faker.internet().emailAddress());
            comprador.setRolUsuario(rolComprador);
            comprador.setTelefono("+569" + faker.number().numberBetween(10000000, 99999999));
            comprador.setDireccion(faker.address().streetAddress());
            comprador.setRegion(region);
            comprador.setCiudad(ciudad);
            comprador.setComuna(comuna);
            comprador.setCodigoPostal(faker.number().numberBetween(1000, 9999));
            compradorRepository.save(comprador);

            // Crear dirección de envío para cada comprador
            DireccionEnvio direccion = new DireccionEnvio();
            direccion.setDireccion(faker.address().streetAddress());
            direccion.setCiudad(ciudad.getNombre());
            direccion.setRegion(region.getNombre());
            direccion.setComuna(comuna.getNombre());
            direccion.setCodigoPostal(faker.number().numberBetween(1000, 9999));
            direccion.setUsuario(comprador);
            direccionEnvioRepository.save(direccion);
        }
        System.out.println("DataLoader: Datos de desarrollo cargados correctamente.");
        */
        System.out.println("DataLoader deshabilitado temporalmente");
    }
}
