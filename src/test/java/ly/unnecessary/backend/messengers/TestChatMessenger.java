package ly.unnecessary.backend.messengers;

import com.google.protobuf.InvalidProtocolBufferException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.nats.client.Connection;
import ly.unnecessary.backend.converters.ChatConverter;
import ly.unnecessary.backend.entities.Chat;

@ExtendWith(MockitoExtension.class)
public class TestChatMessenger {
    private ChatMessenger chatMessenger;

    @Mock
    Connection bus;
    @Mock
    ChatConverter converter;

    @BeforeEach
    void setUp() throws InvalidProtocolBufferException {
        Mockito.doReturn("test".getBytes()).when(this.converter).toByteArray(Mockito.any());

        this.chatMessenger = new ChatMessenger(this.bus, this.converter);
    }

    @Test
    void testPublishChat() {
        this.chatMessenger.publishChat(new Chat());

        Mockito.verify(this.bus).publish(Mockito.any(), Mockito.any());
    }
}