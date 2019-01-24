package com.github.fdesu;

import play.Application;
import play.db.DBApi;
import play.db.Database;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import play.test.WithApplication;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class WithDatabaseApplication extends WithApplication {

    @Override
    protected Application provideApplication() {
        return fakeApplication(inMemoryDatabase());
    }

    public void populateDataRow(DBApi dbApi, long id) {
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