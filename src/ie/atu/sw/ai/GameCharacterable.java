package ie.atu.sw.ai;

public interface GameCharacterable {
    public String getName();

    public void fight(Weapon weapon, Player opponent);

    public void pour(String objName);
}
