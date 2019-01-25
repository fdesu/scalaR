package com.github.fdesu.controller

import com.github.fdesu.controller.validation.{CarAdvertValidator, ValidationException}
import com.github.fdesu.data.repo.CarAdvertRepo
import javax.inject.{Inject, Singleton}
import javax.persistence.EntityNotFoundException
import play.api.libs.json._
import play.api.mvc.{Action, _}

import scala.collection.JavaConverters._

/**
  * Controller that serves /&lt;api_version&gt;/car/ REST requests
  *
  * @param cc controller components dependencies that most controllers rely on
  * @param repo car adverts' persistence layer
  * @param mapper jackson mapper
  * @param validator validates incoming car advert-related input
  */
@Singleton
class CarAdvertController @Inject()(cc: ControllerComponents,
                                    repo: CarAdvertRepo,
                                    validator: CarAdvertValidator) extends AbstractController(cc) {

    /**
      * Serves /car/all requests. Exports all the persisted [[com.github.fdesu.data.model.CarAdvert]]s
      * @return all car advert instances
      */
    def allAdverts(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val list: List[CarAdvertResource] = repo.all
            .iterator()
            .asScala
            .map(adv => CarAdvertResource.fromAdvert(adv))
            .toList

        Ok(Json toJson list)
    }

    /**
      * Serves GET /car/:id requests. Exports single [[com.github.fdesu.data.model.CarAdvert]]
      * @param id car advert's id
      * @return single car advert
      */
    def getCarAdvertById(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val advert = repo findById id

        if (advert == null) {
            NotFound
        } else {
            Ok(Json.toJson(CarAdvertResource fromAdvert advert))
        }
    }

    /**
      * Serves /car/new requests. Persists single [[com.github.fdesu.data.model.CarAdvert]] instance.
      * Might throw validation errors.
      * @return persisted advert's id
      */
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

    private def handleNewAdvert(advertResource: CarAdvertResource): Result = {
        try {
            val advert = CarAdvertResource toAdvert advertResource

            validator validate advert
            repo persist advert

            Ok(Json.toJson(IdResponse(advert.getId)))
        } catch {
            case e: ValidationException => BadRequest(Json.toJson(BadResponse(e.getMessage)))
        }
    }

    /**
      * Serves /car/change requests. Updates single [[com.github.fdesu.data.model.CarAdvert]] instance.
      * Might throw validation errors.
      * @return request processing status
      */
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

    /**
      * Serves DELETE /car/:id requests. Deletes single [[com.github.fdesu.data.model.CarAdvert]] by id.
      * @param id car advert's id
      * @return request processing status
      */
    def removeAdvert(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        try {
            repo delete id
            Ok
        } catch {
            case e: EntityNotFoundException => NotFound
        }
    }

}
