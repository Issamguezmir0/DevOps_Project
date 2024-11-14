package tn.esprit.spring.Services.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tn.esprit.spring.DAO.Entities.TypeChambre.SIMPLE;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrUpdate() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.addOrUpdate(reservation);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void findAll() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById("1");

        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void deleteById() {
        String id = "1";

        reservationService.deleteById(id);

        verify(reservationRepository, times(1)).deleteById(id);
    }

    @Test
    void delete() {
        Reservation reservation = new Reservation();

        reservationService.delete(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    void ajouterReservationEtAssignerAChambreEtAEtudiant() {
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(SIMPLE);
        chambre.setIdChambre(1L);
        Bloc bloc = new Bloc();
        chambre.setBloc(bloc);
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123456789L);

        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(123456789L)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                eq(chambre.getIdChambre()), any(LocalDate.class), any(LocalDate.class))).thenReturn(0);

        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 123456789L);

        assertNull(result);
        verify(chambreRepository, times(1)).findByNumeroChambre(101L);
        verify(etudiantRepository, times(1)).findByCin(123456789L);
        verify(reservationRepository, times(1));
    }

    @Test
    void getReservationParAnneeUniversitaire() {
        LocalDate debutAnnee = LocalDate.of(2023, 9, 15);
        LocalDate finAnnee = LocalDate.of(2024, 6, 30);

        when(reservationRepository.countByAnneeUniversitaireBetween(debutAnnee, finAnnee)).thenReturn(10);

        long result = reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        assertEquals(10L, result);
        verify(reservationRepository, times(1)).countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Test
    void annulerReservation() {
        long cin = 123456789L;
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.getReservations().add(reservation);

        when(reservationRepository.findByEtudiantsCinAndEstValide(cin, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation("1")).thenReturn(chambre);

        String result = reservationService.annulerReservation(cin);

        assertEquals("La réservation 1 est annulée avec succés", result);
        verify(reservationRepository, times(1)).delete(reservation);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void affectReservationAChambre() {
        String idRes = "1";
        long idChambre = 1L;

        Reservation reservation = new Reservation();
        reservation.setIdReservation(idRes);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(idChambre);

        when(reservationRepository.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre(idRes, idChambre);

        verify(chambreRepository, times(1));
    }

    @Test
    void annulerReservations() {
        LocalDate start = LocalDate.of(2023, 9, 15);
        LocalDate end = LocalDate.of(2024, 6, 30);

        Reservation reservation1 = new Reservation();
        reservation1.setIdReservation("1");
        reservation1.setEstValide(true);

        List<Reservation> reservations = List.of(reservation1);

        when(reservationRepository.findByEstValideAndAnneeUniversitaireBetween(true, start, end)).thenReturn(reservations);

        reservationService.annulerReservations();

        verify(reservationRepository, times(1));
        assertTrue(reservation1.isEstValide());
    }

    @Test
    void extendReservationValidity() {
        String reservationId = "1";
        int additionalDays = 30;

        Reservation reservation = new Reservation();
        reservation.setIdReservation(reservationId);
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        reservationService.extendReservationValidity(reservationId, additionalDays);

        assertEquals(LocalDate.now().plusDays(additionalDays), reservation.getAnneeUniversitaire());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void findReservationsByStudentCin() {
        long cin = 123456789L;
        List<Reservation> reservations = List.of(new Reservation());

        when(reservationRepository.findByEtudiantsCin(cin)).thenReturn(reservations);

        List<Reservation> result = reservationService.findReservationsByStudentCin(cin);

        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findByEtudiantsCin(cin);
    }
}
