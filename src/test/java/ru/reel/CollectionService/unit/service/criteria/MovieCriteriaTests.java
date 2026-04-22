package ru.reel.CollectionService.unit.service.criteria;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.entity.MovieStatus;
import ru.reel.CollectionService.service.criteria.MovieCriteria;
import ru.reel.CollectionService.service.exception.UnsuitableCriteriaValueException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("movie criteria")
public class MovieCriteriaTests {
    @Nested
    @DisplayName("sort")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SortTests {
        @Test
        @Order(1)
        @DisplayName("sort null list")
        public void sortNullListTest() {
            assertThrows(NullPointerException.class, () -> MovieCriteria.sort(null).get());
        }

        @Test
        @Order(2)
        @DisplayName("sort empty list")
        public void sortEmptyListTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.sort(new ArrayList<>()).field("rating").get(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("sort list by null field")
        public void sortListByNullFieldTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.sort(new ArrayList<>()).field(null).get(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("sort list by wrong field")
        public void sortListByWrongFieldTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.sort(new ArrayList<>()).field("wrong_field").get());
        }

        @Test
        @Order(5)
        @DisplayName("sort list by rating field")
        public void sortListByOrderFieldTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            Movie movie4 = new Movie();
            movie4.setOwnerRating(2.2);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            List<Movie> sortedList = MovieCriteria.sort(list).field("rating").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOwnerRating() < sortedList.get(1).getOwnerRating(), Matchers.equalTo(true));
        }

        @Test
        @Order(6)
        @DisplayName("sort list by status field")
        public void sortListByCreatedAtFieldTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            MovieStatus status1 = new MovieStatus();
            status1.setOrder((short)3);
            movie1.setStatus(status1);
            Movie movie2 = new Movie();
            MovieStatus status2 = new MovieStatus();
            status2.setOrder((short)1);
            movie2.setStatus(status2);
            Movie movie3 = new Movie();
            MovieStatus status3 = new MovieStatus();
            status3.setOrder((short)4);
            movie3.setStatus(status1);
            Movie movie4 = new Movie();
            MovieStatus status4 = new MovieStatus();
            status4.setOrder((short)2);
            movie4.setStatus(status4);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            List<Movie> sortedList = MovieCriteria.sort(list).field("status").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getStatus().getOrder() < sortedList.get(1).getStatus().getOrder(), Matchers.equalTo(true));
        }

        @Test
        @Order(8)
        @DisplayName("order list by null type")
        public void orderListByNullTypeTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.sort(new ArrayList<>()).order(null).get(), Matchers.empty());
        }

        @Test
        @Order(9)
        @DisplayName("order list by wrong type")
        public void orderListByEmptyTypeTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.sort(new ArrayList<>()).order("wrong_type").get());
        }

        @Test
        @Order(10)
        @DisplayName("sort and order list by asc")
        public void orderListByAscTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            Movie movie4 = new Movie();
            movie4.setOwnerRating(2.2);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            List<Movie> sortedList = MovieCriteria.sort(list).field("rating").order("asc").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOwnerRating() < sortedList.get(1).getOwnerRating(), Matchers.equalTo(true));
        }

        @Test
        @Order(11)
        @DisplayName("sort and order list by desc")
        public void orderListByDescTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            Movie movie4 = new Movie();
            movie4.setOwnerRating(2.2);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            List<Movie> sortedList = MovieCriteria.sort(list).field("rating").order("desc").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOwnerRating() > sortedList.get(1).getOwnerRating(), Matchers.equalTo(true));
        }
    }

    @Nested
    @DisplayName("filter")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class FilterTests {
        @Test
        @Order(1)
        @DisplayName("filter null list")
        public void filterNullListTest() {
            assertThrows(NullPointerException.class, () -> MovieCriteria.filter(null).get());
        }

        @Test
        @Order(2)
        @DisplayName("filter empty list")
        public void filterEmptyListTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.filter(new ArrayList<>()).status("1").get(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("filter list by wrong status value")
        public void filterListByWrongStatusValueTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.filter(new ArrayList<>()).status("wrong_value", "1").get());
            try {
                MovieCriteria.filter(new ArrayList<>()).status("wrong_value").get();
            } catch (UnsuitableCriteriaValueException e) {
                assertThat(e.getProperty(), Matchers.equalTo("filter.value"));
            }
        }

        @Test
        @Order(4)
        @DisplayName("filter list by wrong status field")
        public void filterListByWrongStatusFieldTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.filter(new ArrayList<>()).status("wrong_field", "1").get());
            try {
                MovieCriteria.filter(new ArrayList<>()).status("wrong_field", "1").get();
            } catch (UnsuitableCriteriaValueException e) {
                assertThat(e.getProperty(), Matchers.equalTo("filter.field"));
            }
        }

        @Test
        @Order(5)
        @DisplayName("filter list by include type and valid status value")
        public void filterListByIncludeTypeAndValidStatusValueTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            MovieStatus status1 = new MovieStatus();
            status1.setOrder((short)3);
            movie1.setStatus(status1);
            Movie movie2 = new Movie();
            MovieStatus status2 = new MovieStatus();
            status2.setOrder((short)1);
            movie2.setStatus(status2);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
            }};

            assertThat(MovieCriteria.filter(list).include().status("1").get().contains(movie1), Matchers.equalTo(false));
            assertThat(MovieCriteria.filter(list).include().status("1").get().contains(movie2), Matchers.equalTo(true));
        }

        @Test
        @Order(6)
        @DisplayName("filter list by exclude type and valid status value")
        public void filterListByExcludeTypeAndValidStatusValueTest() throws UnsuitableCriteriaValueException {
            Movie movie1 = new Movie();
            MovieStatus status1 = new MovieStatus();
            status1.setOrder((short)3);
            movie1.setStatus(status1);
            Movie movie2 = new Movie();
            MovieStatus status2 = new MovieStatus();
            status2.setOrder((short)1);
            movie2.setStatus(status2);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
            }};

            assertThat(MovieCriteria.filter(list).exclude().status("1").get().contains(movie1), Matchers.equalTo(true));
            assertThat(MovieCriteria.filter(list).exclude().status("1").get().contains(movie2), Matchers.equalTo(false));
        }

        @Test
        @Order(7)
        @DisplayName("filter list by null type")
        public void filterListByNullTypeTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.filter(new ArrayList<>()).type(null).get(), Matchers.empty());
        }

        @Test
        @Order(8)
        @DisplayName("filter list by wrong type")
        public void filterListByWrongTypeTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.filter(new ArrayList<>()).type("wrong_type").get());
        }

        @Test
        @Order(9)
        @DisplayName("filter list by null rating field")
        public void filterListByNullRatingFieldTest() throws UnsuitableCriteriaValueException {
            assertThat(MovieCriteria.filter(new ArrayList<>()).rating(null, null, null).get(), Matchers.empty());
        }

        @Test
        @Order(10)
        @DisplayName("filter list by wrong rating field")
        public void filterListByWrongRatingFieldTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> MovieCriteria.filter(new ArrayList<>()).status("wrong_field", "1").get());
            try {
                MovieCriteria.filter(new ArrayList<>()).rating("wrong_field", 1.1, 1.1).get();
            } catch (UnsuitableCriteriaValueException e) {
                assertThat(e.getProperty(), Matchers.equalTo("filter.field"));
            }
        }

        @Test
        @Order(11)
        @DisplayName("filter list by rating and valid gte")
        public void filterListByRatingAndValidGteTest() {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
            }};

            List<Movie> filteredList = MovieCriteria.filter(list).rating(2.0, null).get();
            assertThat(filteredList, Matchers.not(Matchers.empty()));
            assertThat(filteredList.contains(movie2), Matchers.equalTo(false));
            assertThat(filteredList.contains(movie1), Matchers.equalTo(true));
            assertThat(filteredList.contains(movie3), Matchers.equalTo(true));
        }

        @Test
        @Order(12)
        @DisplayName("filter list by rating and valid lte")
        public void filterListByRatingAndValidLteTest() {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
            }};

            List<Movie> filteredList = MovieCriteria.filter(list).rating(null, 4.0).get();
            assertThat(filteredList, Matchers.not(Matchers.empty()));
            assertThat(filteredList.contains(movie2), Matchers.equalTo(true));
            assertThat(filteredList.contains(movie1), Matchers.equalTo(true));
            assertThat(filteredList.contains(movie3), Matchers.equalTo(false));
        }

        @Test
        @Order(13)
        @DisplayName("filter list by rating and valid gte, lte")
        public void filterListByRatingAndValidGteAndValidLteTest() {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
            }};

            List<Movie> filteredList = MovieCriteria.filter(list).rating(2.0, 4.0).get();
            assertThat(filteredList, Matchers.not(Matchers.empty()));
            assertThat(filteredList.contains(movie2), Matchers.equalTo(false));
            assertThat(filteredList.contains(movie1), Matchers.equalTo(true));
            assertThat(filteredList.contains(movie3), Matchers.equalTo(false));
        }

        @Test
        @Order(13)
        @DisplayName("filter list by rating and valid gte, lte, but gte > lte")
        public void filterListByRatingAndValidGteAndValidLessLteTest() {
            Movie movie1 = new Movie();
            movie1.setOwnerRating(3.3);
            Movie movie2 = new Movie();
            movie2.setOwnerRating(1.1);
            Movie movie3 = new Movie();
            movie3.setOwnerRating(4.4);
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
            }};

            List<Movie> filteredList = MovieCriteria.filter(list).rating(4.0, 2.0).get();
            assertThat(filteredList, Matchers.empty());
        }
    }

    @Nested
    @DisplayName("page")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class PageTests {
        @Test
        @Order(1)
        @DisplayName("paging null list")
        public void pagingNullListTest() {
            assertThrows(NullPointerException.class, () -> MovieCriteria.page(null).get((short)0, (short)0));
        }

        @Test
        @Order(2)
        @DisplayName("paging empty list")
        public void pagingEmptyListTest() {
            assertThat(MovieCriteria.page(new ArrayList<>()).get((short)0, (short)0), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("paging list with signed index")
        public void pagingListWithSignedIndexTest() {
            Movie movie1 = new Movie();
            Movie movie2 = new Movie();
            Movie movie3 = new Movie();
            Movie movie4 = new Movie();
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            assertThat(MovieCriteria.page(list).get((short)-1, (short)0).size(), Matchers.equalTo(4));
        }

        @Test
        @Order(4)
        @DisplayName("paging list with signed items count")
        public void pagingListWithSignedItemsCountTest() {
            Movie movie1 = new Movie();
            Movie movie2 = new Movie();
            Movie movie3 = new Movie();
            Movie movie4 = new Movie();
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            assertThat(MovieCriteria.page(list).get((short)0, (short)-1).size(), Matchers.equalTo(4));
        }

        @Test
        @Order(5)
        @DisplayName("paging list")
        public void pagingListTest() {
            Movie movie1 = new Movie();
            Movie movie2 = new Movie();
            Movie movie3 = new Movie();
            Movie movie4 = new Movie();
            List<Movie> list = new ArrayList<>() {{
                add(movie1);
                add(movie2);
                add(movie3);
                add(movie4);
            }};

            assertThat(MovieCriteria.page(list).get((short)1, (short)2).size(), Matchers.equalTo(2));
        }
    }
}
