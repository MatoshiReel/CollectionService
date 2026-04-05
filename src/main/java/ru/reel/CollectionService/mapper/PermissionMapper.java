package ru.reel.CollectionService.mapper;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.dto.PermissionDto;
import ru.reel.CollectionService.entity.Permission;

import java.util.UUID;

@Component
public class PermissionMapper implements Mapper<PermissionDto, Permission> {
    @Override
    public Permission from(PermissionDto dto) throws IllegalArgumentException {
        if(dto == null)
            return null;
        Permission entity = new Permission();
        entity.setId(UUID.fromString(dto.id));
        return entity;
    }

    @Override
    public PermissionDto to(Permission entity) {
        if(entity == null)
            return null;
        PermissionDto dto = new PermissionDto();
        dto.id = entity.getId().toString();
        dto.title = entity.getTitle();
        return dto;
    }
}
