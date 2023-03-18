package ie.atu.sw.ai;

public enum Weapon {
	// All the weapons and their values
	AK47("AK47", 100, 25, false), AR15("AR15", 80, 35, false), AXE("Axe", 55, 22, true),
	BATTON("Batton", 10, 50, false), CROSSBOW("Crossbow", 40, 10, true), C4("C4", 100, 0, false),
	FLAMETHROWER("FlameThrower", 60, 0, false), GLOCK("Glock", 40, 10, false), JAVELIN("Javelin", 10, 50, false),
	SWORD("Sword", 60, 60, true);

	private String name; // Name of the weapon
	private double attackPoints, defencePoints; // Points of the weapon
	private boolean sharp; // Is weapon sharp?

	private Weapon(String name, int attackPoints, int defencePoints, boolean sharp) {
		this.name = name;
		this.attackPoints = attackPoints;
		this.defencePoints = defencePoints;
		this.sharp = sharp;
	}

	// Return the attack for that weapon
	public double getAttackPoints() {
		return this.attackPoints;
	}

	// Return the defence value for that weapon
	public double getDefencePoints() {
		return this.defencePoints;
	}

	// Return a boolean true or false if the weapon is sharp
	public boolean isSharp() {
		return this.sharp;
	}

	public String toString() {
		return this.name;
	}
}
