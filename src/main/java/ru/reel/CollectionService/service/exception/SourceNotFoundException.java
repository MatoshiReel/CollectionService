package ru.reel.CollectionService.service.exception;

import lombok.Getter;

@Getter
public class SourceNotFoundException extends Exception {
    private final String source;

    public SourceNotFoundException(String source) {
        super();
        this.source = source;
    }

    public SourceNotFoundException(String message, String source) {
        super(message);
        this.source = source;
    }
}
