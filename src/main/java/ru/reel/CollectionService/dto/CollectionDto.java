package ru.reel.CollectionService.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CollectionDto {
    public String id;
    public String name;
    public short order;
    public Date createdAt;
    public String ownerId;
    public CollectionScopeDto scope;
    public Set<MovieDto> movies = new HashSet<>();
}
