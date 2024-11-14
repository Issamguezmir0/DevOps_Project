package tn.esprit.spring.Services.Bloc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBlocsWithCapacityGreaterThan() {
        long capacity = 10;
        List<Bloc> expectedBlocs = Arrays.asList(
                new Bloc(1L, "Bloc1", 20, null, null),
                new Bloc(2L, "Bloc2", 15, null, null)
        );

        when(blocRepository.findBlocsWithCapacityGreaterThan(capacity)).thenReturn(expectedBlocs);

        List<Bloc> result = blocService.findBlocsWithCapacityGreaterThan(capacity);

        assertEquals(expectedBlocs.size(), result.size());
        assertEquals(expectedBlocs.get(0).getNomBloc(), result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findBlocsWithCapacityGreaterThan(capacity);
    }

    @Test
    void testFindBlocsByTypeChambreGroupedByCapacity() {
        TypeChambre typeChambre = TypeChambre.SIMPLE;
        List<Bloc> expectedBlocs = Arrays.asList(
                new Bloc(1L, "Bloc1", 20, null, null),
                new Bloc(2L, "Bloc2", 20, null, null)
        );

        when(blocRepository.findBlocsByTypeChambreGroupedByCapacity(typeChambre)).thenReturn(expectedBlocs);

        List<Bloc> result = blocService.findBlocsByTypeChambreGroupedByCapacity(typeChambre);

        assertEquals(expectedBlocs.size(), result.size());
        assertEquals(expectedBlocs.get(0).getNomBloc(), result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findBlocsByTypeChambreGroupedByCapacity(typeChambre);
    }

    @Test
    void testFindAll() {
        List<Bloc> expectedBlocs = Arrays.asList(
                new Bloc(1L, "Bloc1", 20, null, null),
                new Bloc(2L, "Bloc2", 15, null, null)
        );

        when(blocRepository.findAll()).thenReturn(expectedBlocs);

        List<Bloc> result = blocService.findAll();

        assertEquals(expectedBlocs.size(), result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testDeleteById() {
        long id = 1L;
        blocService.deleteById(id);
        verify(blocRepository, times(1)).deleteById(id);
    }
}
