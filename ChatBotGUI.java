import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.text.similarity.FuzzyScore;
import java.util.Random;

public class ChatBotGUI extends JFrame {

    // --- ChatBot Logic ---
    // A HashMap where the key is the intent and the value is a list of possible responses
    private static HashMap<String, List<String>> responses = new HashMap<>();
    private static HashMap<String, List<String>> intents = new HashMap<>();
    private static final double FUZZY_THRESHOLD = 0.5;
    private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);
    private static final Random random = new Random();

    // Static initializer block to set up data when the class loads
    static {
        // Define responses as lists to allow for multiple options
        responses.put("greeting", Arrays.asList("Hi there! How can I help you?", "Hello! What can I do for you today?", "Hey! I'm ready to chat."));
        responses.put("feeling", Arrays.asList("I'm just a bot, but I'm doing great! How about you?", "I'm functioning perfectly, thanks for asking! And you?", "Doing well! How are you feeling?"));
        responses.put("goodbye", Arrays.asList("Goodbye! Have a nice day!", "See you later!", "Talk to you soon!"));
        // Updated the "thanks" response to only return "You're welcome!"
        responses.put("thanks", Arrays.asList("You're welcome!"));
        responses.put("joke", Arrays.asList("Why don't scientists trust atoms? Because they make up everything!", "What do you call a fake noodle? An Impasta!", "I told my wife she should embrace her mistakes. She gave me a hug."));
        responses.put("info", Arrays.asList("I am a simple chatbot created in Java.", "I'm a Java-based conversational bot.", "My creator built me using the Java programming language."));
        responses.put("status", Arrays.asList("That's great to hear! How can I help you?", "Wonderful! What's on your mind?", "Good to know! What can we chat about?"));
        responses.put("location", Arrays.asList("I'm currently running on your computer. Where are you?"));
        responses.put("weather", Arrays.asList("I am not sure of the current weather, as I'm just a simple chatbot.", "I can't check the weather, but I hope it's sunny where you are!"));
        responses.put("unknown", Arrays.asList("Sorry, I didn’t understand that. Could you rephrase?", "I'm not sure what you mean. Can you try saying it differently?", "I'm still learning. Try a different question!", "I'm not equipped to answer that right now."));


        // Define intents with more variations to improve fuzzy matching
        intents.put("greeting", Arrays.asList("hi", "hello", "hey", "good morning", "good evening", "i want your help", "how do you do", "what's up", "can you help me"));
        intents.put("feeling", Arrays.asList("how are you", "how are you doing", "how's it going", "how you doing", "how's life", "how have you been"));
        intents.put("goodbye", Arrays.asList("bye", "goodbye", "see you", "exit", "farewell", "got to go", "later"));
        intents.put("thanks", Arrays.asList("thanks", "thank you", "thx", "ok", "okay", "cheers", "much appreciated", "thank you so much"));
        intents.put("joke", Arrays.asList("tell me a joke", "tell me joke", "make me laugh", "joke", "funny", "can you tell me a joke", "do you know any jokes"));
        intents.put("info", Arrays.asList("what is your name", "who are you", "what are you"));
        intents.put("status", Arrays.asList("i am good", "i am great", "great", "doing good", "doing great", "i'm good", "i'm great"));
        intents.put("location", Arrays.asList("where are you", "what is your location", "where do you live"));
        // Added more phrases for weather intent to improve matching
        intents.put("weather", Arrays.asList("what's the weather", "how's the weather", "is it raining", "is it sunny", "will it snow", "what's the weather today", "how is today's weather", "how is the weather"));
    }

    private static String getIntent(String userInput) {
        String bestIntent = "unknown";
        double bestScore = 0.0;
        for (Map.Entry<String, List<String>> entry : intents.entrySet()) {
            for (String phrase : entry.getValue()) {
                double score = fuzzyScore.fuzzyScore(phrase, userInput);
                if (score > bestScore && score >= (phrase.length() * FUZZY_THRESHOLD)) {
                    bestScore = score;
                    bestIntent = entry.getKey();
                }
            }
        }
        return bestIntent;
    }
    
    // Method to get a random response from the list of options
    private static String getRandomResponse(String intent) {
        List<String> possibleResponses = responses.getOrDefault(intent, Collections.singletonList("Sorry, I didn’t understand that."));
        return possibleResponses.get(random.nextInt(possibleResponses.size()));
    }
    // --- End of ChatBot Logic ---

    // --- UI Components ---
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ChatBotGUI() {
        // --- Frame and Layout Setup ---
        setTitle("Simple ChatBot GUI");
        setSize(600, 600); // Adjusted height
        setMinimumSize(new Dimension(500, 500)); // Set a minimum size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // --- Color and Font Definitions ---
        Color primaryColor = new Color(51, 51, 51);      // Dark Grey for background
        Color secondaryColor = new Color(255, 165, 0);   // Orange for accents
        Color tertiaryColor = new Color(240, 240, 240);  // Light Grey for chat text area
        Font mainFont = new Font("Arial", Font.PLAIN, 16);
        Font boldFont = new Font("Arial", Font.BOLD, 18);

        // --- Chat Area Setup ---
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(tertiaryColor);
        chatArea.setForeground(primaryColor);
        chatArea.setFont(mainFont);
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor, 2), "Chat Log", 
            javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, 
            boldFont, primaryColor));
        add(scrollPane, BorderLayout.CENTER);

        // --- Input Panel Setup ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(primaryColor);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(mainFont);
        inputField.setBackground(Color.WHITE);
        inputField.setForeground(primaryColor);
        inputField.setBorder(BorderFactory.createLineBorder(secondaryColor, 1));
        
        sendButton = new JButton("Send");
        sendButton.setBackground(secondaryColor);
        sendButton.setForeground(primaryColor);
        sendButton.setFocusPainted(false);
        sendButton.setFont(boldFont);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        ActionListener sendMessageListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText().toLowerCase().trim();
                if (!userInput.isEmpty()) {
                    displayUserMessage(userInput);
                    String cleanInput = userInput.replaceAll("[^a-zA-Z ]", "").replaceAll("\\s+", " ").trim();
                    String intent = getIntent(cleanInput);
                    String reply = "";

                    if (intent.equals("goodbye")) {
                        reply = getRandomResponse(intent);
                        displayBotMessage(reply);
                        SwingUtilities.invokeLater(() -> {
                            try { Thread.sleep(1000); } catch (InterruptedException ex) {}
                            System.exit(0);
                        });
                    } else {
                        reply = getRandomResponse(intent);
                        displayBotMessage(reply);
                    }
                    inputField.setText("");
                    inputField.requestFocusInWindow();
                }
            }
        };

        inputField.addActionListener(sendMessageListener);
        sendButton.addActionListener(sendMessageListener);

        // Display a welcome message when the application starts
        displayBotMessage(getRandomResponse("greeting"));
    }
    
    private void displayUserMessage(String message) {
        chatArea.append("You: " + message + "\n");
    }

    private void displayBotMessage(String message) {
        chatArea.append("🤖 ChatBot: " + message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatBotGUI().setVisible(true);
        });
    }
}
