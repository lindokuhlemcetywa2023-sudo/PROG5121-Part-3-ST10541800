import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testGenerateMessageID() {
        Message message = new Message(1, "+27812345678", "Hello there");
        assertTrue(message.getMessageID().matches("[0-9]{10}"));
    }

    @Test
    public void testCheckRecipientCellValid() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertTrue(message.checkRecipientCell("+27812345678"));
    }

    @Test
    public void testCheckRecipientCellInvalid() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertFalse(message.checkRecipientCell("0812345678"));
    }

    @Test
    public void testCheckRecipientCellNull() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertFalse(message.checkRecipientCell(null));
    }

    @Test
    public void testCheckMessageLengthValid() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertTrue(message.checkMessageLength("This is a valid message."));
    }

    @Test
    public void testCheckMessageLengthAtLimit() {
        Message message = new Message(1, "+27812345678", "Hello");
        String text = "A".repeat(250);
        assertTrue(message.checkMessageLength(text));
    }

    @Test
    public void testCheckMessageLengthTooLong() {
        Message message = new Message(1, "+27812345678", "Hello");
        String text = "A".repeat(251);
        assertFalse(message.checkMessageLength(text));
    }

    @Test
    public void testCheckMessageLengthNull() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertFalse(message.checkMessageLength(null));
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message(1, "+27812345678", "Hello world");
        String hash = message.createMessageHash();

        assertTrue(hash.matches("[0-9]{2}:1:Helloworld"));
    }
    
    @Test
    public void testMessageHashUsesFirstAndLastWords() {
        Message message = new Message(2, "+27812345678", "QuickChat is working correctly");
        String hash = message.createMessageHash();
        
        assertTrue(hash.endsWith(":QuickChatcorrectly"));
    }
    
    @Test
    public void testGetMessageID() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertNotNull(message.getMessageID());
    }

    @Test
    public void testGetMessageHash() {
        Message message = new Message(1, "+27812345678", "Hello there");
        assertNotNull(message.getMessageHash());
    }

    @Test
    public void testGetRecipient() {
        Message message = new Message(1, "+27812345678", "Hello");
        assertEquals("+27812345678", message.getRecipient());
    }

    @Test
    public void testGetMessageText() {
        Message message = new Message(1, "+27812345678", "Hello there");
        assertEquals("Hello there", message.getMessageText());
    }

    @Test
    public void testGetMessageNo() {
        Message message = new Message(5, "+27812345678", "Hello");
        assertEquals(5, message.getMessageNo());
    }

    @Test
    public void testLoadStoredMessagesFromMissingFile() {
        assertTrue(Message.loadStoredMessagesFromJson("missing_messages_test.json").isEmpty());
    }
}
