package com.github.fdesu.controller

import java.util.stream.Collectors

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fdesu.controller.validation.{CarAdvertValidator, ValidationException}
import com.github.fdesu.data.repo.CarAdvertRepo
import javax.inject.{Inject, Singleton}
import javax.persistence.EntityNotFoundException
import play.api.libs.json._
import play.api.mvc.{Action, _}

@Singleton
class CarAdvertController @Inject()(cc: ControllerComponents,
                                    repo: CarAdvertRepo,
                                    mapper: ObjectMapper,
                                    validator: CarAdvertValidator) extends AbstractController(cc) {

    def allAdverts(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(mapper.writeValueAsString(
            repo.all.collect(Collectors.toList())
        ))
    }

    def getCarAdvertById(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val advert = repo.findById(id)

        if (advert == null) {
            NotFound
        } else {
            Ok(Json.toJson(CarAdvertResource.fromAdvert(advert)))
        }
    }

    def addNewAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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
    }

    def handleNewAdvert(advertResource: CarAdvertResource): Result = {
        try {
            val advert = CarAdvertResource.toAdvert(advertResource)

            validator validate advert
            repo persist advert

            Ok(Json.toJson(IdResponse(advert.getId)))
        } catch {
            case e: ValidationException =>
                BadRequest(Json.toJson(mapper.writeValueAsString(
                    Json.toJson(BadResponse(e.getMessage))
                )))
        }
    }

    def updateAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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
