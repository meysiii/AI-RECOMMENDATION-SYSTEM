import java.util.*;

public class RecommendationSystem {

    static Map<Integer, Map<Integer, Integer>> userRatings = new HashMap<>();

    public static void main(String[] args) {

        loadSampleData();

        int targetUser = 1;

        List<Integer> recommendations = recommendItems(targetUser);

        System.out.println("AI-Based Recommendations for User " + targetUser + ":");
        for (int item : recommendations) {
            System.out.println("Recommended Item ID: " + item);
        }
    }

    // Load sample data
    static void loadSampleData() {
        addRating(1, 101, 5);
        addRating(1, 102, 3);
        addRating(1, 103, 2);

        addRating(2, 101, 4);
        addRating(2, 102, 2);
        addRating(2, 104, 5);

        addRating(3, 101, 2);
        addRating(3, 103, 5);
        addRating(3, 104, 4);
    }

    static void addRating(int user, int item, int rating) {
        userRatings
                .computeIfAbsent(user, k -> new HashMap<>())
                .put(item, rating);
    }

    // Recommendation logic (Collaborative Filtering)
    static List<Integer> recommendItems(int targetUser) {

        Map<Integer, Integer> targetRatings = userRatings.get(targetUser);
        Map<Integer, Integer> itemScores = new HashMap<>();

        for (int otherUser : userRatings.keySet()) {

            if (otherUser == targetUser)
                continue;

            Map<Integer, Integer> otherRatings = userRatings.get(otherUser);

            for (int item : otherRatings.keySet()) {
                if (!targetRatings.containsKey(item)) {
                    itemScores.put(
                            item,
                            itemScores.getOrDefault(item, 0)
                                    + otherRatings.get(item));
                }
            }
        }

        // Sort items by score
        List<Map.Entry<Integer, Integer>> sortedItems = new ArrayList<>(itemScores.entrySet());

        sortedItems.sort((a, b) -> b.getValue() - a.getValue());

        List<Integer> recommendedItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : sortedItems) {
            recommendedItems.add(entry.getKey());
        }

        return recommendedItems;
    }
}
