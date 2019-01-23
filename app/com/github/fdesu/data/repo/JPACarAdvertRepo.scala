package com.github.fdesu.data.repo
import java.lang
import java.util.concurrent.CompletionStage
import java.util.stream

import com.github.fdesu.data.model.CarAdvert
import javax.inject.Inject
import play.db.jpa.JPAApi

class JPACarAdvertRepo @Inject()(jpaApi: JPAApi) extends CarAdvertRepo {

  override def findById(id: lang.Long): CompletionStage[CarAdvert] = {
    null
  }

  override def all(): CompletionStage[stream.Stream[CarAdvert]] = {
    null
  }

  override def persist(entity: CarAdvert): Unit = {
    null
  }

  override def update(entity: CarAdvert): CarAdvert = {
    null
  }

  override def delete(id: lang.Long): Unit = {
    null
  }

}
