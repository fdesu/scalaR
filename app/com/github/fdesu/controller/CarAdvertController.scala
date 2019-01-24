package com.github.fdesu.controller

import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fdesu.data.model.CarAdvert
import com.github.fdesu.data.repo.CarAdvertRepo
import com.github.fdesu.controller.validation.{BadResponse, CarAdvertValidator, IdResponse, ValidationException}
import javax.inject.{Inject, Singleton}
import javax.persistence.EntityNotFoundException
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

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
      val advert = mapper.readValue(request.body.asText.get, classOf[CarAdvert])

      advert setId null
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
      val advert = mapper.readValue(request.body.asText.get, classOf[CarAdvert])
      repo update advert

      Ok
    } catch {
      case e: Exception =>
        Logger.error("Critical exception during update", e)
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
