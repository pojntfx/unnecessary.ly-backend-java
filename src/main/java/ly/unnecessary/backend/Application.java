package ly.unnecessary.backend;

import java.io.IOException;

import io.grpc.ServerBuilder;
import ly.unnecessary.backend.rooms.RoomsCoreImpl;
import ly.unnecessary.backend.rooms.RoomsServiceImpl;

public class Application {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Parse port flag
        var portFlag = System.getenv("PORT");

        final int port;
        if (portFlag == null) {
            port = 1999;
        } else {
            port = Integer.valueOf(portFlag);
        }

        System.out.printf("Starting server on port %d\n", port);

        // Create services
        var roomsCore = new RoomsCoreImpl();
        var roomsService = new RoomsServiceImpl(roomsCore);

        // Serve services
        ServerBuilder.forPort(port).addService(roomsService).build().start().awaitTermination();
    }
}