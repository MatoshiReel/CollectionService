package ru.reel.CollectionService.service.validator;

import java.util.List;

public interface Validator<O, R> {
    List<R> validateBeforeCreating(O object);
    List<R> validateBeforeUpdating(O object);
}
