package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.RoomsOuterClass.Room;

public class RoomsConverterImpl implements RoomsConverter {
    @Override
    public Room toExternal(ly.unnecessary.backend.entities.Room room) {
        return Room.newBuilder().setId(room.id).setTitle(room.title).build();
    }

    @Override
    public ly.unnecessary.backend.entities.Room toInternal(Room room) {
        var internalRoom = new ly.unnecessary.backend.entities.Room();

        // TODO: Set properties

        return internalRoom;
    }
}