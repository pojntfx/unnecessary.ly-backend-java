package ly.unnecessary.backend.persisters;

import io.ebean.Database;

public class RoomsPersisterImpl implements RoomsPersister {
    Database database;

    public RoomsPersisterImpl(Database database) {
        this.database = database;
    }
}