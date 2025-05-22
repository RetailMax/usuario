package cap.usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
