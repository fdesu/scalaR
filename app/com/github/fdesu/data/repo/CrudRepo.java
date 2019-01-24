package com.github.fdesu.data.repo;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;


public interface CrudRepo<T, I> {

    CompletionStage<T> findById(I id);

    CompletionStage<Stream<T>> all();

    I persist(T entity);

    CompletionStage<T> update(T entity);

    void delete(I id);

}
