package ru.reel.CollectionService.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_rating")
    private double ownerRating;

    @Column(name = "catalog_movie_id", nullable = false)
    private String catalogId;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private MovieStatus status;

    @ManyToMany(mappedBy = "movies", fetch = FetchType.EAGER)
    private Set<Collection> collections = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
