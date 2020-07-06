package ly.unnecessary.backend.utilities;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

public class Emailer {
    private Mailer mailer;
    private Email templateEmail;
    private TokenGenerator tokenGenerator;

    public Emailer(Mailer mailer, Email templateEmail, TokenGenerator tokenGenerator) {
        this.mailer = mailer;
        this.templateEmail = templateEmail;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * Send a confirmation email
     * 
     * @param email
     * @return String
     */
    public String requestConfirmation(String email) {
        var token = this.tokenGenerator.generateToken();

        var emailToSend = EmailBuilder.copying(this.templateEmail).to("", email)
                .withPlainText(String.format("%s%s", this.templateEmail.getPlainText(), token)).buildEmail();

        this.mailer.sendMail(emailToSend);

        return token;
    }
}