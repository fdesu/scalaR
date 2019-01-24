package com.github.fdesu.controller

import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fdesu.data.repo.CarAdvertRepo
import com.github.fdesu.controller.validation.{BadResponse, CarAdvertValidator, IdResponse, ValidationException}
import javax.inject.{Inject, Singleton}
import javax.persistence.EntityNotFoundException
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.{Action, _}

@Singleton
class CarAdvertController @Inject()(cc: ControllerComponents,
                                    repo: CarAdvertRepo,
                                    mapper: ObjectMapper,
                                    validator: CarAdvertValidator) extends AbstractController(cc) {

  def allAdverts(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    try {
      Ok(mapper.writeValueAsString(
        repo.all().collect(Collectors.toList())
      ))
    } catch {
      case e: Exception =>
        Logger.error("Critical exception during all records export operation", e)
        InternalServerError
    }
  }

  def getCarAdvertById(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    try {
      val advert = repo.findById(id)

      if (advert == null) {
        NotFound
      } else {
        Ok(mapper.writeValueAsString(advert))
      }
    } catch {
      case e: Exception =>
        Logger.error("Critical exception during searching by id", e)
        InternalServerError
    }
  }

  def addNewAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    try {
      request.body.asJson.map {
        mapped =>
          val json: JsResult[CarAdvertResource] = Json.fromJson[CarAdvertResource](mapped)
          json match {
            case JsSuccess(advertResource: CarAdvertResource, path: JsPath) => handleNewAdvert(advertResource)
            case e: JsError => BadRequest("Request validation error occurred")
          }
      }.getOrElse {
        BadRequest("Expecting application/json request body")
      }
    } catch {
      case e: Exception =>
        Logger.error("Critical exception during searching by id", e)
        InternalServerError
    }
  }

  def handleNewAdvert(advertResource: CarAdvertResource): Result = {
    try {
      val advert = CarAdvertResource.toAdvert(advertResource)

      validator validate advert
      repo persist advert

      Ok(mapper.writeValueAsString(new IdResponse(advert.getId)))
    } catch {
      case e: ValidationException =>
        BadRequest(Json.toJson(mapper.writeValueAsString(
          new BadResponse(e.getMessage)
        )))
      case e: Exception =>
        Logger.error("Critical exception during the persist call", e)
        InternalServerError
    }
  }

  def updateAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    try {
      request.body.asJson.map {
        mapped =>
          val json: JsResult[CarAdvertResource] = Json.fromJson[CarAdvertResource](mapped)
          json match {
            case JsSuccess(advertResource: CarAdvertResource, path: JsPath) =>
              repo update CarAdvertResource.toAdvert(advertResource)
              Ok

            case e: JsError => BadRequest("Request validation error occurred")
          }
      }.getOrElse {
        BadRequest("Expecting application/json request body")
      }
    } catch {
      case e: Exception =>
        Logger.error("Critical exception during searching by id", e)
        InternalServerError
    }
  }

  def removeAdvert(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    try {
      repo delete id
      Ok
    } catch {
      case e: EntityNotFoundException => NotFound
      case e: Exception =>
        Logger.error("Critical exception during removal", e)
        InternalServerError
    }
  }

}
