package com.github.fdesu.data.validation;

import java.util.Date;

import com.github.fdesu.data.model.CarAdvert;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.github.fdesu.data.model.Fuel.GASOLINE;

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
            new CarAdvert(null, T, GASOLINE, 0, false, 0, new Date()),
            new CarAdvert(0L, null, GASOLINE, 0, false, 0, new Date()),
            new CarAdvert(0L, "      ", GASOLINE, 0, false, 0, new Date()),
            new CarAdvert(0L, T, null, 0, false, 0, new Date()),
            new CarAdvert(0L, T, GASOLINE, null, false, 0, new Date()),
            new CarAdvert(0L, T, GASOLINE, 0, null, 0, new Date()),
            new CarAdvert(0L, T, GASOLINE, 0, false, null, new Date()),
            new CarAdvert(0L, T, GASOLINE, 0, false, 0, null)
        };
    }

}
