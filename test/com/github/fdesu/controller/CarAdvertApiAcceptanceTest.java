package com.github.fdesu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fdesu.WithDatabaseApplication;
import com.github.fdesu.controller.validation.IdResponse;
import com.github.fdesu.data.model.CarAdvert;
import org.junit.Before;
import org.junit.Test;
import play.db.DBApi;
import play.mvc.Result;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.*;

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
        assertDataRow(999L, mapper.readValue(contentAsString(result), CarAdvert.class));
    }

    @Test
    public void shouldReturnAllAdverts() throws IOException {
        populateDataRow(dbApi, 999L);
        Result result = route(app, fakeRequest(GET, "/v1/car/all"));

        assertThat(result.status()).isEqualTo(OK);
        assertDataRow(999L, mapper.readValue(contentAsString(result), CarAdvert[].class)[0]);
    }

    @Test
    public void shouldOmitIdAndAdd() {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldAddNewAdvert() throws IOException {
        Result result = route(app, fakeRequest(POST, "/v1/car/new").bodyText(NEW));

        assertThat(result.status()).isEqualTo(OK);
        assertThat(mapper.readValue(contentAsString(result), IdResponse.class).getId()).isNotNull();
    }

    @Test
    public void shouldUpdateAdvert() {
        Result result = route(app, fakeRequest(PUT, "/v1/car/change").bodyText(EXAMPLE));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void shouldDeleteAdvert() throws IOException {
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
