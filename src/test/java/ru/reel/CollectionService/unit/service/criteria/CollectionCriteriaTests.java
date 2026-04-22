package ru.reel.CollectionService.unit.service.criteria;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.service.criteria.CollectionCriteria;
import ru.reel.CollectionService.service.exception.UnsuitableCriteriaValueException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("collection criteria")
public class CollectionCriteriaTests {
    @Nested
    @DisplayName("sort")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class SortTests {
        @Test
        @Order(1)
        @DisplayName("sort null list")
        public void sortNullListTest() {
            assertThrows(NullPointerException.class, () -> CollectionCriteria.sort(null).get());
        }

        @Test
        @Order(2)
        @DisplayName("sort empty list")
        public void sortEmptyListTest() throws UnsuitableCriteriaValueException {
            assertThat(CollectionCriteria.sort(new ArrayList<>()).field("name").get(), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("sort list by null field")
        public void sortListByNullFieldTest() throws UnsuitableCriteriaValueException {
            assertThat(CollectionCriteria.sort(new ArrayList<>()).field(null).get(), Matchers.empty());
        }

        @Test
        @Order(4)
        @DisplayName("sort list by wrong field")
        public void sortListByWrongFieldTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> CollectionCriteria.sort(new ArrayList<>()).field("wrong_field").get());
        }

        @Test
        @Order(5)
        @DisplayName("sort list by order field")
        public void sortListByOrderFieldTest() throws UnsuitableCriteriaValueException {
            Collection collection1 = new Collection();
            collection1.setOrder((short)4);
            Collection collection2 = new Collection();
            collection2.setOrder((short)1);
            Collection collection3 = new Collection();
            collection3.setOrder((short)3);
            Collection collection4 = new Collection();
            collection4.setOrder((short)2);
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            List<Collection> sortedList = CollectionCriteria.sort(list).field("order").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOrder() < sortedList.get(1).getOrder(), Matchers.equalTo(true));
        }

        @Test
        @Order(6)
        @DisplayName("sort list by created_at field")
        public void sortListByCreatedAtFieldTest() throws UnsuitableCriteriaValueException {
            Collection collection1 = new Collection();
            collection1.setCreatedAt(new Date(System.currentTimeMillis() + 1_000));
            Collection collection2 = new Collection();
            collection2.setCreatedAt(new Date(System.currentTimeMillis() + 4_000));
            Collection collection3 = new Collection();
            collection3.setCreatedAt(new Date(System.currentTimeMillis() + 3_000));
            Collection collection4 = new Collection();
            collection4.setCreatedAt(new Date(System.currentTimeMillis() + 2_000));
            List<Collection> list = new ArrayList<>() {{
                add(collection4);
                add(collection1);
                add(collection3);
                add(collection2);
            }};

            List<Collection> sortedList = CollectionCriteria.sort(list).field("created_at").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getCreatedAt().before(sortedList.get(1).getCreatedAt()), Matchers.equalTo(true));
        }

        @Test
        @Order(7)
        @DisplayName("sort list by name field")
        public void sortListByNameFieldTest() throws UnsuitableCriteriaValueException {
            Collection collection1 = new Collection();
            collection1.setName("c");
            Collection collection2 = new Collection();
            collection2.setName("a");
            Collection collection3 = new Collection();
            collection3.setName("b");
            Collection collection4 = new Collection();
            collection4.setName("d");
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            List<Collection> sortedList = CollectionCriteria.sort(list).field("name").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getName(), Matchers.equalTo("a"));
            assertThat(sortedList.get(1).getName(), Matchers.equalTo("b"));
        }

        @Test
        @Order(8)
        @DisplayName("order list by null type")
        public void orderListByNullTypeTest() throws UnsuitableCriteriaValueException {
            assertThat(CollectionCriteria.sort(new ArrayList<>()).order(null).get(), Matchers.empty());
        }

        @Test
        @Order(9)
        @DisplayName("order list by wrong type")
        public void orderListByEmptyTypeTest() {
            assertThrows(UnsuitableCriteriaValueException.class, () -> CollectionCriteria.sort(new ArrayList<>()).order("wrong_type").get());
        }

        @Test
        @Order(10)
        @DisplayName("sort and order list by asc")
        public void orderListByAscTest() throws UnsuitableCriteriaValueException {
            Collection collection1 = new Collection();
            collection1.setOrder((short)4);
            Collection collection2 = new Collection();
            collection2.setOrder((short)1);
            Collection collection3 = new Collection();
            collection3.setOrder((short)3);
            Collection collection4 = new Collection();
            collection4.setOrder((short)2);
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            List<Collection> sortedList = CollectionCriteria.sort(list).field("order").order("asc").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOrder() < sortedList.get(1).getOrder(), Matchers.equalTo(true));
        }

        @Test
        @Order(11)
        @DisplayName("sort and order list by desc")
        public void orderListByDescTest() throws UnsuitableCriteriaValueException {
            Collection collection1 = new Collection();
            collection1.setOrder((short)4);
            Collection collection2 = new Collection();
            collection2.setOrder((short)1);
            Collection collection3 = new Collection();
            collection3.setOrder((short)3);
            Collection collection4 = new Collection();
            collection4.setOrder((short)2);
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            List<Collection> sortedList = CollectionCriteria.sort(list).field("order").order("desc").get();
            assertThat(sortedList, Matchers.not(Matchers.empty()));
            assertThat(sortedList.get(0).getOrder() > sortedList.get(1).getOrder(), Matchers.equalTo(true));
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
            assertThrows(NullPointerException.class, () -> CollectionCriteria.page(null).get((short)0, (short)0));
        }

        @Test
        @Order(2)
        @DisplayName("paging empty list")
        public void pagingEmptyListTest() {
            assertThat(CollectionCriteria.page(new ArrayList<>()).get((short)0, (short)0), Matchers.empty());
        }

        @Test
        @Order(3)
        @DisplayName("paging list with signed index")
        public void pagingListWithSignedIndexTest() {
            Collection collection1 = new Collection();
            Collection collection2 = new Collection();
            Collection collection3 = new Collection();
            Collection collection4 = new Collection();
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            assertThat(CollectionCriteria.page(list).get((short)-1, (short)0).size(), Matchers.equalTo(4));
        }

        @Test
        @Order(4)
        @DisplayName("paging list with signed items count")
        public void pagingListWithSignedItemsCountTest() {
            Collection collection1 = new Collection();
            Collection collection2 = new Collection();
            Collection collection3 = new Collection();
            Collection collection4 = new Collection();
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            assertThat(CollectionCriteria.page(list).get((short)0, (short)-1).size(), Matchers.equalTo(4));
        }

        @Test
        @Order(5)
        @DisplayName("paging list")
        public void pagingListTest() {
            Collection collection1 = new Collection();
            Collection collection2 = new Collection();
            Collection collection3 = new Collection();
            Collection collection4 = new Collection();
            List<Collection> list = new ArrayList<>() {{
                add(collection1);
                add(collection2);
                add(collection3);
                add(collection4);
            }};

            assertThat(CollectionCriteria.page(list).get((short)1, (short)2).size(), Matchers.equalTo(2));
        }
    }
}