package ru.reel.CollectionService.service.sort;

import org.springframework.stereotype.Component;
import ru.reel.CollectionService.entity.Collection;

import java.util.Comparator;
import java.util.List;

public class CollectionSorter {
    public static SorterBuilder sort(List<Collection> list) {
        return new SorterBuilder(list);
    }

    public static class SorterBuilder {
        private List<Collection> list;
        private boolean isReversed = false;
        private boolean isSorted = false;

        public SorterBuilder(List<Collection> list) {
            this.list = list;
        }

        public SorterBuilder by() {
            return this;
        }

        public SorterBuilder and() {
            return this;
        }

        public SorterBuilder or() {
            return this;
        }

        public SorterBuilder order() {
            if(!isReversed) {
                this.list.sort(Comparator.comparingInt(Collection::getOrder));
                this.isSorted = true;
            }
            return this;
        }

        public SorterBuilder order(boolean isActivated) {
            if(isActivated)
                return this.order();
            return this;
        }

        public SorterBuilder createdAt() {
            if(!isReversed) {
                this.list.sort(Comparator.comparing(Collection::getCreatedAt));
                this.isSorted = true;
            }
            return this;
        }

        public SorterBuilder createdAt(boolean isActivated) {
            if(isActivated)
                return this.createdAt();
            return this;
        }

        public SorterBuilder name() {
            if(!isReversed) {
                this.list.sort(Comparator.comparing(Collection::getName));
                this.isSorted = true;
            }
            return this;
        }

        public SorterBuilder name(boolean isActivated) {
            if(isActivated)
                return this.name();
            return this;
        }

        public SorterBuilder field(SortedField field) {
            return switch (field) {
                case ORDER -> this.order();
                case CREATED_AT -> this.createdAt();
                case NAME -> this.name();
            };
        }

        public SorterBuilder order(OrderType orderType) {
            return switch(orderType) {
                case OrderType.DESC -> this.desc();
                case OrderType.ASC -> this.asc();
            };
        }

        public SorterBuilder desc() {
            if(!isReversed && isSorted) {
                this.list = this.list.reversed();
                this.isReversed = true;
            }
            return this;
        }

        public SorterBuilder asc() {
            if(isReversed && isSorted) {
                this.list = this.list.reversed();
                this.isReversed = false;
            }
            return this;
        }

        public List<Collection> get() {
            return this.list;
        }
    }

    public enum SortedField {
        ORDER,
        CREATED_AT,
        NAME
    }

    public enum OrderType {
        ASC,
        DESC
    }
}
