package ly.unnecessary.backend.persisters;

import io.ebean.Database;

public abstract class BasePersister<T> {
    private Database database;

    public BasePersister(Database database) {
        this.database = database;
    }

    public T save(T entity) {
        this.database.save(entity);

        return entity;
    }

    public Database getDatabase() {
        return database;
    }
}