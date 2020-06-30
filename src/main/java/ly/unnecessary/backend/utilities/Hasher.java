package ly.unnecessary.backend.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Hasher {
    public String hash(String plaintext) {
        return BCrypt.withDefaults().hashToString(12, plaintext.toCharArray());
    }

    public boolean verify(String hashed, String plaintext) {
        return BCrypt.verifyer().verify(plaintext.toCharArray(), hashed.toCharArray()).verified;
    }
}