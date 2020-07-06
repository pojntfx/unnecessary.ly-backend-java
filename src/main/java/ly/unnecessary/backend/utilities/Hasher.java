package ly.unnecessary.backend.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Hasher {
    /**
     * Hash a plaintext string
     * 
     * @param plaintext
     * @return String
     */
    public String hash(String plaintext) {
        return BCrypt.withDefaults().hashToString(12, plaintext.toCharArray());
    }

    /**
     * Compare a hashed string with a plaintext string
     * 
     * @param hashed
     * @param plaintext
     * @return boolean
     */
    public boolean verify(String hashed, String plaintext) {
        return BCrypt.verifyer().verify(plaintext.toCharArray(), hashed.toCharArray()).verified;
    }
}