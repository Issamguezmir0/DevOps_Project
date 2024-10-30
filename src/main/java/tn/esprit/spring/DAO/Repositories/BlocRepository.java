package tn.esprit.spring.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.util.List;

public interface BlocRepository extends JpaRepository<Bloc, Long> {
    Bloc findByNomBloc(String nom);

    @Query("select b from Bloc b where b.nomBloc=?1")
    Bloc selectByNomBJPQL1(String nom);

    @Query("select b from Bloc b where b.nomBloc=:nom")
    Bloc selectByNomBJPQL2(@Param("nom") String nom);

    @Query(value = "SELECT * FROM t_bloc WHERE nom_bloc=?1", nativeQuery = true)
    Bloc selectByNomBSQL1(String nom);

    @Query(value = "SELECT * FROM t_bloc WHERE nom_bloc=:nom", nativeQuery = true)
    Bloc selectByNomBSQL2(@Param("nom") String nom);

    @Modifying
    @Query("update Bloc b set b.nomBloc=?1 where b.capaciteBloc<10")
    void updateBlocJPQL(String nom);

    @Modifying
    @Query(value = "update t_bloc set nom_bloc=?1 where capacite_bloc<10", nativeQuery = true)
    void updateBlocSQL(String nom);

    // Récupérer les blocs qui ont des chambres avec un typeChambre donné
    @Query("select b from Bloc b join Chambre c on c.bloc.idBloc=b.idBloc where c.typeC=?1")
    List<Bloc> selectBlocsByTypeChambreJPQL(TypeChambre typeChambre);

    // New Method 1: Find blocs with capacity greater than a given value
    @Query("SELECT b FROM Bloc b WHERE b.capaciteBloc > :capacity")
    List<Bloc> findBlocsWithCapacityGreaterThan(@Param("capacity") long capacity);

    // New Method 2: Find blocs grouped by capacity with a specific type of room
    @Query("SELECT b FROM Bloc b JOIN b.chambres c WHERE c.typeC = :typeChambre GROUP BY b.capaciteBloc")
    List<Bloc> findBlocsByTypeChambreGroupedByCapacity(@Param("typeChambre") TypeChambre typeChambre);
}
