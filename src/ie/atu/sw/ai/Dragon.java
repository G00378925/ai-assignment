package ie.atu.sw.ai;

public class Dragon extends GameCharacterFL implements GameCharacterable {
    // Fuzzy Logic

    public Dragon(Location location) {
        super(location);
    }
    
    public float fight(Weapon weapon, Player opponent) {
		    return 0;
    }

    public String getAIType() {
        return "FL";
    }

    public String toString() {
        return ConsoleColour.RED + "Dragon" + ConsoleColour.RESET;
    }
}