package ie.atu.sw.ai;

public interface GameCharacterable {
    public String getAIType();

    public float fight(Weapon weapon, Player opponent);
}