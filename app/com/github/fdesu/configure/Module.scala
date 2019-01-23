package com.github.fdesu.configure

import com.github.fdesu.data.repo.{CarAdvertRepo, JPACarAdvertRepo}
import com.google.inject.AbstractModule

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[CarAdvertRepo]) to classOf[JPACarAdvertRepo]
  }

}
