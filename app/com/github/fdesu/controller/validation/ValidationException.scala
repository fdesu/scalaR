package com.github.fdesu.controller.validation

final case class ValidationException(private val msg: String) extends RuntimeException(msg) {
}
