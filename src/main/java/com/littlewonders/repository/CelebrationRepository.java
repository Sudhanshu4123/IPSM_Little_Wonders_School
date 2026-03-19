package com.littlewonders.repository;

import com.littlewonders.model.Celebration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CelebrationRepository extends JpaRepository<Celebration, Long> {
    List<Celebration> findByActiveTrueOrderByEventDateDesc();
    Optional<Celebration> findByTitle(String title);
}
