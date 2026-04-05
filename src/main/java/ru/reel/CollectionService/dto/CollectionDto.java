package ru.reel.CollectionService.dto;

import ru.reel.CollectionService.entity.FavoriteMovie;

import java.util.HashSet;
import java.util.Set;

public class CollectionDto {
    public String id;
    public String name;
    public String ownerId;
    public PermissionDto permission;
    public Set<FavoriteMovieDto> favoriteMovies = new HashSet<>();
}
