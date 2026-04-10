package ru.reel.CollectionService.dto;

import java.util.HashSet;
import java.util.Set;

public class MovieDto {
    public String id;
    public double ownerRating;
    public String catalogId;
    public MovieStatusDto status;
    public Set<CollectionDto> collections = new HashSet<>();
}