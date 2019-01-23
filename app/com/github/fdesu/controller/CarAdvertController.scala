package com.github.fdesu.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fdesu.data.repo.CarAdvertRepo
import javax.inject.{Inject, Singleton}
import play.api.mvc._

@Singleton
class CarAdvertController @Inject()(
                       cc: ControllerComponents,
                       repo: CarAdvertRepo,
                       mapper: ObjectMapper
                     ) extends AbstractController(cc) {

  def allAdverts(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }

  def getCarAdvertById(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }

  def addNewAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }

  def updateAdvert(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }

  def removeAdvert(id: Long): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    InternalServerError
  }

}
