package ru.reel.CollectionService.mapper;

public interface Mapper<D, E> {
    E from(D dto);
    D to(E entity);
}
