package com.github.fdesu.data.repo

import java.util.stream

import com.github.fdesu.data.model.CarAdvert
import javax.inject.{Inject, Singleton}
import play.db.jpa.JPAApi

@Singleton
class JPACarAdvertRepo @Inject()(jpaApi: JPAApi) extends CarAdvertRepo {

    val entityClass: Class[CarAdvert] = classOf[CarAdvert]

    override def findById(id: Long): CarAdvert = {
        jpaApi.withTransaction(() => {
            jpaApi.em().find(entityClass, id)
        })
    }

    override def all(): stream.Stream[CarAdvert] = {
        jpaApi.withTransaction(() => {
            val query = jpaApi.em().getCriteriaBuilder.createQuery(entityClass)
            val root = query from entityClass

            jpaApi.em().createQuery(query.select(root)).getResultList.stream()
        })
    }

    override def persist(entity: CarAdvert): Long = {
        jpaApi.withTransaction(() => {
            jpaApi.em().persist(entity)
            entity.getId
        })
    }

    override def update(entity: CarAdvert): CarAdvert = {
        jpaApi.withTransaction(() => {
            jpaApi.em().merge(entity)
        })
    }

    override def delete(id: Long): Unit = {
        jpaApi.withTransaction(() => {
            jpaApi.em().remove(jpaApi.em().getReference(entityClass, id))
            null
        })
    }

}
