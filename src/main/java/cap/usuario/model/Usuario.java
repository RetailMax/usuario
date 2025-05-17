package cap.usuario.model;

import java.sql.Date;

import jakarta.persistence.*;

@MappedSuperclass
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

    //Hasta acá debería ser la clase padre. -A

}
