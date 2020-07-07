package ly.unnecessary.backend.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

@ExtendWith(MockitoExtension.class)
public class TestEmailer {
    private Emailer emailer;

    @Mock
    Mailer mailer;
    @Mock
    Email templateEmail;
    @Mock
    TokenGenerator tokenGenerator;

    private static String TOKEN = "secrettoken";
    private static String MESSAGE = "example message";
    private static String RECIPIENT = "felicitas@pojtinger.com";

    @BeforeEach
    void beforeAll() {
        Mockito.doReturn(TOKEN).when(this.tokenGenerator).generateToken();
        Mockito.doReturn(MESSAGE).when(this.templateEmail).getPlainText();

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                var email = (Email) (invocation.getArguments()[0]);

                assertEquals(email.getRecipients().get(0).getAddress(), RECIPIENT);
                assertEquals(email.getPlainText(), MESSAGE + TOKEN);

                return null;
            }
        }).when(this.mailer).sendMail(Mockito.any());

        this.emailer = new Emailer(this.mailer, this.templateEmail, this.tokenGenerator);
    }

    @Test
    void testRequestConfirmation() {
        this.emailer.requestConfirmation(RECIPIENT);

        Mockito.verify(this.mailer).sendMail(Mockito.any());
    }
}