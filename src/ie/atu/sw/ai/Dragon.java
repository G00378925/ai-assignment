package ie.atu.sw.ai;

public class Dragon extends GameCharacterFL implements GameCharacterable {
    private static final String NAME = "Dragon";

    public Dragon(Location location) {
        super(location, NAME, ConsoleColour.RED);
    }
    
    public void fight(Weapon weapon, Player opponent) {
    }
}