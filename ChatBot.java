import java.util.*;
import org.apache.commons.text.similarity.FuzzyScore;

public class ChatBot {
    private static HashMap<String, String> responses = new HashMap<>();
    private static HashMap<String, List<String>> intents = new HashMap<>();

    // Add a threshold to filter out low-quality matches
    private static final double FUZZY_THRESHOLD = 0.5;
    private static FuzzyScore fuzzyScore = new FuzzyScore(Locale.ENGLISH);

    public static void main(String[] args) {
        // Define responses
        responses.put("greeting", "Hi there! How can I help you?");
        responses.put("feeling", "I'm just a bot, but I'm doing great! How about you?");
        responses.put("goodbye", "Goodbye! Have a nice day!");
        responses.put("thanks", "You're welcome!");
        responses.put("joke", "Why don't scientists trust atoms? Because they make up everything!");
        responses.put("info", "I am a simple chatbot created in Java.");
        responses.put("status", "That's great to hear! How can I help you?");

        // Define intents with multiple variations
        intents.put("greeting", Arrays.asList("hi", "hello", "hey", "good morning", "good evening", "i want your help"));
        intents.put("feeling", Arrays.asList("how are you", "how are you doing", "how's it going"));
        intents.put("goodbye", Arrays.asList("bye", "goodbye", "see you", "exit"));
        intents.put("thanks", Arrays.asList("thanks", "thank you", "thx", "ok", "okay"));
        intents.put("joke", Arrays.asList("tell me a joke", "tell me joke", "make me laugh", "joke", "funny"));
        // Added 'what is your name?' to the intents list
        intents.put("info", Arrays.asList("what is your name", "what is ypur name", "who are you"));
        // Added phrases for confirming good status
        intents.put("status", Arrays.asList("i am good", "i am great", "great", "doing good", "doing great"));


        Scanner scanner = new Scanner(System.in);
        System.out.println("🤖 ChatBot: Hello! Type 'bye' to exit.");

        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().toLowerCase().trim();

            // Clean punctuation and multiple spaces
            userInput = userInput.replaceAll("[^a-zA-Z ]", "");
            userInput = userInput.replaceAll("\\s+", " ").trim();

            String intent = getIntent(userInput);

            if (intent.equals("goodbye")) {
                System.out.println("🤖 ChatBot: " + responses.get(intent));
                break;
            }

            String reply = responses.getOrDefault(intent, "Sorry, I didn’t understand that.");
            System.out.println("🤖 ChatBot: " + reply);
        }

        scanner.close();
    }

    // Match input with intents using FuzzyScore
    private static String getIntent(String userInput) {
        String bestIntent = "unknown";
        double bestScore = 0.0;

        for (Map.Entry<String, List<String>> entry : intents.entrySet()) {
            for (String phrase : entry.getValue()) {
                // Calculate the fuzzy score
                double score = fuzzyScore.fuzzyScore(phrase, userInput);

                // Update the best match if the current score is higher and meets the threshold
                if (score > bestScore && score >= (phrase.length() * FUZZY_THRESHOLD)) {
                    bestScore = score;
                    bestIntent = entry.getKey();
                }
            }
        }
        return bestIntent;
    }
}