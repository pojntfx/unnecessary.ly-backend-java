package ly.unnecessary.backend.converters;

import ly.unnecessary.backend.api.RoomsOuterClass.Room;

public class RoomsConverterImpl implements RoomsConverter {
    @Override
    public Room toExternal(ly.unnecessary.backend.entities.Room room) {
        return Room.newBuilder().setId(room.getId()).setTitle(room.getTitle()).build();
    }

    @Override
    public ly.unnecessary.backend.entities.Room toInternal(Room room) {
        var internalRoom = new ly.unnecessary.backend.entities.Room();

        internalRoom.setId(room.getId());
        internalRoom.setTitle(room.getTitle());

        return internalRoom;
    }
}