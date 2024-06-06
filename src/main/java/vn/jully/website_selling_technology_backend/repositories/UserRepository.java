package vn.jully.website_selling_technology_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.jully.website_selling_technology_backend.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail (String email);

    Optional<User> findByEmail (String email);
}
