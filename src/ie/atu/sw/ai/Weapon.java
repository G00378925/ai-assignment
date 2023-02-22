package ie.atu.sw.ai;

public enum Weapon {
    AK47("AK47", 100, 0),
    AR15("AR15", 80, 0),
    AXE("Axe", 0, 0),
    BATTON("Batton", 10, 50),
    CROSSBOW("Crossbow", 40, 0),
    C4("C4", 100, 0),
    FLAMETHROWER("FlameThrower", 60, 0),
    GLOCK("Glock", 40, 0),
    JAVELIN("Javelin", 40, 0),
    SWORD("Sword", 40, 20);
    
    public static Weapon[] getWeapons() {
        return new Weapon[] {
            AK47, AR15, AXE, BATTON, CROSSBOW, C4,
            FLAMETHROWER, GLOCK, JAVELIN, SWORD
        };
    }
    
	private String name;
    private double attackPoints, defencePoints;

    private Weapon(String name, int attackPoints, int defencePoints) {
        this.name = name;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
    }

    public String getName() {
        return this.name;
    }

    public double getAttackPoints() {
        return this.attackPoints;
    }

    public double getDefencePoints() {
        return this.defencePoints;
    }
}
