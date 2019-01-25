package com.github.fdesu.configure

import com.github.fdesu.controller.BadResponse
import javax.inject.Singleton
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

/**
  * Custom error handler for server-side errors, that avoids
  * stacktraces leakage. Typically returns description of the
  * problem in the [[InternalServerError]]'s body
  */
@Singleton
class ServerErrorHandler extends HttpErrorHandler {

    /**
      * Apply no handling
      * @param request client request
      * @param statusCode error's status code
      * @param message error message
      * @return Unchanged client error result
      */
    override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
        Future.successful(
            Status(statusCode)(message)
        )
    }

    /**
      * Handles server-side errors. Wraps exception message into
      * [[com.github.fdesu.controller.BadResponse]] entity and returns to client for reporting.
      * @param request The request that triggered the server error
      * @param exception The server error
      * @return internal server error (500) with error description
      */
    override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
        Future.successful(
            InternalServerError(Json.toJson(BadResponse(exception.getMessage)))
        )
    }

}
