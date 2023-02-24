package ie.atu.sw.ai;

public enum Weapon {
    AK47("AK47", 100, 0, false),
    AR15("AR15", 80, 0, false),
    AXE("Axe", 0, 0, true),
    BATTON("Batton", 10, 50, false),
    CROSSBOW("Crossbow", 40, 0, true),
    C4("C4", 100, 0, false),
    FLAMETHROWER("FlameThrower", 60, 0, false),
    GLOCK("Glock", 40, 0, false),
    JAVELIN("Javelin", 40, 0, false),
    SWORD("Sword", 40, 20, true);
    
    public static Weapon[] getWeapons() {
        return new Weapon[] {
            AK47, AR15, AXE, BATTON, CROSSBOW, C4,
            FLAMETHROWER, GLOCK, JAVELIN, SWORD
        };
    }
    
	private String name;
    private double attackPoints, defencePoints;
    private boolean sharp;

    private Weapon(String name, int attackPoints, int defencePoints, boolean sharp) {
        this.name = name;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.sharp = sharp;
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
    
    public boolean isSharp() {
    	return this.sharp;
    }

    public String toString() {
        return this.name;
    }
}
