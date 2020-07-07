package ly.unnecessary.backend.utilities;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class TestHasher {
    private Hasher hasher;

    private static String DECRYPTED_MESSAGE = "supersecretmessage";
    private static String ENCRYPTED_MESSAGE = BCrypt.withDefaults().hashToString(12, DECRYPTED_MESSAGE.toCharArray());

    @BeforeEach
    void setUp() {
        this.hasher = new Hasher();
    }

    @Test
    void testHash() {
        var hashed = this.hasher.hash(DECRYPTED_MESSAGE);

        assertNotEquals(ENCRYPTED_MESSAGE, hashed); // We need a non-deterministic encryption algorithm
    }

    @Test
    void testHashVerify() {
        var hashed = this.hasher.hash(DECRYPTED_MESSAGE);

        assertTrue(BCrypt.verifyer().verify(DECRYPTED_MESSAGE.toCharArray(), hashed.toCharArray()).verified);
    }

    @Test
    void testVerify() {
        assertTrue(this.hasher.verify(ENCRYPTED_MESSAGE, DECRYPTED_MESSAGE));
    }
}