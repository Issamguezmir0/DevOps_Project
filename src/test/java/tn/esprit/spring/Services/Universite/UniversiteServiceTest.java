package tn.esprit.spring.Services.Universite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test 1: Add or Update University
    @Test
    void addOrUpdate_ShouldSaveUniversity() {
        Universite universite = new Universite();
        universite.setNomUniversite("Test University");

        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotNull(savedUniversite);
        assertEquals("Test University", savedUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    // Test 2: Find University by ID
    @Test
    void findById_ShouldReturnUniversity_WhenExists() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite foundUniversite = universiteService.findById(1L);

        assertNotNull(foundUniversite);
        assertEquals(1L, foundUniversite.getIdUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    // Test 3: Find Universities by Foyer ID and Student Status
    @Test
    void findUniversitesByFoyerAndStudentStatus_ShouldReturnUniversities() {
        long foyerId = 1L;
        String status = "enrolled";
        Universite universite1 = new Universite();
        Universite universite2 = new Universite();

        when(universiteRepository.findByFoyerIdAndFoyerBlocsChambresReservationsEtudiantsStatus(foyerId, status))
                .thenReturn(List.of(universite1, universite2));

        List<Universite> universites = universiteService.findUniversitesByFoyerAndStudentStatus(foyerId, status);

        assertEquals(2, universites.size());
        verify(universiteRepository, times(1))
                .findByFoyerIdAndFoyerBlocsChambresReservationsEtudiantsStatus(foyerId, status);
    }

    // Test 4: Count Universities by Address
    @Test
    void countUniversitiesByAddress_ShouldReturnCount() {
        String address = "123 Main St";
        long count = 3;

        when(universiteRepository.countByAdresse(address)).thenReturn(count);

        long result = universiteService.countUniversitiesByAddress(address);

        assertEquals(3, result);
        verify(universiteRepository, times(1)).countByAdresse(address);
    }
}
