package ly.unnecessary.backend;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import io.grpc.ServerBuilder;
import ly.unnecessary.backend.converters.RoomsConverterImpl;
import ly.unnecessary.backend.cores.RoomsCoreImpl;
import ly.unnecessary.backend.persisters.RoomsPersisterImpl;
import ly.unnecessary.backend.services.RoomsServiceImpl;

public class Application {
    static String driver = "org.postgresql.Driver";

    public static void main(String[] args) throws InterruptedException, IOException {
        // Get logger
        var logger = LoggerFactory.getLogger(Application.class);

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
        final var dbConnectionLine = String.format("jdbc:postgresql://%s:%d/%s", dbhost, dbport, dbname);

        // Migrate the database
        logger.info("Migrating database {}", dbConnectionLine);
        var migrationConfig = new MigrationConfig();
        migrationConfig.setDbUsername(dbusr);
        migrationConfig.setDbPassword(dbpass);
        migrationConfig.setDbUrl(dbConnectionLine);
        migrationConfig.setDbDriver(driver);

        var migrationRunner = new MigrationRunner(migrationConfig);
        migrationRunner.run();

        // Connect to database
        logger.info("Connecting to database {}", dbConnectionLine);
        var dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(dbusr);
        dataSourceConfig.setPassword(dbpass);
        dataSourceConfig.setUrl(dbConnectionLine);
        dataSourceConfig.setDriver(driver);

        var databaseBaseConfig = new DatabaseConfig();
        databaseBaseConfig.setDataSourceConfig(dataSourceConfig); // Deprecated but still in use; see
                                                                  // https://ebean.io/docs/intro/configuration/#programmatic
        var database = DatabaseFactory.create(databaseBaseConfig);

        // Create services
        var roomsPersister = new RoomsPersisterImpl(database);
        var roomsConverter = new RoomsConverterImpl();
        var roomsCore = new RoomsCoreImpl(roomsPersister, roomsConverter);
        var roomsService = new RoomsServiceImpl(roomsCore);

        // Serve services
        logger.info("Starting server on port {}", lport);
        ServerBuilder.forPort(lport).addService(roomsService).build().start().awaitTermination();
    }
}