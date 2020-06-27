package ly.unnecessary.backend.cores;

import ly.unnecessary.backend.converters.RoomsConverter;
import ly.unnecessary.backend.entities.Room;
import ly.unnecessary.backend.persisters.RoomsPersister;

public class RoomsCoreImpl implements RoomsCore {
    RoomsPersister persister;
    RoomsConverter converter;

    public RoomsCoreImpl(RoomsPersister persister, RoomsConverter converter) {
        this.persister = persister;
        this.converter = converter;
    }

    @Override
    public ly.unnecessary.backend.api.RoomsOuterClass.Room createRoom(
            ly.unnecessary.backend.api.RoomsOuterClass.Room room) {
        var internalRoom = this.converter.toInternal(room);

        var createdRoom = this.persister.createRoom(internalRoom);

        return this.converter.toExternal(createdRoom);
    }
}