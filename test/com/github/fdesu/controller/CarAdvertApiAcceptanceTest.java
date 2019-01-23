package com.github.fdesu.controller;

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
import static play.test.Helpers.*;

public class CarAdvertApiAcceptanceTest extends WithApplication {
    private static final String DB_NAME = "test-db";
    private static final String EXAMPLE = "{\n" +
            "  \"id\": 123,\n" +
            "  \"title\": \"TEST_TITLE\",\n" +
            "  \"fuel\": \"GASOLINE\",\n" +
            "  \"price\": 7654,\n" +
            "  \"isNew\": true\n" +
            "}";

    private DBApi dbApi;

    @Before
    public void setUp() {
        DBApi dbApi = app.injector().instanceOf(DBApi.class);
        this.dbApi = dbApi;

        Database database = dbApi.getDatabase(DB_NAME);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(
                        1,
                        "insert into CAR_ADVERT(ID, TITLE, FUEL, PRICE, IS_NEW) VALUES(999, 'A', 'GASOLINE', 123, 1);",
                        "delete from CAR_ADVERT where ID = 999;"))
        );
    }

    @Test
    public void shouldRetrieveErrorCodeWhenCarAdvertIsMissing() {
        Result result = route(app, fakeRequest(GET, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldRetrieveCarAdvertById() {
        Result result = route(app, fakeRequest(GET, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldReturnAllAdverts() {
        Result result = route(app, fakeRequest(GET, "/v1/car/all"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldAddNewAdvert() {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldUpdateAdvert() {
        Result result = route(app, fakeRequest(PUT, "/v1/car/change").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldDeleteAdvert() {
        Result result = route(app, fakeRequest(DELETE, "/v1/car/999"));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldFailToDeleteUnexistentAdvert() {
        Result result = route(app, fakeRequest(DELETE, "/v1/car/123"));

        assertThat(result.status()).isEqualTo(NOT_FOUND);
    }


    @Override
    protected Application provideApplication() {
        return fakeApplication(inMemoryDatabase(DB_NAME));
    }
}
