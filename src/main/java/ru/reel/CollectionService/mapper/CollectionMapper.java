package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.entity.Collection;

import java.util.stream.Collectors;

@Component
public class CollectionMapper implements Mapper<CollectionDto, Collection> {
    private final PermissionMapper permissionMapper;
    private final FavoriteMovieMapper favoriteMovieMapper;

    public CollectionMapper(PermissionMapper permissionMapper, FavoriteMovieMapper favoriteMovieMapper) {
        this.permissionMapper = permissionMapper;
        this.favoriteMovieMapper = favoriteMovieMapper;
    }

    @Override
    public Collection from(CollectionDto dto) {
        if(dto == null)
            return null;
        Collection entity = new Collection();
        entity.setName(dto.name);
        entity.setOwnerId(dto.ownerId);
        entity.setPermission(permissionMapper.from(dto.permission));
        return entity;
    }

    @Override
    public CollectionDto to(Collection entity) {
        if(entity == null)
            return null;
        CollectionDto dto = new CollectionDto();
        dto.id = entity.getId().toString();
        dto.name = entity.getName();
        dto.ownerId = entity.getOwnerId();
        dto.permission = permissionMapper.to(entity.getPermission());
        dto.favoriteMovies = entity.getFavoriteMovies().stream().map(favoriteMovieMapper::to).collect(Collectors.toSet());
        return dto;
    }
}
