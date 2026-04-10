package ru.reel.CollectionService.service.issue;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class RequestIssue {
    protected String message;
}
