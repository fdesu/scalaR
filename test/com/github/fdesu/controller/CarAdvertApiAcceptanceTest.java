package com.github.fdesu.controller;

import com.github.fdesu.WithDatabaseApplication;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.DBApi;
import play.db.Database;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import play.mvc.Result;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CONFLICT;
import static play.test.Helpers.DELETE;
import static play.test.Helpers.GET;
import static play.test.Helpers.NOT_FOUND;
import static play.test.Helpers.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;

public class CarAdvertApiAcceptanceTest extends WithDatabaseApplication {
    private static final String EXAMPLE = "{\n" +
        "  \"id\": 123,\n" +
        "  \"title\": \"TEST_TITLE\",\n" +
        "  \"fuel\": \"GASOLINE\",\n" +
        "  \"price\": 7654,\n" +
        "  \"new\": true\n" +
        "}";

    private static final String NEW = "{\n" +
        "  \"title\": \"TEST_TITLE\",\n" +
        "  \"fuel\": \"GASOLINE\",\n" +
        "  \"price\": 7654,\n" +
        "  \"new\": true\n" +
        "}";

    private DBApi dbApi;

    @Before
    public void setUp() {
        dbApi = instanceOf(DBApi.class);
    }

    @Test
    public void shouldRetrieveErrorCodeWhenCarAdvertIsMissing() {
        Result result = route(app, fakeRequest(GET, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldRetrieveCarAdvertById() {
        populateDataRow(dbApi, 999L);

        Result result = route(app, fakeRequest(GET, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldReturnAllAdverts() {
        Result result = route(app, fakeRequest(GET, "/v1/car/all"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldOmitIdAndAdd() {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldAddNewAdvert() {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(NEW));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldUpdateAdvert() {
        Result result = route(app, fakeRequest(PUT, "/v1/car/change").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldDeleteAdvert() {
        populateDataRow(dbApi, 999L);

        Result result = route(app, fakeRequest(DELETE, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldFailToDeleteUnexistentAdvert() {
        Result result = route(app, fakeRequest(DELETE, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }

}
