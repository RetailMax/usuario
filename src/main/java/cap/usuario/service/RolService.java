package cap.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cap.usuario.model.Rol;
import cap.usuario.repository.RolRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    //Listar roles
    public List<Rol> listarRoles(){
        return rolRepository.findAll();
    }

    //Buscar rol por id
    public Rol buscarRolPorId(Integer id){
        return rolRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    //Agregar rol
    public Rol guardarRol(Rol rol){
        return rolRepository.save(rol);
    }

    //Actualizar rol
    public Rol actualizarRol(Integer id, Rol rol){
        Rol r = buscarRolPorId(id);
        r.setNombre(rol.getNombre());
        return rolRepository.save(r);
    }

    //Eliminar rol
    public void eliminarRol(Integer id){
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }

}
