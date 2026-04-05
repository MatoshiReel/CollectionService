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
@Table(name = "movie_statuses")
public class MovieStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 25, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "status")
    private Set<FavoriteMovie> userMovies = new HashSet<>();
}
