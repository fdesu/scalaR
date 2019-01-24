package com.github.fdesu.data.repo;

import java.util.concurrent.ExecutionException;

import com.github.fdesu.WithDatabaseApplication;
import com.github.fdesu.data.model.CarAdvert;
import org.junit.Before;
import org.junit.Test;
import play.db.DBApi;
import play.db.jpa.JPAApi;

import static org.assertj.core.api.Assertions.assertThat;

public class JPACarAdvertRepoIntegrationTest extends WithDatabaseApplication {

    private static final long EXISTENT_ID = 678L;
    private static final String EXAMPLE_TITLE = "updated one";

    private CarAdvertRepo repo;
    private JPAApi jpaApi;
    private DBApi dbApi;

    @Before
    public void setUp() {
        repo = instanceOf(CarAdvertRepo.class);
        jpaApi = instanceOf(JPAApi.class);
        dbApi = instanceOf(DBApi.class);
    }

    @Test
    public void shouldFindNothingById() {
        assertThat(repo.findById(321L)).isNull();
    }

    @Test
    public void shouldFindById() {
        populateDataRow(dbApi, EXISTENT_ID);

        assertThat(repo.findById(EXISTENT_ID)).isNotNull();
    }

    @Test
    public void shouldFindNothingAtAll() {
        assertThat(repo.all()).isEmpty();
    }

    @Test
    public void shouldFindSomethingDuringAllCall() throws ExecutionException, InterruptedException {
        populateDataRow(dbApi, 1L);

        assertThat(repo.all()).isNotEmpty();
    }

    @Test
    public void shouldPersistAsIs() {
        CarAdvert carAdvert = new CarAdvert();

        Long id = repo.persist(carAdvert);

        jpaApi.withTransaction(() -> {
            assertThat(jpaApi.em().find(CarAdvert.class, id)).isNotNull();
        });
    }

    @Test
    public void shouldUpdateEntity() {
        Long id = repo.persist(new CarAdvert());

        CarAdvert advert = repo.findById(id);
        advert.setTitle(EXAMPLE_TITLE);
        repo.update(advert);

        jpaApi.withTransaction(() -> assertThat(jpaApi.em().find(CarAdvert.class, id).getTitle()).isEqualTo(EXAMPLE_TITLE));
    }

    @Test
    public void shouldDeleteEntityById() {
        Long id = repo.persist(new CarAdvert());

        repo.delete(id);

        jpaApi.withTransaction(() -> {
            assertThat(jpaApi.em().find(CarAdvert.class, id)).isNull();
        });
    }

}
