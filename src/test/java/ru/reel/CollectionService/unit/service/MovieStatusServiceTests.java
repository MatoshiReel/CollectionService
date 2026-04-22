package ru.reel.CollectionService.unit.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.repository.MovieStatusRepository;
import ru.reel.CollectionService.service.MovieStatusService;
import ru.reel.CollectionService.service.exception.SourceNotFoundException;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("movie status service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieStatusServiceTests {
    @Autowired
    private MovieStatusService service;
    private static MovieStatus status1;
    private static MovieStatus status2;

    @BeforeAll
    static void init(@Autowired MovieStatusRepository repository) {
        status1 = new MovieStatus();
        status1.setName("Watching");
        status1.setOrder((short)1);
        repository.save(status1);
        status2 = new MovieStatus();
        status2.setName("In Plans");
        status2.setOrder((short)2);
        repository.save(status2);
    }

    @Test
    @Order(1)
    @DisplayName("get all")
    public void getAllTest() {
        assertThat(service.getAll(), Matchers.not(Matchers.empty()));
    }

    @Test
    @Order(2)
    @DisplayName("get by null id")
    public void getByNullIdTest() {
        assertThrows(NullPointerException.class, () -> service.getById(null));
    }

    @Test
    @Order(3)
    @DisplayName("get by wrong id")
    public void getByWrongIdTest() {
        assertThrows(IllegalArgumentException.class, () -> service.getById("wrong_id"));
    }

    @Test
    @Order(4)
    @DisplayName("get by valid id but not existing status")
    public void getByValidIdNotExistingStatusTest() {
        assertThrows(SourceNotFoundException.class, () -> service.getById(UUID.randomUUID().toString()));
        try {
            service.getById(UUID.randomUUID().toString());
        } catch (SourceNotFoundException e) {
            assertThat(e.getSource(), Matchers.equalTo(MovieStatusService.SOURCE_NAME));
        }
    }

    @Test
    @Order(5)
    @DisplayName("get by valid id")
    public void getByValidIdTest() throws SourceNotFoundException {
        assertThat(service.getById(status1.getId().toString()), Matchers.notNullValue());
    }

    @Test
    @Order(6)
    @DisplayName("get by order but not existing status")
    public void getByValidOrderNotExistingStatusTest() {
        assertThrows(SourceNotFoundException.class, () -> service.getByOrder((short) 0));
        try {
            service.getByOrder((short) 0);
        } catch (SourceNotFoundException e) {
            assertThat(e.getSource(), Matchers.equalTo(MovieStatusService.SOURCE_NAME));
        }
    }

    @Test
    @Order(7)
    @DisplayName("get by valid priority")
    public void getByValidOrderTest() throws SourceNotFoundException {
        assertThat(service.getByOrder((short) 1), Matchers.notNullValue());
    }

    @AfterAll
    public static void destroy(@Autowired MovieStatusRepository repository) {
        repository.delete(status1);
        repository.delete(status2);
    }
}

