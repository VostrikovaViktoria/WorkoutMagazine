package ru.consist.task3.repository.specification;

public interface Specification<T> {
    boolean specified(T object);
}
