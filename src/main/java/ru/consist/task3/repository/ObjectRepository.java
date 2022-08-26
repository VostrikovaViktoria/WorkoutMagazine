package ru.consist.task3.repository;

import ru.consist.task3.repository.specification.Specification;

import java.util.List;

public interface ObjectRepository<T> {
    List<T> query(Specification<T> specification);

    boolean create(T object);

    boolean update(T object);

    boolean delete(T object);
}
