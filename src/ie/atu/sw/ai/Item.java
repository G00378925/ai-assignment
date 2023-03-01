package ie.atu.sw.ai;

public enum Item {
    // All the items in the game, including their values
    KEY("Key", false, 0),
    APPLE("Apple", true, 10),
    BEETROOT("Beetroot", true, 8),
    BLUEBREAD("BlueBread", true, -10),
    CARROT("Carrot", true, 12),
    GRAPEFRUIT("Grapefruit", true, 5),
    PEAR("Pear", true, 10),
    PINEAPPLE("Pineapple", true, 10),
    POTATO("Potato", true, 10),
    ROTTENPOTATO("RottenPotato", true, -3),
    TURNIP("Turnip", true, 10);
    
    private String name;
    private boolean edible; // Key isn't edible
    private float healthValue; // Amount it restores, or deducts
    
    private Item(String name, boolean edible, float healthValue) {
        this.name = name;
        this.edible = edible;
        this.healthValue = healthValue;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isEdible() {
        return this.edible;
    }

    public float getHealthValue() {
        return this.healthValue;
    }
}
