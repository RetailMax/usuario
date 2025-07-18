package cap.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import cap.usuario.assemblers.UsuarioModelAssembler;
import cap.usuario.controller.UsuarioController;
import cap.usuario.model.Usuario;

@ExtendWith(MockitoExtension.class) //Habilita poderes de Mockito en esta clase de pruebas
//Permite usar Mock (objetos falsos) y InjectMocks (inyección automática).
//Prepara el entorno para probar UsuarioModelAssembler con dependencias falsas controlables
class UsuarioModelAssemblerTest {

    @InjectMocks //Es una anotación de Mockito que inyecta automáticamente los objetos falsos
    //(Mock) en la clase que estás probando.
    private UsuarioModelAssembler usuarioModelAssembler;

    private Usuario usuario;

    @BeforeEach //Código que se ejecuta antes de CADA Test
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setRun("12345678-9");
        usuario.setPNombre("Juan");
        usuario.setSNombre("Carlos");
        usuario.setAPaterno("Pérez");
        usuario.setAMaterno("González");
        usuario.setCorreoElectronico("juan.perez@email.com");
        usuario.setContrasenna("password123");
        // fechaNacimiento y rolUsuario se pueden dejar null para las pruebas básicas
    }

    @Test
    void testToModel_DeberiaCrearEntityModelConLinksCorrectos() {
        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuario);

        // Then
        assertNotNull(entityModel, "El EntityModel no debería ser null");
        assertEquals(usuario, entityModel.getContent(), "El contenido del EntityModel debería ser el usuario");
        
        // Verificar que tiene los links esperados
        assertTrue(entityModel.hasLinks(), "El EntityModel debería tener links");
        assertEquals(2, entityModel.getLinks().toList().size(), "Debería tener exactamente 2 links");
    }

    @Test
    void testToModel_DeberiaIncluirSelfLink() {
        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuario);

        // Then
        assertTrue(entityModel.hasLink("self"), "Debería tener un link 'self'");
        
        Link selfLink = entityModel.getRequiredLink("self");
        String expectedSelfHref = linkTo(methodOn(UsuarioController.class)
                .mostrarUsuarioPorId(usuario.getIdUsuario())).toUri().toString();
        assertEquals(expectedSelfHref, selfLink.getHref(), "El link self debería apuntar al usuario específico");
    }

    @Test
    void testToModel_DeberiaIncluirUsuariosLink() {
        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuario);

        // Then
        assertTrue(entityModel.hasLink("usuarios"), "Debería tener un link 'usuarios'");
        
        Link usuariosLink = entityModel.getRequiredLink("usuarios");
        String expectedUsuariosHref = linkTo(methodOn(UsuarioController.class)
                .mostrarUsuarios()).toUri().toString();
        assertEquals(expectedUsuariosHref, usuariosLink.getHref(), "El link usuarios debería apuntar a la lista de usuarios");
    }

    @Test
    void testToModel_ConUsuarioConIdDiferente() {
        // Given
        Usuario otroUsuario = new Usuario();
        otroUsuario.setIdUsuario(99);
        otroUsuario.setRun("98765432-1");
        otroUsuario.setPNombre("María");
        otroUsuario.setSNombre("Elena");
        otroUsuario.setAPaterno("López");
        otroUsuario.setAMaterno("Martínez");
        otroUsuario.setCorreoElectronico("maria.lopez@email.com");
        otroUsuario.setContrasenna("password456");

        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(otroUsuario);

        // Then
        assertNotNull(entityModel);
        assertEquals(otroUsuario, entityModel.getContent());
        
        Link selfLink = entityModel.getRequiredLink("self");
        String expectedSelfHref = linkTo(methodOn(UsuarioController.class)
                .mostrarUsuarioPorId(99)).toUri().toString();
        assertEquals(expectedSelfHref, selfLink.getHref());
    }

    @Test
    void testToModel_VerificarTiposDeLinks() {
        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuario);

        // Then
        Link selfLink = entityModel.getRequiredLink("self");
        Link usuariosLink = entityModel.getRequiredLink("usuarios");

        assertEquals("self", selfLink.getRel().value(), "El rel del primer link debería ser 'self'");
        assertEquals("usuarios", usuariosLink.getRel().value(), "El rel del segundo link debería ser 'usuarios'");
    }

    @Test
    void testToModel_ConUsuarioMinimo() {
        // Given - Usuario con solo ID
        Usuario usuarioMinimo = new Usuario();
        usuarioMinimo.setIdUsuario(1);

        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuarioMinimo);

        // Then
        assertNotNull(entityModel);
        
        Usuario contenido = entityModel.getContent();
        assertNotNull(contenido, "El contenido del EntityModel no debería ser null");
        assertEquals(1, contenido.getIdUsuario());
        assertTrue(entityModel.hasLinks());
    }

    @Test
    void testImplementaInterfazCorrecta() {
        // Then
        assertTrue(usuarioModelAssembler instanceof org.springframework.hateoas.server.RepresentationModelAssembler,
                "UsuarioModelAssembler debería implementar RepresentationModelAssembler");
    }

    @Test
    void testToModel_ConUsuarioCompleto() {
        // Given
        Usuario usuarioCompleto = new Usuario();
        usuarioCompleto.setIdUsuario(5);
        usuarioCompleto.setRun("11111111-1");
        usuarioCompleto.setPNombre("Ana");
        usuarioCompleto.setSNombre("Victoria");
        usuarioCompleto.setAPaterno("García");
        usuarioCompleto.setAMaterno("Silva");
        usuarioCompleto.setCorreoElectronico("ana.garcia@correo.com");
        usuarioCompleto.setContrasenna("secreto123");

        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuarioCompleto);

        // Then
        assertNotNull(entityModel);
        Usuario contenido = entityModel.getContent();
        assertNotNull(contenido);
        assertEquals("11111111-1", contenido.getRun());
        assertEquals("Ana", contenido.getPNombre());
        assertEquals("García", contenido.getAPaterno());
        assertEquals("ana.garcia@correo.com", contenido.getCorreoElectronico());
        
        // Verificar links específicos para este usuario
        Link selfLink = entityModel.getRequiredLink("self");
        assertTrue(selfLink.getHref().contains("5"));
    }

    @Test
    void testToModel_VerificarPersistenciaDeAtributos() {
        // Given
        usuario.setRun("22222222-2");
        usuario.setPNombre("Pedro");
        usuario.setCorreoElectronico("pedro@test.com");

        // When
        EntityModel<Usuario> entityModel = usuarioModelAssembler.toModel(usuario);

        // Then
        Usuario usuarioDevuelto = entityModel.getContent();
        assertNotNull(usuarioDevuelto, "El usuario devuelto no debería ser null");
        assertEquals("22222222-2", usuarioDevuelto.getRun());
        assertEquals("Pedro", usuarioDevuelto.getPNombre());
        assertEquals("pedro@test.com", usuarioDevuelto.getCorreoElectronico());
    }
}