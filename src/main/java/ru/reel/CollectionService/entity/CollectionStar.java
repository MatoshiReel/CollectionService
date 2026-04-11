package ru.reel.CollectionService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "collection_stars")
@NoArgsConstructor
public class CollectionStar {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    public CollectionStar(String userId, Collection collection) {
        this.userId = userId;
        this.collection = collection;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof CollectionStar)) return false;
        return id != null && id.equals(((CollectionStar) o).id);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
