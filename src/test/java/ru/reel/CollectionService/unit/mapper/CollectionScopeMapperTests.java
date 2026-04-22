package ru.reel.CollectionService.unit.mapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.CollectionScopeDto;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.entity.CollectionScope;
import ru.reel.CollectionService.mapper.CollectionScopeMapper;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection scope mapper")
public class CollectionScopeMapperTests {
    @Autowired
    public CollectionScopeMapper mapper;

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
            CollectionScopeDto dto = new CollectionScopeDto();
            CollectionScope entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getTitle(), Matchers.nullValue());
            assertThat(entity.getPriority(), Matchers.equalTo((short)0));
            assertThat(entity.getCollections(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full DTO to Entity")
        public void convertFullDtoObjTest() {
            String id = UUID.randomUUID().toString();

            CollectionScopeDto dto = new CollectionScopeDto();
            dto.id = id;
            dto.title = "name";
            dto.priority = 1;

            CollectionScope entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.equalTo(UUID.fromString(id)));
            assertThat(entity.getTitle(), Matchers.equalTo("name"));
            assertThat(entity.getPriority(), Matchers.equalTo((short)1));
            assertThat(entity.getCollections(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert DTO with bad UUID format to Entity")
        public void convertDtoObjWithBadUuidTest() {
            CollectionScopeDto dto = new CollectionScopeDto();
            dto.id = "some_text";
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
            CollectionScope entity = new CollectionScope();
            CollectionScopeDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.nullValue());
            assertThat(dto.title, Matchers.nullValue());
            assertThat(dto.priority, Matchers.equalTo((short) 0));
        }

        @Test
        @Order(3)
        @DisplayName("Convert full Entity to DTO")
        public void convertFullEntityObjTest() {
            UUID id = UUID.randomUUID();

            CollectionScope entity = new CollectionScope();
            entity.setId(id);
            entity.setTitle("name");
            entity.setPriority((short)1);
            entity.setCollections(Set.of(new Collection(), new Collection()));

            CollectionScopeDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.title, Matchers.equalTo("name"));
            assertThat(dto.priority, Matchers.equalTo((short) 1));
        }
    }
}
