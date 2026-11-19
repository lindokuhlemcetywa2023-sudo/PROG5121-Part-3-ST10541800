import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        private static final int MAX_MESSAGES = 100;
        private static String[] sentMessages = new String[MAX_MESSAGES];
        private static String[] recipients = new String[MAX_MESSAGES];
        private static String[] senders = new String[MAX_MESSAGES];
        private static String[] hashes = new String[MAX_MESSAGES];
        private static String[] messageIDs = new String[MAX_MESSAGES];
        
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("Welcome to QuickChat.");
        System.out.println("=== QUICKCHAT REGISTRATION ===");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter cellphone (+27 format): ");
        String cell = scanner.nextLine();

        String regResult = login.registerUser(
    username, password, cell, firstName, lastName);
        System.out.println(regResult);

        if (!regResult.contains("successfully")) {
            System.out.println("Registration failed. Cannot proceed.");
            scanner.close();
            return;
        }

        System.out.println("\n=== LOGIN ===");
        System.out.print("Username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Password: ");
        String loginPass = scanner.nextLine();

        boolean loggedIn = login.loginUser(loginUser, loginPass);
        System.out.println(login.returnLoginStatus(loggedIn, loginUser));

        if (!loggedIn) {
            System.out.println("Username or password incorrect. Please login successfully before sending messages.");
            scanner.close();
            return;
        }

        final int MAX_MESSAGES = 100;
        String[] sentMessages = new String[MAX_MESSAGES];
        String[] sentRecipients = new String[MAX_MESSAGES];
        String[] sentSenders = new String[MAX_MESSAGES];
        String[] messageHashes = new String[MAX_MESSAGES];
        String[] messageIDs = new String[MAX_MESSAGES];
        String[] disregardedMessages = new String[MAX_MESSAGES];
        String[] storedMessages = Message.loadStoredMessagesFromJson("messages.json");

        int[] sentCountRef = {0};
        int disregardedCount = 0;
        int messageCounter = 0;

        String senderFullName = firstName + " " + lastName;
        boolean running = true;

        while (running) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages & Reports");
            System.out.print("Choose an option: ");
            String menuChoice = scanner.nextLine();

            switch (menuChoice) {
                case "1":
                    System.out.print("\nHow many messages do you want to send? ");
                    int numMessages = Integer.parseInt(scanner.nextLine());

                    for (int i = 1; i <= numMessages; i++) {
                        messageCounter++;
                        System.out.println("\n--- Message " + i + " ---");
                        System.out.print("Enter recipient cell (+27): ");
                        String recipient = scanner.nextLine();

                        Message tempMsg = new Message(messageCounter, recipient, "");
                        while (!tempMsg.checkRecipientCell(recipient)) {
                            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.");
                            System.out.print("Re-enter recipient: ");
                            recipient = scanner.nextLine();
                        }

                        System.out.print("Enter your message (max 250 chars): ");
                        String text = scanner.nextLine();

                        Message message = new Message(messageCounter, recipient, text);
                        String lengthCheck = message.checkMessageLength(text);
                        System.out.println(lengthCheck);

                        if (lengthCheck.contains("exceeds")) {
                            continue;
                        }

                        System.out.println("\n1) Send message\n2) Disregard message\n3) Store message to send later");
                        System.out.print("Choose option: ");
                        String option = scanner.nextLine();

                        switch (option) {
                            case "1":
                                int idx = sentCountRef[0];
                                sentMessages[idx] = message.getMessageText();
                                sentRecipients[idx] = message.getRecipient();
                                sentSenders[idx] = senderFullName;
                                messageHashes[idx] = message.getMessageHash();
                                messageIDs[idx] = message.getMessageID();
                                sentCountRef[0]++;

                                System.out.println("\n" + message.printMessage());
                                System.out.println("Message successfully sent.");
                                break;
                            case "2":
                                disregardedMessages[disregardedCount++] = message.getMessageText();
                                System.out.println("Press 0 to delete message.");
                                break;
                            case "3":
                                message.storeMessage();
                                storedMessages = Message.loadStoredMessagesFromJson("messages.json");
                                System.out.println("Message successfully stored.");
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                    break;

                case "2":
                    System.out.println(Message.displayFullReport(messageHashes, sentRecipients, sentMessages, sentCountRef[0]));
                    break;

                case "3":
                    System.out.println("\nTotal messages sent: " + sentCountRef[0]);
                    running = false;
                    break;

                case "4":
                    System.out.println("\n--- Stored Messages & Reports ---");
                    System.out.println("a) Display sender and recipient of all sent messages");
                    System.out.println("b) Display the longest message");
                    System.out.println("c) Search by Message ID");
                    System.out.println("d) Search messages by recipient");
                    System.out.println("e) Delete a message using Message Hash");
                    System.out.println("f) Display full report of sent messages");
                    System.out.println("g) View messages loaded from JSON (" + storedMessages.length + " stored)");
                    System.out.print("Select report option (a-g): ");
                    String subOption = scanner.nextLine().toLowerCase();

                    switch (subOption) {
                        case "a":
                            System.out.println("\n" + Message.displaySendersAndRecipients(sentSenders, sentRecipients, sentCountRef[0]));
                            break;
                        case "b":
                            System.out.println("\nLongest Message: " + Message.findLongestMessage(sentMessages, sentCountRef[0]));
                            break;
                        case "c":
                            System.out.print("Enter Message ID to search: ");
                            String searchId = scanner.nextLine();
                            System.out.println("\n" + Message.searchByMessageID(messageIDs, sentRecipients, sentMessages, sentCountRef[0], searchId));
                            break;
                        case "d":
                            System.out.print("Enter Recipient cell number (+27...): ");
                            String searchRecip = scanner.nextLine();
                            System.out.println("\n" + Message.searchByRecipient(sentRecipients, sentMessages, sentCountRef[0], searchRecip));
                            break;
                        case "e":
                            System.out.print("Enter Message Hash to delete: ");
                            String delHash = scanner.nextLine();
                            System.out.println("\n" + Message.deleteByMessageHash(messageHashes, messageIDs, sentRecipients, sentSenders, sentMessages, sentCountRef, delHash));
                            break;
                        case "f":
                            System.out.println("\n" + Message.displayFullReport(messageHashes, sentRecipients, sentMessages, sentCountRef[0]));
                            break;
                        case "g":
                            storedMessages = Message.loadStoredMessagesFromJson("messages.json");
                            if (storedMessages.length == 0) {
                                System.out.println("No stored messages found in messages.json.");
                            } else {
                                System.out.println("=== STORED MESSAGES (FROM JSON) ===");
                                for (String msg : storedMessages) {
                                    System.out.println("- " + msg);
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid report option.");
                    }
                    break;

                default:
                    System.out.println("Invalid menu option. Please select 1-4.");
            }
        }

        scanner.close();
    }
}
