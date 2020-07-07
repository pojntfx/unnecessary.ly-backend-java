package ly.unnecessary.backend.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ly.unnecessary.backend.entities.UserSignUpRequest;
import ly.unnecessary.backend.persisters.UserSignUpRequestPersister;
import ly.unnecessary.backend.utilities.Emailer;
import ly.unnecessary.backend.utilities.Hasher;

@ExtendWith(MockitoExtension.class)
public class TestUserSignUpRequestCore {
    private UserSignUpRequestCore userSignUpRequestCore;

    @Mock
    UserSignUpRequestPersister persister;
    @Mock
    Emailer emailer;
    @Mock
    Hasher hasher;

    private UserSignUpRequest userSignInRequest;

    static private String TOKEN = "secrettoken";
    static private String EMAIL = "hello@example.com";

    @BeforeEach
    void setUp() {
        this.userSignUpRequestCore = new UserSignUpRequestCore(this.persister, this.emailer, this.hasher);

        this.userSignInRequest = new UserSignUpRequest();
        this.userSignInRequest.setToken(TOKEN);
    }

    @Test
    void testCreateRequest() {
        this.userSignUpRequestCore.createRequest(this.userSignInRequest, EMAIL);

        Mockito.verify(this.emailer).requestConfirmation(Mockito.any());
        Mockito.verify(this.persister).save(Mockito.any());
    }

    @Test
    void testValidateSignUpRequest() {
        Mockito.doReturn(true).when(this.hasher).verify(Mockito.any(), Mockito.matches(TOKEN));

        assertFalse(this.userSignInRequest.isConfirmed());

        var res = this.userSignUpRequestCore.validateSignUpRequest(this.userSignInRequest, TOKEN);

        Mockito.verify(this.hasher).verify(Mockito.any(), Mockito.matches(TOKEN));
        Mockito.verify(this.persister).save(Mockito.any());

        assertTrue(res.isConfirmed());
    }
}