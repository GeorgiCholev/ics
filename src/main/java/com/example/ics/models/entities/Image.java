package com.example.ics.models.entities;

import com.example.ics.models.dtos.ImageDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String url;

    @Column(name = "analysed_at", nullable = false)
    private LocalDateTime analysedAt;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @ManyToMany
    @JoinTable(name = "images_tags",
            joinColumns = @JoinColumn(name = "image_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    private Set<Tag> tags;

    public Image() {
        this.tags = new TreeSet<>();
    }

    public Image(ImageDto dto, List<Tag> relatedTags) {
        this.url = dto.getUrl();
        this.analysedAt = dto.getAnalysedAt();
        this.width = dto.getWidth();
        this.height = dto.getHeight();
        this.tags = new TreeSet<>(relatedTags);
    }
}
