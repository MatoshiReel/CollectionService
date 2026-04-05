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
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 15, nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "permission")
    private Set<Collection> collections = new HashSet<>();
}
