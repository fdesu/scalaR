package com.github.fdesu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fdesu.WithDatabaseApplication;
import com.github.fdesu.data.model.CarAdvert;
import org.junit.Before;
import org.junit.Test;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import play.db.DBApi;
import play.mvc.Result;

import java.io.IOException;

import static com.github.fdesu.data.model.Fuel.GASOLINE;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.MimeTypes.JSON;
import static play.test.Helpers.*;

public class CarAdvertApiAcceptanceTest extends WithDatabaseApplication {
    private static final JsValue EXAMPLE = Json.toJson(new CarAdvertResource(
            123L, "TEST_TITLE", GASOLINE,
            7654, true, 0, now()
    ), CarAdvertResource.implicitWrites());

    private DBApi dbApi;
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        dbApi = instanceOf(DBApi.class);
        mapper = instanceOf(ObjectMapper.class);
    }

    @Test
    public void shouldRetrieveErrorCodeWhenCarAdvertIsMissing() {
        Result result = route(app, fakeRequest(GET, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldRetrieveCarAdvertById() throws IOException {
        populateDataRow(dbApi, 999L);

        Result result = route(app, fakeRequest(GET, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.contentType()).hasValue(JSON);
        assertDataRow(999L, mapper.readValue(contentAsString(result), CarAdvert.class));
    }

    @Test
    public void shouldReturnAllAdverts() throws IOException {
        populateDataRow(dbApi, 999L);
        Result result = route(app, fakeRequest(GET, "/v1/car/all"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.contentType()).hasValue(JSON);
        assertDataRow(999L, mapper.readValue(contentAsString(result), CarAdvert[].class)[0]);
    }

    @Test
    public void shouldOmitIdAndAdd() {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyJson(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.contentType()).hasValue(JSON);
        assertThat(contentAsString(result)).matches(".*id.*\\d+.*");
    }

    @Test
    public void shouldUpdateAdvert() {
        Result result = route(app, fakeRequest(PUT, "/v1/car/change").bodyJson(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.contentType()).hasValue(JSON);
    }

    @Test
    public void shouldDeleteAdvert() {
        populateDataRow(dbApi, 999L);

        Result result = route(app, fakeRequest(DELETE, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(result.contentType()).hasValue(JSON);
    }

    @Test
    public void shouldFailToDeleteUnexistentAdvert() {
        Result result = route(app, fakeRequest(DELETE, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }

}
