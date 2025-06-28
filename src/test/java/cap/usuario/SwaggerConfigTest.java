package cap.usuario;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cap.usuario.config.SwaggerConfig;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitarios para SwaggerConfig")
class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    @Test
    @DisplayName("Debería crear OpenAPI con configuración correcta")
    void testCrearOpenAPIConConfiguracion() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        
        // Then
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
    }

    @Test
    @DisplayName("Debería configurar título correctamente")
    void testConfigurarTitulo() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        Info info = openAPI.getInfo();
        
        // Then
        assertThat(info.getTitle()).isEqualTo("Capacitación API");
    }

    @Test
    @DisplayName("Debería configurar versión correctamente")
    void testConfigurarVersion() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        Info info = openAPI.getInfo();
        
        // Then
        assertThat(info.getVersion()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("Debería configurar descripción correctamente")
    void testConfigurarDescripcion() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        Info info = openAPI.getInfo();
        
        // Then
        assertThat(info.getDescription()).isEqualTo("API para la gestión de usuarios en la capacitación");
    }

    @Test
    @DisplayName("Debería crear nuevas instancias en cada llamada")
    void testCrearNuevasInstancias() {
        // When
        OpenAPI openAPI1 = swaggerConfig.openAPI();
        OpenAPI openAPI2 = swaggerConfig.openAPI();
        
        // Then
        assertThat(openAPI1).isNotNull();
        assertThat(openAPI2).isNotNull();
        assertThat(openAPI1).isNotSameAs(openAPI2); // Diferentes instancias
        
        // Pero con la misma configuración
        assertThat(openAPI1.getInfo().getTitle()).isEqualTo(openAPI2.getInfo().getTitle());
        assertThat(openAPI1.getInfo().getVersion()).isEqualTo(openAPI2.getInfo().getVersion());
        assertThat(openAPI1.getInfo().getDescription()).isEqualTo(openAPI2.getInfo().getDescription());
    }

    @Test
    @DisplayName("Debería verificar que Info no sea null")
    void testInfoNoEsNull() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        
        // Then
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isNotNull();
        assertThat(openAPI.getInfo().getVersion()).isNotNull();
        assertThat(openAPI.getInfo().getDescription()).isNotNull();
    }

    @Test
    @DisplayName("Debería verificar estructura completa del objeto")
    void testEstructuraCompletaObjeto() {
        // When
        OpenAPI openAPI = swaggerConfig.openAPI();
        
        // Then
        assertThat(openAPI)
            .isNotNull()
            .extracting(OpenAPI::getInfo)
            .isNotNull();
            
        Info info = openAPI.getInfo();
        assertThat(info.getTitle()).contains("Capacitación");
        assertThat(info.getVersion()).matches("\\d+\\.\\d+\\.\\d+");
        assertThat(info.getDescription()).contains("gestión");
        assertThat(info.getDescription()).contains("usuarios");
    }
}