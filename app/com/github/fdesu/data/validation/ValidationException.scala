package com.github.fdesu.data.validation

final case class ValidationException(private val msg: String) extends RuntimeException(msg) {
}
