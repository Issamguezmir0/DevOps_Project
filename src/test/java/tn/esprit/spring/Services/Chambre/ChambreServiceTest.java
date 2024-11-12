package tn.esprit.spring.Services.Chambre;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChambreServiceTest {

    @InjectMocks
    private ChambreService chambreService; // Use the implementation class here

    @Mock
    private ChambreRepository chambreRepository;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)
                .bloc(new Bloc(1L, "A"))
                .build();
    }

    @Test
    void testAddOrUpdateChambre() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Chambre savedChambre = chambreService.addOrUpdate(chambre);

        assertNotNull(savedChambre);
        assertEquals(1L, savedChambre.getIdChambre());
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }

    @Test
    void testFindAllChambres() {
        List<Chambre> chambres = Arrays.asList(chambre, chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.findAll();

        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testFindChambreById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteChambreById() {
        doNothing().when(chambreRepository).deleteById(1L);

        chambreService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteChambre() {
        doNothing().when(chambreRepository).delete(any(Chambre.class));

        chambreService.delete(chambre);

        verify(chambreRepository, times(1)).delete(any(Chambre.class));
    }

    @Test
    void testGetChambresParNomBloc() {
        List<Chambre> chambres = Arrays.asList(chambre, chambre);
        when(chambreRepository.findByBlocNomBloc("A")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBloc("A");

        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findByBlocNomBloc("A");
    }

    @Test
    void testNbChambreParTypeEtBloc() {
        when(chambreRepository.countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, 1L)).thenReturn(Math.toIntExact(Long.valueOf(5)));

        long count = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L);

        assertEquals(5L, count);
        verify(chambreRepository, times(1)).countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, 1L);
    }
}
