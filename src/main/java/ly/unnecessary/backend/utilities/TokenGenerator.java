package ly.unnecessary.backend.utilities;

import java.util.UUID;

public class TokenGenerator {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}