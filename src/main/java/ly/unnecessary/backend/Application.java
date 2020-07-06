package ly.unnecessary.backend;

import java.io.IOException;

import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.nats.client.Nats;
import io.nats.client.Options;
import ly.unnecessary.backend.converters.ChannelConverter;
import ly.unnecessary.backend.converters.ChatConverter;
import ly.unnecessary.backend.converters.CommunityConverter;
import ly.unnecessary.backend.converters.InvitationConverter;
import ly.unnecessary.backend.converters.UserConverter;
import ly.unnecessary.backend.converters.UserPasswordResetRequestConverter;
import ly.unnecessary.backend.converters.UserSignUpRequestConverter;
import ly.unnecessary.backend.core.ChannelCore;
import ly.unnecessary.backend.core.ChatCore;
import ly.unnecessary.backend.core.CommunityCore;
import ly.unnecessary.backend.core.InvitationCore;
import ly.unnecessary.backend.core.UserCore;
import ly.unnecessary.backend.core.UserPasswordResetRequestCore;
import ly.unnecessary.backend.core.UserSignUpRequestCore;
import ly.unnecessary.backend.interceptors.UserInterceptor;
import ly.unnecessary.backend.messengers.ChatMessenger;
import ly.unnecessary.backend.persisters.ChannelPersister;
import ly.unnecessary.backend.persisters.ChatPersister;
import ly.unnecessary.backend.persisters.CommunityPersister;
import ly.unnecessary.backend.persisters.InvitationPersister;
import ly.unnecessary.backend.persisters.UserPasswordResetRequestPersister;
import ly.unnecessary.backend.persisters.UserPersister;
import ly.unnecessary.backend.persisters.UserSignUpRequestPersister;
import ly.unnecessary.backend.services.CommunityService;
import ly.unnecessary.backend.services.UserService;
import ly.unnecessary.backend.utilities.Hasher;
import ly.unnecessary.backend.utilities.TokenGenerator;
import ly.unnecessary.backend.utilities.Emailer;

public class Application {
        /**
         * Database driver FQDN
         */
        private static String DRIVER = "org.postgresql.Driver";

        /**
         * Env variable config prefix
         */
        private static String CONFIG_PREFIX = "ULY_";

        private static Logger logger = LoggerFactory.getLogger(Application.class);

        /**
         * Start the app
         * 
         * @param args
         * @throws InterruptedException
         * @throws IOException
         */
        public static void main(String[] args) throws InterruptedException, IOException {
                // Parse flags
                var lportFlag = System.getenv(CONFIG_PREFIX + "LPORT");
                var dbusrFlag = System.getenv(CONFIG_PREFIX + "DBUSR");
                var dbpassFlag = System.getenv(CONFIG_PREFIX + "DBPASS");
                var dbhostFlag = System.getenv(CONFIG_PREFIX + "DBHOST");
                var dbportFlag = System.getenv(CONFIG_PREFIX + "DBPORT");
                var dbnameFlag = System.getenv(CONFIG_PREFIX + "DBNAME");
                var smtpHostFlag = System.getenv(CONFIG_PREFIX + "SMTPHOST");
                var smtpPortFlag = System.getenv(CONFIG_PREFIX + "SMTPPORT");
                var smtpUsrFlag = System.getenv(CONFIG_PREFIX + "SMTPUSR");
                var smtpPassFlag = System.getenv(CONFIG_PREFIX + "SMTPPASS");
                var smtpEmailFlag = System.getenv(CONFIG_PREFIX + "SMTPEMAIL");
                var busHostFlag = System.getenv(CONFIG_PREFIX + "BUSHOST");
                var busPortFlag = System.getenv(CONFIG_PREFIX + "BUSPORT");
                var busUsrFlag = System.getenv(CONFIG_PREFIX + "BUSUSR");
                var busPassFlag = System.getenv(CONFIG_PREFIX + "BUSPASS");

                // Use default values if flags are not set
                var lport = (lportFlag == null) ? 1999 : Integer.valueOf(lportFlag);
                var dbusr = (dbusrFlag == null) ? "example" : dbusrFlag;
                var dbpass = (dbpassFlag == null) ? "example" : dbpassFlag;
                var dbhost = (dbhostFlag == null) ? "localhost" : dbhostFlag;
                var dbport = (dbportFlag == null) ? 5432 : Integer.valueOf(dbportFlag);
                var dbname = (dbnameFlag == null) ? "example" : dbnameFlag;
                var smtpHost = (smtpHostFlag == null) ? "mail.example.com" : smtpHostFlag;
                var smtpPort = (smtpPortFlag == null) ? 587 : Integer.valueOf(smtpPortFlag);
                var smtpUsr = (smtpUsrFlag == null) ? "system@example.com" : smtpUsrFlag;
                var smtpPass = (smtpPassFlag == null) ? "example" : smtpPassFlag;
                var smtpEmail = (smtpEmailFlag == null) ? "system@example.com" : smtpEmailFlag;
                var busHost = (busHostFlag == null) ? "localhost" : busHostFlag;
                var busPort = (busPortFlag == null) ? 4222 : Integer.valueOf(busPortFlag);
                var busUsr = (busUsrFlag == null) ? "example" : busUsrFlag;
                var busPass = (busPassFlag == null) ? "example" : busPassFlag;
                var dbConnectionLine = String.format("jdbc:postgresql://%s:%d/%s", dbhost, dbport, dbname);
                var busConnectionLine = String.format("nats://%s:%d", busHost, busPort);

                // Migrate the database
                logger.info("Migrating database {}", dbConnectionLine);
                var migrationConfig = new MigrationConfig();
                migrationConfig.setDbUsername(dbusr);
                migrationConfig.setDbPassword(dbpass);
                migrationConfig.setDbUrl(dbConnectionLine);
                migrationConfig.setDbDriver(DRIVER);

                var migrationRunner = new MigrationRunner(migrationConfig);
                migrationRunner.run();

                // Connect to database
                logger.info("Connecting to database {}", dbConnectionLine);
                var dataSourceConfig = new DataSourceConfig();
                dataSourceConfig.setUsername(dbusr);
                dataSourceConfig.setPassword(dbpass);
                dataSourceConfig.setUrl(dbConnectionLine);
                dataSourceConfig.setDriver(DRIVER);

                var databaseBaseConfig = new DatabaseConfig();
                databaseBaseConfig.setDataSourceConfig(dataSourceConfig); // Deprecated but still in use; see
                                                                          // https://ebean.io/docs/intro/configuration/#programmatic
                var database = DatabaseFactory.create(databaseBaseConfig);

                // Connect to bus
                logger.info("Connecting to bus {}", busConnectionLine);
                var busConfig = new Options.Builder().server(busConnectionLine)
                                .userInfo(busUsr.toCharArray(), busPass.toCharArray()).build();
                var bus = Nats.connect(busConfig);

                // Create utilities
                var mailer = MailerBuilder.withSMTPServer(smtpHost, smtpPort, smtpUsr, smtpPass)
                                .withTransportStrategy(TransportStrategy.SMTP_TLS).buildMailer();
                var templateEmail = EmailBuilder.startingBlank().from("unneccessary.ly", smtpEmail)
                                .withSubject("unneccessary.ly Confirmation Token")
                                .withPlainText("Your confirmation token is: ").buildEmail();
                var tokenGenerator = new TokenGenerator();
                var emailer = new Emailer(mailer, templateEmail, tokenGenerator);
                var hasher = new Hasher();
                var userInterceptor = new UserInterceptor();

                // Create persisters
                var userSignUpRequestPersister = new UserSignUpRequestPersister(database);
                var userPasswordResetRequestPersister = new UserPasswordResetRequestPersister(database);
                var userPersister = new UserPersister(database);
                var invitationPersister = new InvitationPersister(database);
                var channelPersister = new ChannelPersister(database);
                var chatPersister = new ChatPersister(database);
                var communityPersister = new CommunityPersister(database);

                // Create converters
                var userSignUpRequestConverter = new UserSignUpRequestConverter();
                var userPasswordResetRequestConverter = new UserPasswordResetRequestConverter();
                var userConverter = new UserConverter();
                var invitationConverter = new InvitationConverter();
                var chatConverter = new ChatConverter();
                var channelConverter = new ChannelConverter(chatConverter);
                var communityConverter = new CommunityConverter(userConverter, channelConverter);

                // Create messengers
                var chatMessenger = new ChatMessenger(bus, chatConverter);

                // Create core
                var userSignUpRequestCore = new UserSignUpRequestCore(userSignUpRequestPersister, emailer, hasher);
                var userPasswordResetRequestCore = new UserPasswordResetRequestCore(userPasswordResetRequestPersister,
                                emailer, hasher);
                var userCore = new UserCore(userPersister, userSignUpRequestCore, userPasswordResetRequestCore, hasher);
                var invitationCore = new InvitationCore(invitationPersister, hasher, tokenGenerator);
                var channelCore = new ChannelCore(channelPersister);
                var chatCore = new ChatCore(chatPersister, chatMessenger);
                var communityCore = new CommunityCore(communityPersister, userCore, invitationCore, channelCore,
                                chatCore);

                // Create services
                var userService = new UserService(userCore, userConverter, userSignUpRequestConverter,
                                userPasswordResetRequestConverter);
                var communityService = new CommunityService(communityCore, communityConverter, userConverter,
                                invitationConverter, communityConverter, channelConverter, chatConverter);

                // Serve services
                logger.info("Starting server on port {}", lport);
                ServerBuilder.forPort(lport).addService(userService)
                                .addService(ServerInterceptors.intercept(communityService, userInterceptor)).build()
                                .start().awaitTermination();
        }
}