package ru.reel.CollectionService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.reel.CollectionService.entity.Permission;

import java.util.UUID;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, UUID> {
}
