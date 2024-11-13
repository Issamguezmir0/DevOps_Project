package tn.esprit.spring.services.Foyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;
    @Mock
    private UniversiteRepository universiteRepository;
    @Mock
    private BlocRepository blocRepository;

    private FoyerService foyerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        foyerService = new FoyerService(foyerRepository, universiteRepository, blocRepository);
    }

    @Test
    public void testAddOrUpdateFoyer() {
        Foyer foyer = new Foyer(1L, "Foyer 1", 100, null, new ArrayList<>());
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addOrUpdate(foyer);

        assertNotNull(result);
        assertEquals(foyer.getNomFoyer(), result.getNomFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    public void testFindById() {
        Foyer foyer = new Foyer(1L, "Foyer 1", 100, null, new ArrayList<>());
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);

        assertNotNull(result);
        assertEquals(foyer.getNomFoyer(), result.getNomFoyer());
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        long foyerId = 1L;
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.deleteById(foyerId);

        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    @Test

    public void testAffecterFoyerAUniversite() {
        long idFoyer = 1L;
        String nomUniversite = "University A";
        Foyer foyer = new Foyer(idFoyer, "Foyer 1", 100, null, new ArrayList<>());
        Universite universite = new Universite(1L, nomUniversite, "Address", null);

        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite(nomUniversite)).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(idFoyer, nomUniversite);

        assertNotNull(result);
        assertEquals(nomUniversite, result.getNomUniversite());
        // Vérifiez si l'université contient bien le foyer affecté
        assertNotNull(result.getFoyer());
        assertEquals(foyer, result.getFoyer());
        verify(universiteRepository, times(1)).save(universite);
    }


    @Test
    public void testAjouterFoyerEtAffecterAUniversite() {
        long idUniversite = 1L;
        Universite universite = new Universite(idUniversite, "University A", "Address", null);
        Foyer foyer = new Foyer(1L, "Foyer 1", 100, null, new ArrayList<>());
        List<Bloc> blocs = new ArrayList<>();
        Bloc bloc = new Bloc();
        blocs.add(bloc);
        foyer.setBlocs(blocs);

        when(universiteRepository.findById(idUniversite)).thenReturn(Optional.of(universite));
        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);
        // Assurez-vous que l'université est sauvegardée avec la relation mise à jour
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);

        assertNotNull(result);
        assertEquals(foyer.getNomFoyer(), result.getNomFoyer());
        verify(universiteRepository, times(1)).save(universite);  // Vérifiez que l'université est sauvegardée
        verify(blocRepository, times(1)).save(bloc);  // Vérifiez que le bloc est sauvegardé
    }


    // Vous pouvez ajouter d'autres tests pour d'autres méthodes
}
