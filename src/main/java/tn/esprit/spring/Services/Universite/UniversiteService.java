package tn.esprit.spring.Services.Universite;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteService implements IUniversiteService {

    private final UniversiteRepository repo;

    @Override
    public Universite addOrUpdate(Universite u) {
        return repo.save(u);
    }

    @Override
    public List<Universite> findAll() {
        return repo.findAll();
    }

    @Override
    public Universite findById(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Universite u) {
        repo.delete(u);
    }

    // New functionality 1: Find universities by foyer ID and student enrollment status
    @Override
    public List<Universite> findUniversitesByFoyerAndStudentStatus(long foyerId, String status) {
        return repo.findByFoyerIdAndFoyerBlocsChambresReservationsEtudiantsStatus(foyerId, status);
    }

    // New functionality 2: Count universities by address
    @Override
    public long countUniversitiesByAddress(String adresse) {
        return repo.countByAdresse(adresse);
    }
}
