package ly.unnecessary.backend.rooms;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.rooms.RoomsOuterClass.RoomsResponse;

public class RoomsServiceImpl extends RoomsGrpc.RoomsImplBase implements RoomsService {
    RoomsCore core;

    public RoomsServiceImpl(RoomsCore core) {
        this.core = core;
    }

    @Override
    public void listRooms(Empty request, StreamObserver<RoomsResponse> responseObserver) {
        super.listRooms(request, responseObserver);
    }
}