package com.github.fdesu.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fdesu.WithDatabaseApplication;
import com.github.fdesu.data.model.CarAdvert;
import com.github.fdesu.data.model.Fuel;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import play.mvc.Result;
import scala.Option;

import static com.github.fdesu.data.model.Fuel.GASOLINE;
import static com.github.fdesu.data.model.Fuel.UNKNOWN;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.POST;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

@RunWith(JUnitParamsRunner.class)
public class ValidationAcceptanceTest extends WithDatabaseApplication {
    private static final String T = "DUMMY TITLE";

    @Test
    @Parameters(method = "validationTestData")
    public void shouldFailAddExistent(JsValue toSend) {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyJson(toSend));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

    public static List<JsValue> validationTestData() {
        return Arrays.asList(
            payload(null, GASOLINE, 0, false, 0, now()),
            payload("      ", GASOLINE, 0, false, 0, now()),
            payload(T, UNKNOWN, 0, false, 0, now()),
            payload(T, GASOLINE, -123, false, 0, now()),
            payload(T, GASOLINE, 0, false, -33, now())

            // not working as of serialization problems
            //payload(T, GASOLINE, 0, false, 0, null)
        );
    }

    public static JsValue payload(String title, Fuel fuel,
                                  int price, boolean isNew, int mileage,
                                  LocalDate registrationDate) {
        return Json.toJson(new CarAdvertResource(
                0L, title, fuel, price, isNew, mileage, registrationDate
        ), CarAdvertResource.implicitWrites());
    }

}
