package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionDto;
import ru.reel.CollectionService.entity.Collection;

import java.util.stream.Collectors;

@Component
public class CollectionMapper implements Mapper<CollectionDto, Collection> {
    private final CollectionScopeMapper collectionScopeMapper;

    public CollectionMapper(CollectionScopeMapper collectionScopeMapper) {
        this.collectionScopeMapper = collectionScopeMapper;
    }

    @Override
    public Collection from(CollectionDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        Collection entity = new Collection();
        entity.setName(dto.name);
        entity.setOrder(dto.order);
        entity.setOwnerId(dto.ownerId);
        entity.setScope(collectionScopeMapper.from(dto.scope));
        return entity;
    }

    @Override
    public CollectionDto to(Collection entity) {
        return to(entity, false, null);
    }

    public CollectionDto to(Collection entity, boolean isDeepMapping, MovieMapper movieMapper) {
        if(entity == null)
            return null;
        CollectionDto dto = new CollectionDto();
        dto.id = entity.getId().toString();
        dto.name = entity.getName();
        dto.order = entity.getOrder();
        dto.createdAt = entity.getCreatedAt();
        dto.ownerId = entity.getOwnerId();
        dto.scope = collectionScopeMapper.to(entity.getScope());
        if(isDeepMapping)
            dto.movies = entity.getMovies().stream().map(movieMapper::to).collect(Collectors.toSet());
        return dto;
    }
}
