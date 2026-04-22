package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.CollectionScopeDto;
import ru.reel.CollectionService.entity.CollectionScope;

import java.util.UUID;

@Component
public class CollectionScopeMapper implements Mapper<CollectionScopeDto, CollectionScope> {
    @Override
    public CollectionScope from(CollectionScopeDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        CollectionScope entity = new CollectionScope();
        if(dto.id != null) entity.setId(UUID.fromString(dto.id));
        entity.setPriority(dto.priority);
        entity.setTitle(dto.title);
        return entity;
    }

    @Override
    public CollectionScopeDto to(CollectionScope entity) {
        if(entity == null)
            return null;
        CollectionScopeDto dto = new CollectionScopeDto();
        if(entity.getId() != null) dto.id = entity.getId().toString();
        dto.title = entity.getTitle();
        dto.priority = entity.getPriority();
        return dto;
    }
}
