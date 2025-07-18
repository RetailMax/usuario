package cap.usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comprador")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Comprador extends Usuario{

    @Column(nullable=true)
    private Integer telefono;

    @Column(nullable=false)
    private String direccion;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Region region;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Ciudad ciudad;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Comuna comuna;

    @Column(nullable=false)
    private Integer codigoPostal;

}
