package com.example.ics.models.entities;

import com.example.ics.models.dtos.image.PersistImageDto;
import com.example.ics.models.dtos.image.UpdateImageDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "images_tags",
            joinColumns = @JoinColumn(name = "image_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    private Set<Tag> tags;

    public Image() {
        this.tags = new TreeSet<>();
    }

    public Image(UpdateImageDto dto, Set<Tag> relatedTags) {
        this(dto.getUrl(), dto.getWidth(), dto.getHeight(), relatedTags);
        setId(dto.getId());
    }

    public Image(PersistImageDto dto, Set<Tag> relatedTags) {
        this(dto.url(), dto.width(), dto.height(), relatedTags);
    }

    private Image(String url, Integer width, Integer height, Set<Tag> tags) {
        this.url = url;
        this.analysedAt = LocalDateTime.now();
        this.width = width;
        this.height = height;
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getAnalysedAt() {
        return analysedAt;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
