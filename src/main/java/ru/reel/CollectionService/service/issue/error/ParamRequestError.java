package ru.reel.CollectionService.service.issue.error;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ParamRequestError extends RequestError {
    protected String param;
}
