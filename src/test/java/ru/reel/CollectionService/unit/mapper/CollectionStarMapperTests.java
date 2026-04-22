package ru.reel.CollectionService.unit.mapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.CollectionStarDto;
import ru.reel.CollectionService.entity.*;
import ru.reel.CollectionService.mapper.CollectionStarMapper;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection star mapper")
public class CollectionStarMapperTests {
    @Autowired
    private CollectionStarMapper mapper;

    @Nested
    @DisplayName("from")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class FromTests {
        @Test
        @Order(1)
        @DisplayName("Convert null DTO to Entity")
        public void convertNullDtoObjTest() {
            assertThat(mapper.from(null), Matchers.nullValue());
        }

        @Test
        @Order(2)
        @DisplayName("Convert empty DTO to Entity")
        public void convertEmptyDtoObjTest() {
            CollectionStarDto dto = new CollectionStarDto();
            CollectionStar entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getUserId(), Matchers.nullValue());
            assertThat(entity.getCollection(), Matchers.nullValue());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full DTO to Entity")
        public void convertFullDtoObjTest() {
            String userId = UUID.randomUUID().toString();

            CollectionStarDto dto = new CollectionStarDto();
            dto.id = UUID.randomUUID().toString();
            dto.userId = userId;
            dto.collectionId = UUID.randomUUID().toString();

            CollectionStar entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getUserId(), Matchers.equalTo(UUID.fromString(userId)));
            assertThat(entity.getCollection(), Matchers.nullValue());
        }

        @Test
        @Order(4)
        @DisplayName("Convert DTO with bad UUID format to Entity")
        public void convertDtoObjWithBadUuidTest() {
            CollectionStarDto dto = new CollectionStarDto();
            dto.userId = "some_text";
            assertThrows(IllegalArgumentException.class, () -> mapper.from(dto));
        }
    }

    @Nested
    @DisplayName("to")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class ToTests {
        @Test
        @Order(1)
        @DisplayName("Convert null Entity to DTO")
        public void convertNullEntityObjTest() {
            assertThat(mapper.to(null), Matchers.nullValue());
        }

        @Test
        @Order(2)
        @DisplayName("Convert empty Entity to DTO")
        public void convertEmptyEntityObjTest() {
            CollectionStar entity = new CollectionStar();
            CollectionStarDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.nullValue());
            assertThat(dto.userId, Matchers.nullValue());
            assertThat(dto.collectionId, Matchers.nullValue());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full Entity to DTO")
        public void convertFullEntityObjTest() {
            UUID id = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            Collection collection = new Collection();
            collection.setId(UUID.randomUUID());

            CollectionStar entity = new CollectionStar();
            entity.setId(id);
            entity.setUserId(userId);
            entity.setCollection(collection);

            CollectionStarDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.userId, Matchers.equalTo(userId.toString()));
            assertThat(dto.collectionId, Matchers.equalTo(collection.getId().toString()));
        }
    }
}
