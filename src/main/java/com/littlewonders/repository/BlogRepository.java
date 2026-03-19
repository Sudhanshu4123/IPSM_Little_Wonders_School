package com.littlewonders.repository;

import com.littlewonders.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByActiveTrueOrderByPublishedDateDesc();
}
