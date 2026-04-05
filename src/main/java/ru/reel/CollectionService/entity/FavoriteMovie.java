package ru.reel.CollectionService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "favorite_movies")
public class FavoriteMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_rating")
    private double ownerRating;

    @Column(name = "catalog_movie_id", nullable = false)
    private String catalogMovieId;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private MovieStatus status;

    @ManyToMany(mappedBy = "favoriteMovies")
    private Set<Collection> collections = new HashSet<>();
}
