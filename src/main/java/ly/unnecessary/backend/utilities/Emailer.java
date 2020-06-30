package ly.unnecessary.backend.utilities;

import java.util.UUID;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

public class Emailer {
    private Mailer mailer;
    private Email templateEmail;

    public Emailer(Mailer mailer, Email templateEmail) {
        this.mailer = mailer;
        this.templateEmail = templateEmail;
    }

    public String requestConfirmation(String email) {
        var token = UUID.randomUUID();

        var emailToSend = EmailBuilder.copying(this.templateEmail).to("", email)
                .withPlainText(String.format("%s%s", this.templateEmail.getPlainText(), token.toString())).buildEmail();

        this.mailer.sendMail(emailToSend);

        return token.toString();
    }
}