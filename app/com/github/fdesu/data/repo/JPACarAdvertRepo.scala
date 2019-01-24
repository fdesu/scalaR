package com.github.fdesu.data.repo

import java.util.concurrent.CompletionStage
import java.util.stream

import com.github.fdesu.data.model.CarAdvert
import javax.inject.Inject
import play.db.jpa.JPAApi

class JPACarAdvertRepo @Inject()(jpaApi: JPAApi) extends CarAdvertRepo {

    override def findById(id: java.lang.Long): CompletionStage[CarAdvert] = {
        null
    }

    override def all(): CompletionStage[stream.Stream[CarAdvert]] = {
        null
    }

    override def persist(entity: CarAdvert): java.lang.Long = {
        null
    }

    override def update(entity: CarAdvert): CompletionStage[CarAdvert] = {
        null
    }

    override def delete(id: java.lang.Long): Unit = {
        null
    }

}
