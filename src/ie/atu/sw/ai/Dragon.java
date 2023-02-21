package ie.atu.sw.ai;

public class Dragon extends GameCharacterFL implements GameCharacterable {
    private static final String COLOUR_NAME = ConsoleColour.RED + "Dragon" + ConsoleColour.RESET;
    private static final String NAME = "Dragon";

    public Dragon(Location location) {
        super(location, NAME, COLOUR_NAME);
    }
    
    public void fight(Weapon weapon, Player opponent) {
    }
}