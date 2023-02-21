package ie.atu.sw.ai;

public abstract class GameCharacterFL extends GameCharacter implements GameCharacterable {
    // Fuzzy Logic

    public GameCharacterFL(Location location, String name, String colourName) {
        super(location, name, colourName);
    }

    public String getAIType() {
        return "FL";
    }
}
