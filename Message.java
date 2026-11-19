import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        Random rand = new Random();
        long id = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

public boolean checkRecipientCell(String cellNumber) {
    return cellNumber != null
            && cellNumber.matches("^\\+27[0-9]{9}$");
    }
    

    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";

        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        return firstTwo + ":" + messageNo + ":" + (firstWord + lastWord).toUpperCase();
    }

    public String printMessage() {
        return "MessageID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    public void storeMessage() {
        JSONObject obj = new JSONObject();
        obj.put("messageID", messageID);
        obj.put("messageNo", messageNo);
        obj.put("recipient", recipient);
        obj.put("messageText", messageText);
        obj.put("messageHash", messageHash);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(obj.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
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

    public static String[] loadStoredMessagesFromJson(String filePath) {
        List<String> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    JSONObject obj = new JSONObject(line);
                    loaded.add(obj.optString("messageText", ""));
                }
            }
        } catch (IOException e) {
        }
        return loaded.toArray(new String[0]);
    }

    public static String displaySendersAndRecipients(String[] senders, String[] recipients, int count) {
        if (count == 0) {
            return "No sent messages available.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("Sender: ").append(senders[i])
              .append(" | Recipient: ").append(recipients[i]).append("\n");
        }
        return sb.toString().trim();
    }

    public static String findLongestMessage(String[] messages, int count) {
        if (count == 0 || messages == null) {
            return "No messages available.";
        }
        String longest = messages[0];
        for (int i = 1; i < count; i++) {
            if (messages[i] != null && messages[i].length() > longest.length()) {
                longest = messages[i];
            }
        }
        return longest;
    }

    public static String searchByMessageID(String[] messageIDs, String[] recipients, String[] messages, int count, String targetID) {
        for (int i = 0; i < count; i++) {
            if (targetID.equals(messageIDs[i])) {
                return "Recipient: " + recipients[i] + "\nMessage: " + messages[i];
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String[] recipients, String[] messages, int count, String targetRecipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (targetRecipient.equals(recipients[i])) {
                sb.append(messages[i]).append("\n");
            }
        }
        String result = sb.toString().trim();
        return result.isEmpty() ? "No messages found for recipient: " + targetRecipient : result;
    }

    public static String deleteByMessageHash(String[] hashes, String[] messageIDs, String[] recipients, String[] senders, String[] messages, int[] countRef, String targetHash) {
        int count = countRef[0];
        for (int i = 0; i < count; i++) {
            if (targetHash.equals(hashes[i])) {
                String deletedText = messages[i];
                for (int j = i; j < count - 1; j++) {
                    hashes[j] = hashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    recipients[j] = recipients[j + 1];
                    senders[j] = senders[j + 1];
                    messages[j] = messages[j + 1];
                }
                hashes[count - 1] = null;
                messageIDs[count - 1] = null;
                recipients[count - 1] = null;
                senders[count - 1] = null;
                messages[count - 1] = null;
                countRef[0] = count - 1;
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayFullReport(String[] hashes, String[] recipients, String[] messages, int count) {
        if (count == 0) {
            return "No messages to display in report.";
        }
        StringBuilder sb = new StringBuilder("=== SENT MESSAGES REPORT ===\n");
        for (int i = 0; i < count; i++) {
            sb.append("Message Hash: ").append(hashes[i]).append("\n")
              .append("Recipient: ").append(recipients[i]).append("\n")
              .append("Message: ").append(messages[i]).append("\n")
              .append("----------------------------\n");
        }
        return sb.toString().trim();
    }
}
