import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONObject;

public class Message {

    private String messageID;
    private int messageNo;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(int messageNo, String recipient, String messageText) {
        this.messageNo = messageNo;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        long number = ThreadLocalRandom.current().nextLong(1000000000L, 10000000000L);
        return String.valueOf(number);
    }

    public boolean checkRecipientCell(String cellNumber) {
        return cellNumber != null && cellNumber.matches("^\\+27[0-9]{9}$");
    }

    public boolean checkMessageLength(String message) {
        return message != null && message.length() <= 250;
    }

    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + messageNo + ":";
        }

        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2) + ":" + messageNo + ":"
                + firstWord + lastWord;
    }

    public void printMessage() {
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Message Number: " + messageNo);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageText);
    }

    public void storeMessage() {
        JSONObject messageObject = new JSONObject();
        messageObject.put("messageID", messageID);
        messageObject.put("messageNo", messageNo);
        messageObject.put("recipient", recipient);
        messageObject.put("messageText", messageText);
        messageObject.put("messageHash", messageHash);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("messages.json", true))) {
            writer.write(messageObject.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Unable to store message: " + e.getMessage());
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageNo() {
        return messageNo;
    }

    public static List<String> loadStoredMessagesFromJson(String filePath) {
        List<String> storedMessages = new ArrayList<>();

        try {
            if (!Files.exists(Paths.get(filePath))) {
                return storedMessages;
            }

            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                if (line != null && !line.trim().isEmpty()) {
                    try {
                        JSONObject messageObject = new JSONObject(line);
                        storedMessages.add(messageObject.optString("messageText", ""));
                    } catch (org.json.JSONException e) {
                        System.out.println("A stored message could not be read.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to load stored messages: " + e.getMessage());
        }

        return storedMessages;
    }

    public static void displaySendersAndRecipients(String[] senders, String[] recipients, int count) {
        System.out.println("Senders and Recipients:");

        for (int i = 0; i < count; i++) {
            if (senders[i] != null && recipients[i] != null) {
                System.out.println("Sender: " + senders[i] + " | Recipient: " + recipients[i]);
            }
        }
    }

    public static void findLongestMessage(String[] messages, int count) {
        String longestMessage = "";

        for (int i = 0; i < count; i++) {
            if (messages[i] != null && messages[i].length() > longestMessage.length()) {
                longestMessage = messages[i];
            }
        }

        if (longestMessage.isEmpty()) {
            System.out.println("No messages available.");
        } else {
            System.out.println("Longest Message: " + longestMessage);
        }
    }

    public static void searchByMessageID(String[] messageIDs, String[] recipients,
            String[] messages, int count, String searchID) {

        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(searchID)) {
                System.out.println("Recipient: " + recipients[i]);
                System.out.println("Message: " + messages[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Message ID not found.");
        }
    }

    public static void searchByRecipient(String[] recipients, String[] messages,
            int count, String searchRecipient) {

        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (recipients[i] != null && recipients[i].equals(searchRecipient)) {
                System.out.println("Message: " + messages[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    public static void deleteByMessageHash(String[] messageHashes, String[] messageIDs,
            String[] recipients, String[] senders, String[] messages,
            int[] countRef, String deleteHash) {

        boolean found = false;

        for (int i = 0; i < countRef[0]; i++) {
            if (messageHashes[i] != null && messageHashes[i].equals(deleteHash)) {
                for (int j = i; j < countRef[0] - 1; j++) {
                    messageHashes[j] = messageHashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    recipients[j] = recipients[j + 1];
                    senders[j] = senders[j + 1];
                    messages[j] = messages[j + 1];
                }

                countRef[0]--;

                messageHashes[countRef[0]] = null;
                messageIDs[countRef[0]] = null;
                recipients[countRef[0]] = null;
                senders[countRef[0]] = null;
                messages[countRef[0]] = null;

                System.out.println("Message successfully deleted.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Message hash not found.");
        }
    }

    public static void displayFullReport(String[] messageHashes, String[] recipients,
            String[] messages, int count) {

        if (count == 0) {
            System.out.println("No sent messages to display.");
            return;
        }

        System.out.println("Full Message Report:");

        for (int i = 0; i < count; i++) {
            System.out.println("Message Hash: " + messageHashes[i]);
            System.out.println("Recipient: " + recipients[i]);
            System.out.println("Message: " + messages[i]);
            System.out.println("------------------------------");
        }
    }
}
