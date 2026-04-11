package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionStarDto;
import ru.reel.CollectionService.entity.CollectionStar;

@Component
public class CollectionStarMapper implements Mapper<CollectionStarDto, CollectionStar> {
    @Override
    public CollectionStar from(CollectionStarDto dto) {
        if(dto == null)
            return null;
        CollectionStar entity = new CollectionStar();
        entity.setUserId(dto.userId);
        return entity;
    }

    @Override
    public CollectionStarDto to(CollectionStar entity) {
        if(entity == null)
            return null;
        CollectionStarDto dto = new CollectionStarDto();
        dto.id = entity.getId().toString();
        dto.userId = entity.getUserId();
        dto.collectionId = entity.getCollection().getId().toString();
        return dto;
    }
}
