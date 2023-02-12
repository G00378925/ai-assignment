package ie.atu.sw.ai;

public enum Weapon {
    SWORD(0), AK47(1),
    AXE(2), CROSSBOW(3);
	
	private int weaponID;

    private Weapon(int weaponID) {
        this.weaponID = weaponID;
    }

    public int getWeaponID() {
        return weaponID;
    }

    public String toString() {
        return switch (this.weaponID) {
            case 0 -> "Sword";
            case 1 -> "AK47";
            default -> "Undefined";
        };
    }
}
