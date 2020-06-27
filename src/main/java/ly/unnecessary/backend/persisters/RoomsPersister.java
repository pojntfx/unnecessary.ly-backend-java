package ly.unnecessary.backend.persisters;

import ly.unnecessary.backend.entities.Room;

public interface RoomsPersister {
    Room createRoom(Room room);
}