package ly.unnecessary.backend.persisters;

import io.ebean.Database;

/**
 * Base persister
 */
public abstract class BasePersister<T> {
    private Database database;

    public BasePersister(Database database) {
        this.database = database;
    }

    /**
     * Save entity
     * 
     * @param entity
     * @return T
     */
    public T save(T entity) {
        this.database.save(entity);

        return entity;
    }

    /**
     * Get the database instance
     * 
     * @return Database
     */
    public Database getDatabase() {
        return database;
    }
}