package com.github.fdesu.data.validation

import com.github.fdesu.data.model.CarAdvert
import javax.inject.Singleton

@Singleton
class CarAdvertValidator {

    def validate(advert: CarAdvert): Unit = {
        if (advert.getId == null) throw ValidationException("Id is empty!")
        validateWithoutId(advert)
    }

    def validateWithoutId(advert: CarAdvert): Unit = {
        if (advert.getTitle == null || advert.getTitle.trim.isEmpty) throw ValidationException("Title is empty!")
        if (advert.getFuel == null) throw ValidationException("Fuel is empty!")
        if (advert.getPrice == null) throw ValidationException("Price is empty!")
        if (advert.isNew == null) throw ValidationException("IsNew is empty!")

        if (!advert.isNew) {
            if (advert.getMileage == null) throw ValidationException("Mileage is empty!")
            if (advert.getRegistrationDate == null) throw ValidationException("RegistrationDate is empty!")
        }
    }

}
