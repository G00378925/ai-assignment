package ie.atu.sw.ai;

public class Dragon extends GameCharacterFL implements GameCharacterable {
    // The dragon will use fuzzy logic.

    public Dragon(Location location) {
        super(location, "Dragon", ConsoleColour.RED);
    }

    public void fight(Weapon weapon, Player opponent) {
    }
}
