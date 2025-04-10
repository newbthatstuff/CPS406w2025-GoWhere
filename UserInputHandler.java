import java.util.Map;

public class UserInputHandler {
    private Map<String, Location> locations;

    public UserInputHandler(Map<String, Location> locations) {
        this.locations = locations;
    }

    public Location getLocationFromText(String input) {
        String lowercaseInput = input.trim().toLowerCase();
        for (String key : locations.keySet()) {
            if (key.toLowerCase().equals(lowercaseInput)) {
                return locations.get(key);
            }
        }
        return null;
    }
}