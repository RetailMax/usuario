package cap.usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comprador")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Comprador extends Usuario{

    @Column(nullable=true)
    private Integer telefono;

    @Column(nullable=false)
    private String direccion;

    @OneToOne
    @JoinColumn(nullable=false)
    private Region region;

    @OneToOne
    @JoinColumn(nullable=false)
    private Ciudad ciudad;

    @OneToOne
    @JoinColumn(nullable=false)
    private Comuna comuna;

    @Column(nullable=false)
    private Integer codigoPostal;

    @Column(nullable=false)
    private String correoElectronico;

}
