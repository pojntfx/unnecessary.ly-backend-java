package ly.unnecessary.backend.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTokenGenerator {
    private TokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        this.tokenGenerator = new TokenGenerator();
    }

    @Test
    void testGenerateTokenExists() {
        var token = this.tokenGenerator.generateToken();

        assertNotNull(token);
    }

    @Test
    void testGenerateTokenIsRandom() {
        var tokens = new ArrayList<String>();

        for (var i = 0; i < 1000; i++) {
            tokens.add(this.tokenGenerator.generateToken());
        }

        var uniqueTokens = new HashSet<String>(tokens);

        assertEquals(tokens.size(), uniqueTokens.size());
    }
}