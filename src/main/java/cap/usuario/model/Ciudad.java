package cap.usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ciudad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCiudad;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(nullable = true)
    private String codigoPostal;
}
