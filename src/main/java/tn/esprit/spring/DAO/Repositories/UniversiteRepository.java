package tn.esprit.spring.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.DAO.Entities.Universite;

import java.time.LocalDate;
import java.util.List;

public interface UniversiteRepository extends JpaRepository<Universite, Long> {

    // Find a university by its name
    Universite findByNomUniversite(String nomUniversite);

    // Find universities with students whose names contain the given substring and
    // whose date of birth falls between two specified dates
    List<Universite> findByFoyerBlocsChambresReservationsEtudiantsNomEtLikeAndFoyerBlocsChambresReservationsEtudiantsDateNaissanceBetween(
            String nom, LocalDate date1, LocalDate date2);

    // Find universities by Foyer ID and student enrollment status
    List<Universite> findByFoyerIdAndFoyerBlocsChambresReservationsEtudiantsStatus(long foyerId, String status);

    // Count the number of universities located at a specific address
    long countByAdresse(String adresse);
}
