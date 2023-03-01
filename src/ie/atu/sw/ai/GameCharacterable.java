package ie.atu.sw.ai;

// Implement this, and you can be an enemy too!
public interface GameCharacterable {
    public String getName();

    public void fight(Weapon weapon, Player opponent);

    public void pour(String objName);
}
