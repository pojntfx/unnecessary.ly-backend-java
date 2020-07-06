package ly.unnecessary.backend.utilities;

import java.util.UUID;

/**
 * Random token generator
 */
public class TokenGenerator {
    /**
     * Generate a random token
     * 
     * @return String
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}