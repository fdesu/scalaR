package com.github.fdesu.controller

import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fdesu.data.model.CarAdvert
import com.github.fdesu.data.repo.CarAdvertRepo
import com.github.fdesu.controller.validation.{BadResponse, CarAdvertValidator, ValidationException}
import javax.inject.{Inject, Singleton}
import javax.persistence.EntityNotFoundException
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class CarAdvertController @Inject()(cc: ControllerComponents,
                                    repo: CarAdvertRepo,
                                    mapper: ObjectMapper,
                                    validator: CarAdvertValidator) extends AbstractController(cc) {

    def allAdverts(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(Json.toJson(mapper.writeValueAsString(
            repo.all().collect(Collectors.toList())
        )))
    }

    def getCarAdvertById(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val advert = repo.findById(id)

        if (advert == null) {
            NotFound
        } else {
            Ok(Json.toJson(mapper.writeValueAsString(advert)))
        }
    }

    def addNewAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val advert = mapper.readValue(request.body.asText.get, classOf[CarAdvert])

        try {
            advert setId null
            validator validate advert
            repo persist advert

            Ok
        } catch {
            case e: ValidationException =>
                BadRequest(Json.toJson(mapper.writeValueAsString(
                    new BadResponse(e.getMessage)
                )))
        }
    }

    def updateAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val advert = mapper.readValue(request.body.asText.get, classOf[CarAdvert])
        repo update advert

        Ok
    }

    def removeAdvert(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        try {
            repo delete id
            Ok
        } catch {
            case e: EntityNotFoundException => NotFound
        }
    }

}
