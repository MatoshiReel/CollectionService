package ru.reel.CollectionService.service.criteria;

import ru.reel.CollectionService.entity.Movie;
import ru.reel.CollectionService.service.exception.UnsuitableCriteriaValueException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MovieCriteria {
    public static SorterBuilder sort(List<Movie> list) throws NullPointerException {
        return new SorterBuilder(list);
    }

    public static FilterBuilder filter(List<Movie> list) throws NullPointerException {
        return new FilterBuilder(list);
    }

    public static PageBuilder page(List<Movie> list) throws NullPointerException {
        return new PageBuilder(list);
    }

    public static class SorterBuilder {
        private List<Movie> list;
        private boolean isReversed = false;
        private boolean isSorted = false;

        public SorterBuilder(List<Movie> list) throws NullPointerException {
            if(list == null)
                throw new NullPointerException();
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

        public SorterBuilder rating() {
            if(!isReversed) {
                this.list.sort(Comparator.comparingDouble(Movie::getOwnerRating));
                this.isSorted = true;
            }
            return this;
        }

        public SorterBuilder rating(boolean isActivated) {
            if(isActivated)
                return this.rating();
            return this;
        }

        public SorterBuilder status() {
            if(!isReversed) {
                this.list.sort(Comparator.comparingInt((movie) -> movie.getStatus().getOrder()));
                this.isSorted = true;
            }
            return this;
        }

        public SorterBuilder status(boolean isActivated) {
            if(isActivated)
                return this.status();
            return this;
        }

        public SorterBuilder field(String field) throws UnsuitableCriteriaValueException {
            if(field == null)
                return this;
            try {
                return switch (SortedField.valueOf(field.toUpperCase())) {
                    case RATING -> this.rating();
                    case STATUS -> this.status();
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

        public List<Movie> get() {
            return this.list;
        }

        private enum SortedField {
            RATING,
            STATUS
        }

        private enum OrderType {
            ASC,
            DESC
        }
    }

    public static class FilterBuilder {
        private List<Movie> list;
        private boolean exclude = false;

        public FilterBuilder(List<Movie> list) throws NullPointerException {
            if(list == null)
                throw new NullPointerException();
            this.list = list;
        }

        public FilterBuilder by() {
            return this;
        }

        public FilterBuilder or() {
            return this;
        }

        public FilterBuilder type(String type) throws UnsuitableCriteriaValueException {
            if(type == null)
                return this;
            try {
                return switch (FilterType.valueOf(type.toUpperCase())) {
                    case INCLUDE -> this.include();
                    case EXCLUDE -> this.exclude();
                };
            } catch (IllegalArgumentException e) {
                throw new UnsuitableCriteriaValueException("filter.type", Arrays.stream(FilterType.values()).map(Enum::name).toList());
            }
        }

        public FilterBuilder include() {
            this.exclude = false;
            return this;
        }

        public FilterBuilder exclude() {
            this.exclude = true;
            return this;
        }

        public FilterBuilder status(String field, String statusOrder) throws UnsuitableCriteriaValueException {
            if(field != null) {
                try {
                    if(FilteredField.valueOf(field.toUpperCase()).equals(FilteredField.STATUS))
                        return status(statusOrder);
                } catch (IllegalArgumentException e) {
                    throw new UnsuitableCriteriaValueException("filter.field", Arrays.stream(FilteredField.values()).map(Enum::name).toList());
                }
            }
            return this;
        }

        public FilterBuilder status(String statusOrder) throws UnsuitableCriteriaValueException {
            if(statusOrder != null) {
                try {
                    if(this.exclude)
                        this.list = list.stream().filter((movie) -> movie.getStatus().getOrder() != Short.parseShort(statusOrder)).toList();
                    else
                        this.list = list.stream().filter((movie) -> movie.getStatus().getOrder() == Short.parseShort(statusOrder)).toList();
                } catch (NumberFormatException e) {
                    throw new UnsuitableCriteriaValueException("filter.value", "double variable type");
                }
            }
            return this;
        }

        public FilterBuilder rating(String field, Double gte, Double lte) throws UnsuitableCriteriaValueException {
            if(field != null) {
                try {
                    if(FilteredField.valueOf(field.toUpperCase()).equals(FilteredField.RATING))
                        return rating(gte, lte);
                } catch (IllegalArgumentException e) {
                    throw new UnsuitableCriteriaValueException("filter.field", Arrays.stream(FilteredField.values()).map(Enum::name).toList());
                }
            }
            return this;
        }

        public FilterBuilder rating(Double gte, Double lte) {
            if(gte != null)
                this.list = list.stream().filter((movie) -> movie.getOwnerRating() >= gte).toList();
            if(lte != null)
                this.list = list.stream().filter((movie) -> movie.getOwnerRating() <= lte).toList();
            return this;
        }

        public List<Movie> get() {
            return this.list;
        }

        private enum FilteredField {
            RATING,
            STATUS
        }

        private enum FilterType {
            INCLUDE,
            EXCLUDE
        }
    }

    public static class PageBuilder {
        private List<Movie> list;

        public PageBuilder(List<Movie> list) throws NullPointerException {
            if(list == null)
                throw new NullPointerException();
            this.list = list;
        }

        public List<Movie> get(short index, short itemsCount) {
            if(index >= 0 && itemsCount >= 0)
                this.list = this.list.stream().skip(index*itemsCount).limit(itemsCount).toList();
            return this.list;
        }
    }
}