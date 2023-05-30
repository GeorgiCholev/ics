package com.example.ics.repositories;

import com.example.ics.models.entities.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findByUrl(String url);

    Optional<Image> findByChecksum(String checksum);

    @Query("SELECT img FROM Image img JOIN img.tags t WHERE t.name IN :tagNames")
    Page<Image> findAllThatContain(@Param("tagNames") List<String> tagNames, Pageable pageable);
}
