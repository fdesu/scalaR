package com.github.fdesu.data.repo

import java.util.stream

/**
  * Exposes CRUD functionality for entity persistence
  * @tparam T entity generic type
  * @tparam I entity's id generic type
  */
trait CrudRepo[T, I] {

  /**
    * Find entity by id
    * @param id id of the entity
    * @return entity from the backing storage
    */
  def findById(id: I): T

  /**
    * Export all the entities of particular type
    * @return all the entities
    */
  def all: stream.Stream[T]

  /**
    * Persist new entity
    * @param entity entity to persist
    * @return persisted entity's id
    */
  def persist(entity: T): I

  /**
    * Makes entity managed and udpates it's database state
    * @param entity entity to update
    * @return udpated entity
    */
  def update(entity: T): T

  /**
    * Deletes the entity from the database by id
    * @param id id of the entity
    */
  def delete(id: I): Unit

}
