package ly.unnecessary.backend.utilities;

import java.util.UUID;

public class UserEmailer {
    public String requestSignUpConfirmation(String email) {
        var token = UUID.randomUUID();

        System.out.printf("Sending confirmation mail to %s with token %s\n", email, token.toString());

        return token.toString();
    }
}