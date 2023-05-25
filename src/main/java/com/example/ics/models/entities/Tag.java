package com.example.ics.models.entities;

import com.example.ics.models.dtos.tag.TagDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag extends BaseEntity implements Comparable<Tag> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer confidence;

    public String getName() {
        return name;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public Tag() {
    }

    public Tag(TagDto dto) {
        setId(dto.getId());
        this.name = dto.getName();
        this.confidence = dto.getConfidence();
    }

    public Tag(String name, Integer confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    @Override
    public int compareTo(Tag o) {
        int orderByConfidence = Integer.compare(o.getConfidence(), this.confidence);
        return orderByConfidence != 0 ? orderByConfidence : this.name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!name.equals(tag.name)) return false;
        if (!getId().equals(tag.getId())) return false;
        return confidence.equals(tag.confidence);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + confidence.hashCode();
        return result;
    }
}
