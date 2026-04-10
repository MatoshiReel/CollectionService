package ru.reel.CollectionService.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
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

    @Column(name = "priority")
    private short priority;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @ManyToOne
    @JoinColumn(name = "collection_scope _id")
    private CollectionScope scope;

    @OneToMany(mappedBy = "collection")
    private Set<CollectionStar> userStars = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "collections_movies",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> movies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collection)) return false;
        return id != null && id.equals(((Collection) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
