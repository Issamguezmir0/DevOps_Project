package tn.esprit.spring.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Universite.IUniversiteService;

import java.util.List;

@RestController
@RequestMapping("universite")
@AllArgsConstructor
public class UniversiteRestController {

    IUniversiteService service;

    @PostMapping("addOrUpdate")
    public Universite addOrUpdate(@RequestBody Universite u) {
        return service.addOrUpdate(u);
    }

    @GetMapping("findAll")
    public List<Universite> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Universite findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Universite u) {
        service.delete(u);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }

    // New functionality 1: Find universities by foyer ID and student enrollment status
    @GetMapping("findByFoyerAndStudentStatus")
    public List<Universite> findByFoyerAndStudentStatus(@RequestParam long foyerId, @RequestParam String status) {
        return service.findUniversitesByFoyerAndStudentStatus(foyerId, status);
    }

    // New functionality 2: Count universities by address
    @GetMapping("countByAddress")
    public long countByAddress(@RequestParam String adresse) {
        return service.countUniversitiesByAddress(adresse);
    }
}
