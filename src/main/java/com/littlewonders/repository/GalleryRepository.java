package com.littlewonders.repository;

import com.littlewonders.model.GalleryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GalleryRepository extends JpaRepository<GalleryItem, Long> {
    List<GalleryItem> findByActiveTrueOrderByUploadDateDesc();
    List<GalleryItem> findByCategoryAndActiveTrue(String category);
}
