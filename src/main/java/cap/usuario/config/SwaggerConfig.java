package cap.usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration // Configuration: Indica que esta clase contiene definiciones de beans para el contexto de Spring.
public class SwaggerConfig {

    @Bean //Bean: Marca el método openAPI() como un bean que será gestionado por Spring.
    public OpenAPI openAPI() {
        return new OpenAPI() //Creas un formulario en blanco
                .info(new Info() // Agregas la sección "Datos Básicos"
                        .title("Capacitación API") // Rellenas el campo "Título
                        .version("1.0.0")//Rellenas el campo "Versión"
                        .description("API para la gestión de usuarios en la capacitación"));
    }                     //Rellenas el campo "Descripción

}
