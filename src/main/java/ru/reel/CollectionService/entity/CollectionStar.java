package ru.reel.CollectionService.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "collection_stars")
public class CollectionStar {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;
}
