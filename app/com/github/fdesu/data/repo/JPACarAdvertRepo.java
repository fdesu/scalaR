package com.github.fdesu.data.repo;

import com.github.fdesu.data.model.CarAdvert;

import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@Singleton
public class JPACarAdvertRepo implements CarAdvertRepo {


    @Override
    public CompletionStage<CarAdvert> findById(Long id) {
        return null;
    }

    @Override
    public CompletionStage<Stream<CarAdvert>> all() {
        return null;
    }

    @Override
    public void persist(CarAdvert entity) {

    }

    @Override
    public CarAdvert update(CarAdvert entity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
