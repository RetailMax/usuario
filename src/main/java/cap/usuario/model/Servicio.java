package cap.usuario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Boolean habilitado = true;

    @Column(nullable = false)
    private String version;

    @Column(nullable = true)
    private String configuracion;
}
