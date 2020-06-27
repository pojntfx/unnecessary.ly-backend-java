package ly.unnecessary.backend.cores;

import ly.unnecessary.backend.persisters.RoomsPersister;

public class RoomsCoreImpl implements RoomsCore {
    RoomsPersister persister;

    public RoomsCoreImpl(RoomsPersister persister) {
        this.persister = persister;
    }
}