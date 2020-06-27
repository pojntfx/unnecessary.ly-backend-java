package ly.unnecessary.backend.persisters;

import io.ebean.Database;
import ly.unnecessary.backend.entities.Room;

public class RoomsPersisterImpl implements RoomsPersister {
    Database database;

    public RoomsPersisterImpl(Database database) {
        this.database = database;
    }

    @Override
    public Room createRoom(Room room) {
        this.database.beginTransaction();

        this.database.save(room);

        this.database.commitTransaction();

        return room;
    }
}