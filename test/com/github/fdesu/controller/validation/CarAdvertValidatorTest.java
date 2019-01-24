package com.github.fdesu.controller.validation;

import com.github.fdesu.data.model.CarAdvert;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.fdesu.data.model.Fuel.GASOLINE;
import static com.github.fdesu.data.model.Fuel.UNKNOWN;
import static java.time.LocalDate.now;

@RunWith(JUnitParamsRunner.class)
public class CarAdvertValidatorTest {
    private static final String T = "DUMMY TITLE";

    private CarAdvertValidator validator = new CarAdvertValidator();

    @Test(expected = ValidationException.class)
    @Parameters(method = "validationTestData")
    public void testValidation(CarAdvert advert) {
        validator.validate(advert);
    }

    public static CarAdvert[] validationTestData() {
        return new CarAdvert[] {
            new CarAdvert(0L, null, GASOLINE, 0, false, 0, now()),
            new CarAdvert(0L, "      ", GASOLINE, 0, false, 0, now()),
            new CarAdvert(0L, T, null, 0, false, 0, now()),
            new CarAdvert(0L, T, UNKNOWN, 0, false, 0, now()),
            new CarAdvert(0L, T, GASOLINE, 0, false, 0, null)
        };
    }

}
