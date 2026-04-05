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
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 35, nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @ManyToOne
    @JoinColumn(name = "permission _id")
    private Permission permission;

    @OneToMany(mappedBy = "collection")
    private Set<UserCollectionStar> userStars = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "collections_favorite_movies",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_movie_id")
    )
    private Set<FavoriteMovie> favoriteMovies = new HashSet<>();
}
