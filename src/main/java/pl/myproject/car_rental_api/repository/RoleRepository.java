package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Role;
import pl.myproject.car_rental_api.enums.UserRole;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role, Integer> {

    Optional<Role> findByRole(UserRole userRole);
}
