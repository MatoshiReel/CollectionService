package ru.reel.CollectionService.infrastructure.messaging.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.service.CollectionScopeService;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.UUID;

@Slf4j
@Component
public class UserRegisteredEventHandler {
    private final static short COLLECTION_SCOPE_PRIORITY = 1;
    private final CollectionService collectionService;
    private final CollectionScopeService collectionScopeService;

    public UserRegisteredEventHandler(CollectionService collectionService, CollectionScopeService collectionScopeService) {
        this.collectionService = collectionService;
        this.collectionScopeService = collectionScopeService;
    }

    @KafkaListener(topics = "user-registered")
    public void handle(String userId) {
        Collection collection = new Collection();
        collection.setName("Избранное");
        try {
            collection.setOwnerId(UUID.fromString(userId));
        } catch (IllegalArgumentException e) {
            log.error("Bad User UUID format, message content : [{}]. More details : {}", userId, e.getMessage());
        }
        collection.setOrder((short) 1);
        try {
            collection.setScope(collectionScopeService.getByPriority(COLLECTION_SCOPE_PRIORITY));
            collectionService.save(collection);
        } catch (SourceNotFoundException e) {
            log.error("collection scope wasn't found, priority value : {}. More details : {}", COLLECTION_SCOPE_PRIORITY, e.getMessage());
        }
    }
}
