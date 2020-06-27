package ly.unnecessary.backend.services;

import ly.unnecessary.backend.cores.RoomsCore;
import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.RoomsGrpc;
import ly.unnecessary.backend.api.RoomsOuterClass.NewRoom;
import ly.unnecessary.backend.api.RoomsOuterClass.Room;

public class RoomsServiceImpl extends RoomsGrpc.RoomsImplBase implements RoomsService {
    RoomsCore core;

    public RoomsServiceImpl(RoomsCore core) {
        this.core = core;
    }

    @Override
    public void createRoom(NewRoom request, StreamObserver<Room> responseObserver) {
        var newRoom = this.core.createRoom(Room.newBuilder().setTitle(request.getTitle()).build());

        responseObserver.onNext(newRoom);

        responseObserver.onCompleted();
    }
}