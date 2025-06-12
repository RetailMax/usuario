package cap.usuario.controller;

import cap.usuario.model.DireccionEnvio;
import cap.usuario.service.DireccionEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/direcciones-envio")
public class DireccionEnvioController {

    @Autowired
    private DireccionEnvioService direccionEnvioService;

    @GetMapping("/usuario/{idUsuario}")
    public List<DireccionEnvio> listarDirecciones(@PathVariable Integer idUsuario) {
        return direccionEnvioService.obtenerDireccionesPorUsuario(idUsuario);
    }

    @PostMapping
    public DireccionEnvio agregarDireccion(@RequestBody DireccionEnvio direccion) {
        return direccionEnvioService.agregarDireccion(direccion);
    }

    @PutMapping("/{id}")
    public DireccionEnvio actualizarDireccion(@PathVariable Integer id, @RequestBody DireccionEnvio direccion) {
        return direccionEnvioService.actualizarDireccion(id, direccion);
    }

    @DeleteMapping("/{id}")
    public void eliminarDireccion(@PathVariable Integer id) {
        direccionEnvioService.eliminarDireccion(id);
    }
}
