package com.example.ics.models.entities;

import com.example.ics.models.dtos.image.PersistImageDto;
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

    @Column(unique = true, nullable = false)
    private String checksum;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "images_tags",
            joinColumns = @JoinColumn(name = "image_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false))
    private Set<Tag> tags;

    public Image() {
    }

    public Image(PersistImageDto dto, Set<Tag> relatedTags) {
        this(dto.url(), dto.width(), dto.height(), dto.checksum(), relatedTags);
    }

    private Image(String url, Integer width, Integer height, String checksum, Set<Tag> tags) {
        this.url = url;
        setAnalysedAt();
        this.width = width;
        this.height = height;
        this.checksum = checksum;
        setTags(tags);
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
        return new TreeSet<>(tags);
    }


    private void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    private void setAnalysedAt() {
        this.analysedAt = LocalDateTime.now();
    }

    public void updateTags(Set<Tag> newTags) {
        setTags(newTags);
        setAnalysedAt();
    }
}
