package ie.atu.sw.ai;

import java.util.*;

public class Location {
    private static String LOCATION_NAMES[] = {
        "Cave", "Dessert", "Volcano", "Castle", "Laboratory"
    };

    private static String LOCATION_DESCRIPTIONS[] = {
        "Cave filled with blood thirsty bats.",
        "Dry dessert with nobody for kilometers.",
        "Volcano filled with bubbling lava.",
        "Cold castle filled with ghosts.",
        "Laboratory with leaking boiling flasks."
    };

    private static Item LOCATION_ITEMS[][] = {
        {Item.BEETROOT, Item.CARROT},
        {Item.BEETROOT, Item.CARROT},
        {Item.BEETROOT, Item.CARROT},
        {Item.BEETROOT, Item.CARROT},
        {Item.BEETROOT, Item.CARROT}
    };

    private static Weapon LOCATION_WEAPONS[][] = {
        {Weapon.AK47, Weapon.AR15},
        {Weapon.AXE, Weapon.BATTON},
        {Weapon.C4, Weapon.CROSSBOW},
        {Weapon.FLAMETHROWER, Weapon.JAVELIN},
        {Weapon.GLOCK, Weapon.SWORD}
    };

    private String name, description;
    private HashMap<String, Location> edges;
    private boolean isExit, playerHere;

    private Dictionary<String, Item> items = new Hashtable<String, Item>();
    private Dictionary<String, Weapon> weapons = new Hashtable<String, Weapon>();
    private Dictionary<String, GameCharacterable> enemies = new Hashtable<String, GameCharacterable>();

    public HashMap<String, Location> getEdges() {
        return this.edges;
    }

    public void setExit() {
        this.isExit = true;
    }

    public boolean isExit() {
        return this.isExit;
    }
    
    public void togglePlayerHere() {
    	this.playerHere = !this.playerHere;
    }
    
    public boolean isPlayerHere() {
    	return this.playerHere;
    }

    public void addEdge(String direction, Location location) {
        this.edges.put(direction, location);
    }

    public Location getRandomEdge() {
    	int randomEdgeIndex = new Random().nextInt(this.edges.size());
        return (Location) this.edges.values().toArray()[randomEdgeIndex];
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

    private static Location setupLocationGraph(int currentDepth, int maxDepth,
            Collection<Location> locations, String direction, Location previousLocation) {
        Location newLocation = new Location();
        locations.add(newLocation);
        
        if (currentDepth > maxDepth) {
            newLocation.setExit();
            newLocation.addEdge(reverseLocation(direction), previousLocation);
            return newLocation;
        }
        
        for (int i = 0; i < 4; i++) {
            String newDirection = switch (i) {
                case 0 -> "North";
                case 1 -> "South";
                case 2 -> "East";
                case 3 -> "West";
                default -> null;
            };
            
            if (direction != null) {
                if (newDirection.equals(reverseLocation(direction)))
                    continue;
            }
           
            Location newNewLocation = setupLocationGraph(currentDepth + 1, maxDepth, locations, newDirection, newLocation);
            newLocation.addEdge(newDirection.toUpperCase(), newNewLocation);
        }
        
        if (previousLocation != null)
            newLocation.addEdge(reverseLocation(direction).toUpperCase(), previousLocation);
        
        return newLocation;
    }
    
    public static Location setupLocationGraph(int maxDepth, Collection<Location> locations) {
    	return Location.setupLocationGraph(0, maxDepth, locations, null, null);
    }
    
    public Location() {
        this.edges = new HashMap<String, Location>();

        int randomLocationIndex = new Random().nextInt(LOCATION_NAMES.length);
        this.name = LOCATION_NAMES[randomLocationIndex];
        this.description = LOCATION_DESCRIPTIONS[randomLocationIndex];

        for (Item item : LOCATION_ITEMS[randomLocationIndex])
            this.items.put(item.toString(), item);

        for (Weapon weapon : LOCATION_WEAPONS[randomLocationIndex])
        	this.weapons.put(weapon.toString(), weapon);
        
        this.playerHere = false;
    }

    public String getDescription() {
        return this.description;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public Dictionary<String, GameCharacterable> getEnemies() {
    	return this.enemies;
    }
    
    public Dictionary<String, Item> getItems() {
    	return this.items;
    }
    
    public Dictionary<String, Weapon> getWeapons() {
    	return this.weapons;
    }
}
