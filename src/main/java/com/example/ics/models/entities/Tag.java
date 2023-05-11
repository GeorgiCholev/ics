package com.example.ics.models.entities;

import com.example.ics.models.dtos.TagDto;
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
        this.name = dto.getName();
        this.confidence = dto.getConfidence();
    }

    @Override
    public int compareTo(Tag o) {
        int orderByConfidence = Integer.compare(o.getConfidence(), this.confidence);
        return orderByConfidence != 0 ? orderByConfidence : this.name.compareTo(o.getName());
    }
}
