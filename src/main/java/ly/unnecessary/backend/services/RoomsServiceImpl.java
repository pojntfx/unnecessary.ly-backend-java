package ly.unnecessary.backend.services;

import ly.unnecessary.backend.cores.RoomsCore;

import com.google.protobuf.Empty;

import io.grpc.stub.StreamObserver;
import ly.unnecessary.backend.api.RoomsGrpc;
import ly.unnecessary.backend.api.RoomsOuterClass.RoomsResponse;

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