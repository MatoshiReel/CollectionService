package ru.reel.CollectionService.unit.mapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.dto.MovieStatusDto;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.mapper.MovieStatusMapper;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("movie status mapper")
public class MovieStatusMapperTests {
    @Autowired
    private MovieStatusMapper mapper;

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
            MovieStatusDto dto = new MovieStatusDto();
            MovieStatus entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.nullValue());
            assertThat(entity.getName(), Matchers.nullValue());
            assertThat(entity.getOrder(), Matchers.equalTo((short)0));
            assertThat(entity.getMovies(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("Convert full DTO to Entity")
        public void convertFullDtoObjTest() {
            String id = UUID.randomUUID().toString();

            MovieStatusDto dto = new MovieStatusDto();
            dto.id = id;
            dto.name = "name";
            dto.order = 1;

            MovieStatus entity = mapper.from(dto);
            assertThat(entity, Matchers.notNullValue());
            assertThat(entity.getId(), Matchers.equalTo(UUID.fromString(id)));
            assertThat(entity.getName(), Matchers.equalTo("name"));
            assertThat(entity.getOrder(), Matchers.equalTo((short)1));
            assertThat(entity.getMovies(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("Convert DTO with bad UUID format to Entity")
        public void convertDtoObjWithBadUuidTest() {
            MovieStatusDto dto = new MovieStatusDto();
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
            MovieStatus entity = new MovieStatus();
            MovieStatusDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.nullValue());
            assertThat(dto.name, Matchers.nullValue());
            assertThat(dto.order, Matchers.equalTo((short) 0));
        }

        @Test
        @Order(3)
        @DisplayName("Convert full Entity to DTO")
        public void convertFullEntityObjTest() {
            UUID id = UUID.randomUUID();

            MovieStatus entity = new MovieStatus();
            entity.setId(id);
            entity.setName("name");
            entity.setOrder((short)1);
            entity.setMovies(Set.of(new Movie(), new Movie()));

            MovieStatusDto dto = mapper.to(entity);
            assertThat(dto, Matchers.notNullValue());
            assertThat(dto.id, Matchers.equalTo(id.toString()));
            assertThat(dto.name, Matchers.equalTo("name"));
            assertThat(dto.order, Matchers.equalTo((short) 1));
        }
    }
}