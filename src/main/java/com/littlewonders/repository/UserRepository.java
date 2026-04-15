package com.littlewonders.repository;

import com.littlewonders.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    @Query("SELECT MAX(u.rollNumber) FROM User u")
    String findMaxRollNumber();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    java.util.List<User> findAllByRoleName(String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.session = :session AND r.name = :roleName")
    java.util.List<User> findAllBySessionAndRoles_Name(String session, String roleName);
}
