package ie.atu.sw.ai;

public abstract class GameCharacterFL extends GameCharacter implements GameCharacterable {
    // Fuzzy Logic

    public GameCharacterFL(Location location) {
		    super(location);
    }

    public String getAIType() {
        return "FL";
    }
}