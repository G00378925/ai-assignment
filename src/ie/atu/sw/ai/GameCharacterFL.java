package ie.atu.sw.ai;

// Extend this, and you can be a fuzzy logic enemy too!
public abstract class GameCharacterFL extends GameCharacter implements GameCharacterable {
    // Fuzzy Logic

    public GameCharacterFL(Location location, String name, ConsoleColour consoleColour) {
        super(location, name, consoleColour);
    }
}
