package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.RoomsOuterClass.Room;

public interface RoomsConverter {
    Room toExternal(ly.unnecessary.backend.entities.Room room);

    ly.unnecessary.backend.entities.Room toInternal(Room room);
}