package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.MovieDto;
import ru.reel.CollectionService.entity.Movie;

import java.util.stream.Collectors;

@Component
public class MovieMapper implements Mapper<MovieDto, Movie> {
    private final MovieStatusMapper movieStatusMapper;

    public MovieMapper(MovieStatusMapper movieStatusMapper) {
        this.movieStatusMapper = movieStatusMapper;
    }

    @Override
    public Movie from(MovieDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        Movie entity = new Movie();
        entity.setCatalogId(dto.catalogId);
        entity.setOwnerRating(dto.ownerRating);
        entity.setStatus(movieStatusMapper.from(dto.status));
        return entity;
    }

    @Override
    public MovieDto to(Movie entity) {
        return this.to(entity, false, null);
    }

    public MovieDto to(Movie entity, boolean isDeepMapping, CollectionMapper collectionMapper) {
        if(entity == null)
            return null;
        MovieDto dto = new MovieDto();
        dto.id = entity.getId().toString();
        dto.catalogId = entity.getCatalogId();
        dto.ownerRating = entity.getOwnerRating();
        dto.status = movieStatusMapper.to(entity.getStatus());
        if(isDeepMapping)
            dto.collections = entity.getCollections().stream().map(collectionMapper::to).collect(Collectors.toSet());
        return dto;
    }
}
