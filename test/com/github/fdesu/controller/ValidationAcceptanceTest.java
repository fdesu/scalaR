package com.github.fdesu.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fdesu.WithDatabaseApplication;
import com.github.fdesu.data.model.CarAdvert;
import com.github.fdesu.data.model.Fuel;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import play.mvc.Result;

import static com.github.fdesu.data.model.Fuel.GASOLINE;
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
    public void shouldFailAddExistent(String entity) {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(entity));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
        assertThat(contentAsString(result)).matches(".+errorMessage.+property is empty!.+");
    }

    public static List<String> validationTestData() {
        return Arrays.asList(
            payload(null, null, GASOLINE, 0, false, 0, new Date()),
            payload(null, "      ", GASOLINE, 0, false, 0, new Date()),
            payload(null, T, null, 0, false, 0, new Date()),
            payload(null, T, GASOLINE, null, false, 0, new Date()),
            payload(null, T, GASOLINE, 0, null, 0, new Date()),
            payload(null, T, GASOLINE, 0, false, null, new Date()),
            payload(null, T, GASOLINE, 0, false, 0, null)
        );
    }

    public static String payload(Long id, String title, Fuel fuel,
                                 Integer price, Boolean isNew, Integer mileage,
                                 Date registrationDate) {
        try {
            return new ObjectMapper().writeValueAsString(new CarAdvert(
                id, title, fuel, price, isNew, mileage, registrationDate
            ));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

}
