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
@Table(name = "collection_scopes")
public class CollectionScope {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 15, nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "scope")
    private Set<Collection> collections = new HashSet<>();
}
