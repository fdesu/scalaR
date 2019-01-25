package com.github.fdesu.controller

import java.time.LocalDate

import com.github.fdesu.data.model.{CarAdvert, Fuel}
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Mapping from Scala Json's infra [[com.github.fdesu.controller.CarAdvertResource]]
  * to JPA [[com.github.fdesu.data.model.CarAdvert]] entity
  */
case class CarAdvertResource(id: Long, title: String, fuel: Fuel,
                             price: Int, isNew: Boolean, mileage: Int,
                             registrationDate: LocalDate) {
}

object CarAdvertResource {

    implicit val implicitWrites = new Writes[CarAdvertResource] {
        def writes(ad: CarAdvertResource): JsValue = {
            Json.obj(
                "id" -> ad.id,
                "title" -> ad.title,
                "fuel" -> ad.fuel.toString,
                "price" -> ad.price,
                "new" -> ad.isNew,
                "mileage" -> ad.mileage,
                "registrationDate" -> ad.registrationDate
            )
        }
    }

    implicit val implicitReads: Reads[CarAdvertResource] = (
        (__ \ "id").read[Long] and
            (__ \ "title").read[String] and
            (__ \ "fuel").read[String].map(f => Fuel.valueOf(f)) and
            (__ \ "price").read[Int] and
            (__ \ "new").read[Boolean] and
            (__ \ "mileage").read[Int] and
            (__ \ "registrationDate").read[LocalDate]
        ) (CarAdvertResource.apply _)

    def toAdvert(ad: CarAdvertResource): CarAdvert = {
        new CarAdvert(0L, ad.title, ad.fuel, ad.price, ad.isNew, ad.mileage, ad.registrationDate)
    }

    def fromAdvert(ad: CarAdvert): CarAdvertResource = {
        CarAdvertResource(
            ad.getId, ad.getTitle, ad.getFuel,
            ad.getPrice, ad.isNew, ad.getMileage, ad.getRegistrationDate
        )
    }

}

case class IdResponse(id: Long) {}

object IdResponse {
    implicit val implicitWrites: Writes[IdResponse] = Json.writes[IdResponse]
    implicit val implicitReads: Reads[IdResponse] = Json.reads[IdResponse]
}

case class BadResponse(msg: String) {}

object BadResponse {
    implicit val implicitWrites: Writes[BadResponse] = Json.writes[BadResponse]
    implicit val implicitReads: Reads[BadResponse] = Json.reads[BadResponse]
}



