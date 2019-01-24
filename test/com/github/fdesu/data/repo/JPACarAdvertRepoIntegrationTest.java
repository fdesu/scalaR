package com.github.fdesu.data.repo;

import com.github.fdesu.data.model.CarAdvert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.DBApi;
import play.db.Database;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import play.db.jpa.JPAApi;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class JPACarAdvertRepoIntegrationTest extends WithApplication {
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
        repo.findById(321L).thenAccept(advert -> assertThat(advert).isNull());
    }

    @Test
    public void shouldFindById() {
        populateDataRow(EXISTENT_ID);

        repo.findById(EXISTENT_ID).thenAccept(advert -> assertThat(advert).isNotNull());
    }

    @Test
    public void shouldFindNothingAtAll() {
        repo.all().thenAccept(adverts -> assertThat(adverts).isEmpty());
    }

    @Test
    public void shouldFindSomethingDuringAllCall() {
        populateDataRow(1L);

        repo.all().thenAccept(adverts -> assertThat(adverts).isNotEmpty());
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

        repo.findById(id).thenApply(advert -> {
            advert.setTitle(EXAMPLE_TITLE);
            return advert;
        }).thenAccept(advert -> repo.update(advert));

        jpaApi.withTransaction(() -> {
            assertThat(jpaApi.em().find(CarAdvert.class, id).getTitle()).isEqualTo(EXAMPLE_TITLE);
        });
    }

    @Test
    public void shouldDeleteEntityById() {
        Long id = repo.persist(new CarAdvert());

        repo.delete(id);

        jpaApi.withTransaction(() -> {
            assertThat(jpaApi.em().find(CarAdvert.class, id)).isNull();
        });
    }

    @Override
    protected Application provideApplication() {
        return fakeApplication(inMemoryDatabase());
    }

    private void populateDataRow(long id) {
        Database database = dbApi.getDatabases().get(0);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
            new Evolution(
                1,
                "insert into CAR_ADVERT(ID, TITLE, FUEL, PRICE, ISNEW) VALUES(" + id + ", 'A', 'GASOLINE', 123, 1);",
                "delete from CAR_ADVERT where ID = " + id + ";"
            ))
        );
    }

}
