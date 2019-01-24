package com.github.fdesu.data.repo;

import java.util.stream.Stream;


public interface CrudRepo<T, I> {

    T findById(I id);

    Stream<T> all();

    I persist(T entity);

    T update(T entity);

    void delete(I id);

}
