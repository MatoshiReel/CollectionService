package ru.reel.CollectionService.unit.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.repository.CollectionRepository;
import ru.reel.CollectionService.repository.CollectionScopeRepository;
import ru.reel.CollectionService.service.CollectionService;
import ru.reel.CollectionService.service.CollectionStarService;
import ru.reel.CollectionService.service.exception.NotAllowException;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection star service")
public class CollectionStarServiceTests {
    @Autowired
    private CollectionStarService service;
    private static CollectionScope scope;
    private static Collection collection;

    @BeforeAll
    public static void init(@Autowired CollectionScopeRepository collectionScopeRepository, @Autowired CollectionRepository collectionRepository) {
        scope = new CollectionScope();
        scope.setTitle("scope_title");
        scope.setPriority((short) 3);
        collectionScopeRepository.save(scope);
        collection = new Collection();
        collection.setOwnerId(UUID.randomUUID());
        collection.setName("collection_name");
        collection.setOrder((short) 1);
        collection.setScope(scope);
        collectionRepository.save(collection);
    }

    @Nested
    @DisplayName("save")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SaveTests {
        @Test
        @Order(1)
        @DisplayName("save with null userId")
        public void saveWithNullUserIdTest() {
            assertThrows(NullPointerException.class, () -> service.save(null, "collectionId"));
        }

        @Test
        @Order(2)
        @DisplayName("save with wrong userId")
        public void saveWithWrongUserIdTest() {
            assertThrows(IllegalArgumentException.class, () -> service.save("wrong_user_id", collection.getId().toString()));
        }

        @Test
        @Order(3)
        @DisplayName("save with null collectionId")
        public void saveWithNullCollectionIdTest() {
            assertThrows(NullPointerException.class, () -> service.save("userId", null));
        }

        @Test
        @Order(4)
        @DisplayName("save with wrong collectionId")
        public void saveWithWrongCollectionIdTest() {
            assertThrows(IllegalArgumentException.class, () -> service.save(UUID.randomUUID().toString(), "wrong_collection_id"));
        }

        @Test
        @Order(5)
        @DisplayName("save with not existing collection")
        public void saveWithNotExistingCollectionTest() throws NotAllowException {
            assertThrows(SourceNotFoundException.class, () -> service.save(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            try {
                service.save(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(6)
        @DisplayName("save star")
        public void saveStarTest() throws SourceNotFoundException, NotAllowException {
            service.save(collection.getOwnerId().toString(), collection.getId().toString());
        }

        @Test
        @Order(7)
        @DisplayName("save existing star")
        public void saveExistingStarTest() {
            assertThrows(NotAllowException.class, () -> service.save(collection.getOwnerId().toString(), collection.getId().toString()));
        }
    }

    @Nested
    @DisplayName("delete")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class DeleteTests {
        @Test
        @Order(1)
        @DisplayName("delete with null userId")
        public void deleteWithNullUserIdTest() {
            assertThrows(NullPointerException.class, () -> service.delete(null, "collectionId"));
        }

        @Test
        @Order(2)
        @DisplayName("delete with wrong userId")
        public void deleteWithWrongUserIdTest() {
            assertThrows(IllegalArgumentException.class, () -> service.delete("wrong_user_id", collection.getId().toString()));
        }

        @Test
        @Order(3)
        @DisplayName("delete with null collectionId")
        public void deleteWithNullCollectionIdTest() {
            assertThrows(NullPointerException.class, () -> service.delete("userId", null));
        }

        @Test
        @Order(4)
        @DisplayName("delete with wrong collectionId")
        public void deleteWithWrongCollectionIdTest() {
            assertThrows(IllegalArgumentException.class, () -> service.delete(UUID.randomUUID().toString(), "wrong_collection_id"));
        }

        @Test
        @Order(5)
        @DisplayName("delete not existing collection")
        public void deleteNotExistingCollectionTest() {
            assertThrows(SourceNotFoundException.class, () -> service.delete(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            try {
                service.delete(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }

        @Test
        @Order(6)
        @DisplayName("delete existing star")
        public void deleteExistingStarTest() throws SourceNotFoundException {
            service.delete(collection.getOwnerId().toString(), collection.getId().toString());
        }

        @Test
        @Order(7)
        @DisplayName("delete just deleted star")
        public void deleteJustDeletedStarTest() {
            assertThrows(SourceNotFoundException.class, () -> service.delete(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            try {
                service.delete(collection.getOwnerId().toString(), collection.getId().toString());
            } catch (SourceNotFoundException e) {
                assertThat(e.getSource(), Matchers.equalTo(CollectionService.SOURCE_NAME));
            }
        }
    }

    @AfterAll
    public static void destroy(@Autowired CollectionScopeRepository collectionScopeRepository, @Autowired CollectionRepository collectionRepository) {
        collectionRepository.delete(collection);
        collectionScopeRepository.delete(scope);
    }
}
