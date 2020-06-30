package ly.unnecessary.backend;

import java.io.IOException;

import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.LoggerFactory;

import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import io.grpc.ServerBuilder;
import ly.unnecessary.backend.converters.UserConverter;
import ly.unnecessary.backend.converters.UserSignUpRequestConverter;
import ly.unnecessary.backend.core.UserCore;
import ly.unnecessary.backend.core.UserSignUpRequestCore;
import ly.unnecessary.backend.persisters.UserPersister;
import ly.unnecessary.backend.persisters.UserSignUpRequestPersister;
import ly.unnecessary.backend.services.UserService;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.UserEmailer;

public class Application {
    static String driver = "org.postgresql.Driver";

    public static void main(String[] args) throws InterruptedException, IOException {
        // Get logger
        var logger = LoggerFactory.getLogger(Application.class);

        // Parse flags
        var lportFlag = System.getenv("ULY_LPORT");
        var dbusrFlag = System.getenv("ULY_DBUSR");
        var dbpassFlag = System.getenv("ULY_DBPASS");
        var dbhostFlag = System.getenv("ULY_DBHOST");
        var dbportFlag = System.getenv("ULY_DBPORT");
        var dbnameFlag = System.getenv("ULY_DBNAME");
        var smtpHostFlag = System.getenv("ULY_SMTPHOST");
        var smtpPortFlag = System.getenv("ULY_SMTPPORT");
        var smtpUsrFlag = System.getenv("ULY_SMTPUSR");
        var smtpPassFlag = System.getenv("ULY_SMTPPASS");
        var smtpEmailFlag = System.getenv("ULY_SMTPEMAIL");

        // Use default values if flags are not set
        final var lport = (lportFlag == null) ? 1999 : Integer.valueOf(lportFlag);
        final var dbusr = (dbusrFlag == null) ? "example" : dbusrFlag;
        final var dbpass = (dbpassFlag == null) ? "example" : dbpassFlag;
        final var dbhost = (dbhostFlag == null) ? "localhost" : dbhostFlag;
        final var dbport = (dbportFlag == null) ? 5432 : Integer.valueOf(dbportFlag);
        final var dbname = (dbnameFlag == null) ? "example" : dbnameFlag;
        final var smtpHost = (smtpHostFlag == null) ? "mail.example.com" : smtpHostFlag;
        final var smtpPort = (smtpPortFlag == null) ? 587 : Integer.valueOf(smtpPortFlag);
        final var smtpUsr = (smtpUsrFlag == null) ? "system@example.com" : smtpUsrFlag;
        final var smtpPass = (smtpPassFlag == null) ? "example" : smtpPassFlag;
        final var smtpEmail = (smtpEmailFlag == null) ? "system@example.com" : smtpEmailFlag;
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

        // Create persisters
        var userSignUpRequestPersister = new UserSignUpRequestPersister(database);
        var userPersister = new UserPersister(database);

        // Create utilities
        var mailer = MailerBuilder.withSMTPServer(smtpHost, smtpPort, smtpUsr, smtpPass)
                .withTransportStrategy(TransportStrategy.SMTP_TLS).buildMailer();
        var templateEmail = EmailBuilder.startingBlank().from("unneccessary.ly", smtpEmail)
                .withSubject("unneccessary.ly Signup Confirmation").withPlainText("Your confirmation token is: ")
                .buildEmail();
        var userEmailer = new UserEmailer(mailer, templateEmail);
        var hasher = new Hasher();

        // Create core
        var userSignUpRequestCore = new UserSignUpRequestCore(userSignUpRequestPersister, userEmailer, hasher);
        var userCore = new UserCore(userPersister, userSignUpRequestCore, hasher);

        // Create converters
        var userSignUpRequestConverter = new UserSignUpRequestConverter();
        var userConverter = new UserConverter();

        // Create services
        var userService = new UserService(userCore, userConverter, userSignUpRequestConverter);

        // Serve services
        logger.info("Starting server on port {}", lport);
        ServerBuilder.forPort(lport).addService(userService).build().start().awaitTermination();
    }
}