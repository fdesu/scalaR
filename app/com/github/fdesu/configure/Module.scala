package com.github.fdesu.configure

import com.github.fdesu.data.repo.{CarAdvertRepo, JPACarAdvertRepo}
import com.google.inject.AbstractModule

/**
  * Helps Guice to resolve bindings.
  * It helps to avoid hard coupling with [[com.google.inject.ImplementedBy]].
  */
class Module extends AbstractModule {

    override def configure(): Unit = {
        bind(classOf[CarAdvertRepo]) to classOf[JPACarAdvertRepo]
    }

}
