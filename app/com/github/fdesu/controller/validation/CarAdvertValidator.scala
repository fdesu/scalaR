package com.github.fdesu.controller.validation

import com.github.fdesu.data.model.CarAdvert
import javax.inject.Singleton

@Singleton
class CarAdvertValidator {

    def validate(advert: CarAdvert): Unit = {
        if (advert.getId != null) throw ValidationException("Id property should be omitted")

        if (advert.getTitle == null || advert.getTitle.trim.isEmpty) throw ValidationException("Title property is empty!")
        if (advert.getFuel == null) throw ValidationException("Fuel property is empty!")
        if (advert.getPrice == null) throw ValidationException("Price property is empty!")
        if (advert.isNew == null) throw ValidationException("IsNew property is empty!")

        if (!advert.isNew) {
            if (advert.getMileage == null) throw ValidationException("Mileage property is empty!")
            if (advert.getRegistrationDate == null) throw ValidationException("RegistrationDate property is empty!")
        }
    }

}
