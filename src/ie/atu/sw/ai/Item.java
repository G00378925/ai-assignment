package ie.atu.sw.ai;

public enum Item {
    KEY("Key", false, 0),
    BEETROOT("Beetroot", true, 0),
    CARROT("Carrot", true, 0);
	
	private String name;
    private boolean edible;
    private float healthValue;
    
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