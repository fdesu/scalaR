package com.github.fdesu.data.repo

import java.util.stream

trait CrudRepo[T, I] {

  def findById(id: I): T

  def all: stream.Stream[T]

  def persist(entity: T): I

  def update(entity: T): T

  def delete(id: I): Unit

}
