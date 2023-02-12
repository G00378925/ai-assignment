package ie.atu.sw.ai;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class Location {
    private static String LOCATION_NAMES[] = {
        "Cave", "Dessert", "Volcano"
    };

    private static String LOCATION_DESCRIPTIONS[] = {
        "Cave filled with bats",
        "Dessert filled with sand",
        "Volcano filled with lava"
    };

    private static int LOCATION_ITEMS[][] = {
        {0, 0},
        {0, 0}
    };

    private static int LOCATION_WEAPONS[][] = {
        {0, 0},
        {0, 0}
    };

    private String name, description;
    private HashMap<String, Location> edges;

    private Collection<Item> items;
    private Collection<Weapon> weapons;

    private boolean isExit;

    public void addEdge(Location location, String direction) {
        this.edges.put(direction, location);
    }

    public static String reverseLocation(String locationStr) {
        return switch (locationStr.toUpperCase()) {
            case "NORTH" -> "South";
            case "SOUTH" -> "North";
            case "EAST" -> "West";
            case "WEST" -> "East";
            case "NORTHEAST" -> "SouthWest";
            case "NORTHWEST" -> "SouthEast";
            case "SOUTHEAST" -> "NorthWest";
            case "SOUTHWEST" -> "NorthEast";
            default -> null;
        };
    }

    public static Location setupLocationGraph(int currentDepth, int maxDepth, Collection<Location> locations, Location currentLocation, String direction, Location previousLocation) {
        Location newLocation = new Location();
        locations.add(newLocation);
        
        if (currentDepth < maxDepth)
            return newLocation;
        
        for (int i = 0; i < 4; i++) {
            String newDirection = switch (i) {
                case 0 -> "North";
                case 1 -> "South";
                case 2 -> "East";
                case 3 -> "West";
                default -> null;
            };
            
            if (newDirection == direction)
                continue;
            
            Location newEdgeLocation = setupLocationGraph(currentDepth + 1, maxDepth, locations, newLocation, newDirection, currentLocation);
            currentLocation.addEdge(newEdgeLocation, newDirection);
        }

        currentLocation.addEdge(previousLocation, reverseLocation(direction));
        
        return newLocation;
    }
    
    public Location() {
        this.edges = new HashMap<String, Location>();

        int randomLocationIndex = new Random().nextInt(LOCATION_NAMES.length);
        this.name = LOCATION_NAMES[randomLocationIndex];
        this.description = LOCATION_DESCRIPTIONS[randomLocationIndex];
    }

    public String getDescription() {
        return this.description;
    }
}
