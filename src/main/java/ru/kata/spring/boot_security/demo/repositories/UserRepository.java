package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :name")
    Optional<User> findByUsername (@Param("name") String username);

    @Query("SELECT u FROM User u join fetch u.roles WHERE u.username = :name")
    User getByUsername(@Param("name") String username);
}
