package com.github.fdesu.controller.validation

/**
  * Thrown in case validation unsuccessful
  * @param msg description of what caused validation to fail
  */
final case class ValidationException(private val msg: String) extends RuntimeException(msg) {}
