package com.github.fdesu.controller.validation

import com.github.fdesu.data.model.{CarAdvert, Fuel}
import javax.inject.Singleton

@Singleton
class CarAdvertValidator {

    def validate(advert: CarAdvert): Unit = {
        if (advert.getTitle == null || advert.getTitle.trim.isEmpty) throw ValidationException("Title property is empty!")
        if (advert.getFuel == null || advert.getFuel == Fuel.UNKNOWN) throw ValidationException("Fuel property is empty!")
        if (advert.getPrice < 0) throw ValidationException("Prive should be positive")

        if (!advert.isNew) {
            if (advert.getMileage < 0) throw ValidationException("Mileage should be positive")
            if (advert.getRegistrationDate == null) throw ValidationException("RegistrationDate property is empty!")
        }
    }

}
