package cap.usuario.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pais")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pais {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPais;

    @Column(nullable = false)
    private String nombre;

}
