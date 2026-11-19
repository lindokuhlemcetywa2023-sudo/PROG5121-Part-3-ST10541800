import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testSentMessagesArrayPopulated() {
        String[] sentMessages = new String[10];
        int count = 0;

        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        sentMessages[count++] = msg1.getMessageText();

        Message msg4 = new Message(4, "0838884567", "It is dinner time!");
        sentMessages[count++] = msg4.getMessageText();

        assertEquals(2, count);
        assertEquals("Did you get the cake?", sentMessages[0]);
        assertEquals("It is dinner time!", sentMessages[1]);
    }

    @Test
    public void testDisplayLongestMessage() {
        String[] messages = {
            "Did you get the cake?",
            "Where are you? You are late! I have asked you to be on time.",
            "Yohoooo, I am at your gate.",
            "It is dinner time!"
        };

        String longest = Message.findLongestMessage(messages, messages.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByMessageID() {
        Message msg4 = new Message(4, "0838884567", "It is dinner time!");
        String[] messageIDs = {msg4.getMessageID()};
        String[] recipients = {msg4.getRecipient()};
        String[] messages = {msg4.getMessageText()};

        String result = Message.searchByMessageID(messageIDs, recipients, messages, 1, msg4.getMessageID());
        assertTrue(result.contains("It is dinner time!"));
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!", result);
    }

    @Test
    public void testSearchByRecipient() {
        String[] recipients = {
            "+27834557896",
            "+27838884567",
            "+27834484567",
            "0838884567",
            "+27838884567"
        };
        String[] messages = {
            "Did you get the cake?",
            "Where are you? You are late! I have asked you to be on time.",
            "Yohoooo, I am at your gate.",
            "It is dinner time!",
            "Ok, I am leaving without you."
        };

        String result = Message.searchByRecipient(recipients, messages, messages.length, "+27838884567");
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.\nOk, I am leaving without you.",
            result
        );
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String[] hashes = {msg2.getMessageHash()};
        String[] messageIDs = {msg2.getMessageID()};
        String[] recipients = {msg2.getRecipient()};
        String[] senders = {"Lindokuhle Mcetywa"};
        String[] messages = {msg2.getMessageText()};
        int[] countRef = {1};

        String result = Message.deleteByMessageHash(
            hashes,
            messageIDs,
            recipients,
            senders,
            messages,
            countRef,
            msg2.getMessageHash()
        );

        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            result
        );
        assertEquals(0, countRef[0]);
    }

    @Test
    public void testDisplayReport() {
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        Message msg4 = new Message(4, "0838884567", "It is dinner time!");

        String[] hashes = {msg1.getMessageHash(), msg4.getMessageHash()};
        String[] recipients = {msg1.getRecipient(), msg4.getRecipient()};
        String[] messages = {msg1.getMessageText(), msg4.getMessageText()};

        String report = Message.displayFullReport(hashes, recipients, messages, 2);
        assertTrue(report.contains(msg1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(msg4.getMessageHash()));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("It is dinner time!"));
    }

    @Test
    public void testMessageLengthValidation() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength(msg.getMessageText()));

        String longText = new String(new char[255]).replace('\0', 'a');
        assertEquals("Message exceeds 250 characters by 5, please reduce size.", msg.checkMessageLength(longText));
    }

    @Test
    public void testRecipientCellValidation() {
        Message msg = new Message(1, "+27718693002", "Test");
        assertTrue(msg.checkRecipientCell("+27718693002"));
        assertFalse(msg.checkRecipientCell("08575975889"));
    }
}
