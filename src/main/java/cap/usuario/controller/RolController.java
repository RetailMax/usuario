package cap.usuario.controller;

import cap.usuario.model.Rol;
import cap.usuario.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    // Crear un nuevo rol
    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolRepository.save(rol);
    }

    // Listar todos los roles
    @GetMapping
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    // Actualizar un rol
    @PutMapping("/{id}")
    public Rol actualizarRol(@PathVariable Integer id, @RequestBody Rol rolActualizado) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setNombre(rolActualizado.getNombre());
        return rolRepository.save(rol);
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    public void eliminarRol(@PathVariable Integer id) {
        rolRepository.deleteById(id);
    }
}