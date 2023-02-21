package ie.atu.sw.ai;

public interface GameCharacterable {
    public String getAIType();

    public void fight(Weapon weapon, Player opponent);
    
    public String getName();
}
