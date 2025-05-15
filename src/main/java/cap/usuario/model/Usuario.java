package cap.usuario.model;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable=false)
    private String contrasenna;

    @Column(nullable=true)
    private Integer telefono;

    @Column(nullable=false)
    private String direccion;

    @Column(nullable=false)
    private String region;

    @Column(nullable=false)
    private String ciudad;

    @Column(nullable=false)
    private String comuna;

    @Column(nullable=false)
    private Integer codigoPostal;

    @Column(nullable=false)
    private String correoElectronico;

}
