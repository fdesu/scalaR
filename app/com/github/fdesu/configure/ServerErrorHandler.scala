package com.github.fdesu.configure

import com.github.fdesu.controller.BadResponse
import javax.inject.Singleton
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

@Singleton
class ServerErrorHandler extends HttpErrorHandler {

    override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
        Future.successful(
            Status(statusCode)("A client error occurred: " + message)
        )
    }

    override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
        Future.successful(
            InternalServerError(Json.toJson(BadResponse(exception.getMessage)))
        )
    }

}
