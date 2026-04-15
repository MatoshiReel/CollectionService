package ru.reel.CollectionService.service.exception;

import lombok.Getter;
import java.util.List;

@Getter
public class UnsuitableCriteriaValueException extends Exception {
    private final String property;
    private List<String> suitableValues;

    public UnsuitableCriteriaValueException(String property, List<String> suitableValues) {
        super(String.format("Criteria value of %s property isn't suit. Suitable values: %s.", property, suitableValues));
        this.property = property;
        this.suitableValues = suitableValues;
    }

    public UnsuitableCriteriaValueException(String property, String suitableValue) {
        super(String.format("Criteria value of %s property isn't suit. Suitable value: %s.", property, suitableValue));
        this.property = property;
    }
}
