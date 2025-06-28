package cap.usuario;

import cap.usuario.model.*;
import cap.usuario.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {

    @Mock
    private RolRepository rolRepository;
    @Mock
    private CompradorRepository compradorRepository;
    @Mock
    private PaisRepository paisRepository;
    @Mock
    private RegionRepository regionRepository;
    @Mock
    private CiudadRepository ciudadRepository;
    @Mock
    private ComunaRepository comunaRepository;
    @Mock
    private DireccionEnvioRepository direccionEnvioRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_shouldLoadDataCorrectly() throws Exception {
        // Given
        when(rolRepository.saveAll(anyList())).thenReturn(List.of(new Rol(), new Rol()));
        when(paisRepository.save(any(Pais.class))).thenReturn(new Pais());
        when(regionRepository.save(any(Region.class))).thenReturn(new Region());
        when(ciudadRepository.save(any(Ciudad.class))).thenReturn(new Ciudad());
        when(comunaRepository.save(any(Comuna.class))).thenReturn(new Comuna());
        when(compradorRepository.save(any(Comprador.class))).thenReturn(new Comprador());
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenReturn(new DireccionEnvio());

        // When
        dataLoader.run();

        // Then
        verify(direccionEnvioRepository, times(1)).deleteAll();
        verify(compradorRepository, times(1)).deleteAll();
        verify(comunaRepository, times(1)).deleteAll();
        verify(ciudadRepository, times(1)).deleteAll();
        verify(regionRepository, times(1)).deleteAll();
        verify(paisRepository, times(1)).deleteAll();
        verify(rolRepository, times(1)).deleteAll();

        verify(rolRepository, times(1)).saveAll(anyList());
        verify(paisRepository, times(1)).save(any(Pais.class));
        verify(regionRepository, times(1)).save(any(Region.class));
        verify(ciudadRepository, times(1)).save(any(Ciudad.class));
        verify(comunaRepository, times(1)).save(any(Comuna.class));

        verify(compradorRepository, times(10)).save(any(Comprador.class));
        verify(direccionEnvioRepository, times(10)).save(any(DireccionEnvio.class));
    }
}
