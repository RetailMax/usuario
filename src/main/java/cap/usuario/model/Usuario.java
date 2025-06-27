package cap.usuario.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(unique=true, length=13, nullable=false)
    private String run;

    @Column(nullable=false)
    private String pNombre;

    @Column(nullable=false)
    private String sNombre;

    @Column(nullable=false)
    private String aPaterno;

    @Column(nullable=false)
    private String aMaterno;

    @Column(nullable=false)
    private Date fechaNacimiento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;

    @Column(nullable=false)
    private String contrasenna;

    @Column(nullable=false, unique=true, length=100)
    private String correoElectronico;

}
