package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.FavoriteMovieDto;
import ru.reel.CollectionService.entity.FavoriteMovie;

@Component
public class FavoriteMovieMapper implements Mapper<FavoriteMovieDto, FavoriteMovie> {
    private final MovieStatusMapper movieStatusMapper;

    public FavoriteMovieMapper(MovieStatusMapper movieStatusMapper) {
        this.movieStatusMapper = movieStatusMapper;
    }

    @Override
    public FavoriteMovie from(FavoriteMovieDto dto) {
        if(dto == null)
            return null;
        FavoriteMovie entity = new FavoriteMovie();
        entity.setCatalogMovieId(dto.catalogMovieId);
        entity.setOwnerRating(dto.ownerRating);
        entity.setStatus(movieStatusMapper.from(dto.status));
        return entity;
    }

    @Override
    public FavoriteMovieDto to(FavoriteMovie entity) {
        if(entity == null)
            return null;
        FavoriteMovieDto dto = new FavoriteMovieDto();
        dto.id = entity.getId().toString();
        dto.catalogMovieId = entity.getCatalogMovieId();
        dto.ownerRating = entity.getOwnerRating();
        dto.status = movieStatusMapper.to(entity.getStatus());
        return dto;
    }
}
