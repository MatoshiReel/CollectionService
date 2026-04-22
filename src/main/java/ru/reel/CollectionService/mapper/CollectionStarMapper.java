package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionStarDto;
import ru.reel.CollectionService.entity.CollectionStar;

import java.util.UUID;

@Component
public class CollectionStarMapper implements Mapper<CollectionStarDto, CollectionStar> {
    @Override
    public CollectionStar from(CollectionStarDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        CollectionStar entity = new CollectionStar();
        if(dto.userId != null) entity.setUserId(UUID.fromString(dto.userId));
        return entity;
    }

    @Override
    public CollectionStarDto to(CollectionStar entity) {
        if(entity == null)
            return null;
        CollectionStarDto dto = new CollectionStarDto();
        if(entity.getId() != null) dto.id = entity.getId().toString();
        if(entity.getUserId() != null) dto.userId = entity.getUserId().toString();
        if(entity.getCollection() != null && entity.getCollection().getId() != null) dto.collectionId = entity.getCollection().getId().toString();
        return dto;
    }
}
