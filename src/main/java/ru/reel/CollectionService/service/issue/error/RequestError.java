package ru.reel.CollectionService.service.issue.error;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.reel.CollectionService.service.issue.RequestIssue;

/**
 * The {@code RequestError} is a DTO class, that describes a reason of error for 4xx response status.
 * @see ErrorReason
 */
@Getter
@SuperBuilder
public class RequestError extends RequestIssue {
    protected ErrorReason errorReason;
}
