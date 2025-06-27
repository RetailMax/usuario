package cap.usuario;

import cap.usuario.model.*;
import cap.usuario.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Profile("dev")
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

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // Crear roles
        Rol compradorRol = new Rol();
        compradorRol.setNombre("COMPRADOR");

        Rol adminRol = new Rol();
        adminRol.setNombre("ADMIN");

        rolRepository.saveAll(List.of(compradorRol, adminRol));

        // Crear país
        Pais chile = new Pais(null, "Chile");
        paisRepository.save(chile);

        // Crear región
        Region region = new Region(null, "Región Metropolitana", chile);
        regionRepository.save(region);

        // Crear ciudad y comuna
        Ciudad ciudad = new Ciudad(null, "Santiago", region);
        ciudadRepository.save(ciudad);

        Comuna comuna = new Comuna(null, "Maipú", ciudad);
        comunaRepository.save(comuna);

        // Crear 10 compradores con direcciones
        for (int i = 0; i < 10; i++) {
            Comprador comprador = new Comprador();

            // Datos heredados desde Usuario
            comprador.setPNombre(faker.name().firstName());
            comprador.setSNombre(faker.name().firstName());
            comprador.setAPaterno(faker.name().lastName());
            comprador.setAMaterno(faker.name().lastName());

            LocalDate fecha = LocalDate.of(
                    1990 + random.nextInt(15),
                    1 + random.nextInt(12),
                    1 + random.nextInt(28));
            comprador.setFechaNacimiento(java.sql.Date.valueOf(fecha));

            comprador.setRun(String.valueOf(faker.number().numberBetween(10000000, 25000000)) + "-" + faker.number().digit());
            comprador.setContrasenna("123456");
            comprador.setRolUsuario(compradorRol);

            // Datos propios del Comprador
            comprador.setTelefono(faker.number().numberBetween(900000000, 999999999));
            comprador.setDireccion(faker.address().streetAddress());
            comprador.setRegion(region);
            comprador.setCiudad(ciudad);
            comprador.setComuna(comuna);
            comprador.setCodigoPostal(faker.number().numberBetween(1000, 9999));
            comprador.setCorreoElectronico(faker.internet().emailAddress());

            // Guardar comprador (también guarda como Usuario por herencia)
            compradorRepository.save(comprador);

            // Crear dirección asociada al comprador
            DireccionEnvio direccion = new DireccionEnvio();
            direccion.setDireccion(faker.address().streetAddress());
            direccion.setCiudad(ciudad.getNombre());
            direccion.setRegion(region.getNombre());
            direccion.setComuna(comuna.getNombre());
            direccion.setCodigoPostal(faker.number().numberBetween(1000, 9999));
            direccion.setUsuario(comprador); // comprador ya es un Usuario
            direccionEnvioRepository.save(direccion);
        }
    }
}
