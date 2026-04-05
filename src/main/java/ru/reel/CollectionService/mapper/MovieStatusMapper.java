package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.entity.MovieStatus;

import java.util.UUID;

@Component
public class MovieStatusMapper implements Mapper<MovieStatusDto, MovieStatus> {
    @Override
    public MovieStatus from(MovieStatusDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        MovieStatus entity = new MovieStatus();
        entity.setId(UUID.fromString(dto.id));
        return entity;
    }

    @Override
    public MovieStatusDto to(MovieStatus entity) {
        if(entity == null)
            return null;
        MovieStatusDto dto = new MovieStatusDto();
        dto.id = entity.getId().toString();
        dto.name = entity.getName();
        return dto;
    }
}
