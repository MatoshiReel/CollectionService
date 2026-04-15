package ru.reel.CollectionService.service.criteria;

import ru.reel.CollectionService.entity.Collection;
import ru.reel.CollectionService.service.exception.UnsuitableCriteriaValueException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CollectionCriteria {
    public static SorterBuilder sort(List<Collection> list) {
        return new SorterBuilder(list);
    }

    public static PageBuilder page(List<Collection> list) {
        return new PageBuilder(list);
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

        public SorterBuilder field(String field) throws UnsuitableCriteriaValueException {
            if(field == null)
                return this;
            try {
                return switch (SortedField.valueOf(field.toUpperCase())) {
                    case ORDER -> this.order();
                    case CREATED_AT -> this.createdAt();
                    case NAME -> this.name();
                };
            } catch (IllegalArgumentException e) {
                throw new UnsuitableCriteriaValueException("sort.field", Arrays.stream(SortedField.values()).map(Enum::name).toList());
            }
        }

        public SorterBuilder order(String orderType) throws UnsuitableCriteriaValueException {
            if(orderType == null)
                return this;
            try {
                return switch(OrderType.valueOf(orderType.toUpperCase())) {
                    case OrderType.DESC -> this.desc();
                    case OrderType.ASC -> this.asc();
                };
            } catch (IllegalArgumentException e) {
                throw new UnsuitableCriteriaValueException("sort.order", Arrays.stream(OrderType.values()).map(Enum::name).toList());
            }
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

        private enum SortedField {
            ORDER,
            CREATED_AT,
            NAME
        }

        private enum OrderType {
            ASC,
            DESC
        }
    }

    public static class PageBuilder {
        private List<Collection> list;

        public PageBuilder(List<Collection> list) {
            this.list = list;
        }

        public List<Collection> get(short index, short itemsCount) {
            if(index >= 0 && itemsCount >= 0)
                this.list = this.list.stream().skip(index*itemsCount).limit(itemsCount).toList();
            return this.list;
        }
    }
}
