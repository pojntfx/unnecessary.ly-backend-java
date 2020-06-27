package ly.unnecessary.backend;

import java.io.IOException;

import io.grpc.ServerBuilder;
import ly.unnecessary.backend.rooms.RoomsCoreImpl;
import ly.unnecessary.backend.rooms.RoomsServiceImpl;

public class Application {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Parse flags
        var lportFlag = System.getenv("LPORT");
        var dbusrFlag = System.getenv("DBUSR");
        var dbpassFlag = System.getenv("DBPASS");
        var dbhostFlag = System.getenv("DBHOST");
        var dbportFlag = System.getenv("DBPORT");
        var dbnameFlag = System.getenv("DBNAME");

        // Use default values if flags are not set
        final var lport = (lportFlag == null) ? 1999 : Integer.valueOf(lportFlag);
        final var dbusr = (dbusrFlag == null) ? "pojntfx" : dbusrFlag;
        final var dbpass = (dbpassFlag == null) ? "pojntfx" : dbpassFlag;
        final var dbhost = (dbhostFlag == null) ? "localhost" : dbhostFlag;
        final var dbport = (dbportFlag == null) ? 5432 : Integer.valueOf(dbportFlag);
        final var dbname = (dbnameFlag == null) ? "pojntfx" : dbnameFlag;

        System.out.printf("Starting server on port %d\n", lport);

        // Create services
        var roomsCore = new RoomsCoreImpl();
        var roomsService = new RoomsServiceImpl(roomsCore);

        // Serve services
        ServerBuilder.forPort(lport).addService(roomsService).build().start().awaitTermination();
    }
}