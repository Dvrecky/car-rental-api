package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Rent;
import pl.myproject.car_rental_api.projection.ClientRentBaseView;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository <Rent, Long> {

    @Query("""
            SELECT
                r.id as id,
                r.startDate as startDate,
                r.endDate as endDate,
                r.status as status,
                concat(m.name,' ',m.brand) as carFullName,
                m.photoUrl as photoUrl,
                res.id as reservationId,
                c.id as carId
            FROM Rent r
            JOIN r.reservation res
            JOIN res.car c
            JOIN c.model m
            WHERE res.user.id = :clientId
            """)
    List<ClientRentBaseView> findAllRentsBaseView(long clientId);
}
