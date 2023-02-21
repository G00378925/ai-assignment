package ie.atu.sw.ai;

public enum Weapon {
    SWORD(0, 0, 0),
    AK47(1, 0, 0),
    AXE(2, 0, 0),
    CROSSBOW(3, 0, 0);
    
    private int weaponID, attackPoints, defencePoints;

    private Weapon(int weaponID, int attackPoints, int defencePoints) {
        this.weaponID = weaponID;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
    }

    public int getWeaponID() {
        return weaponID;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getDefencePoints() {
        return defencePoints;
    }

    public String toString() {
        return switch (this.weaponID) {
            case 0 -> "Sword";
            case 1 -> "AK47";
            default -> "Undefined";
        };
    }

    public static Weapon[] getWeapons() {
        return new Weapon[] {
            SWORD, AK47
        };
    }

    public static Weapon getWeapon(int weaponID) {
        return switch (weaponID) {
            case 0 -> SWORD;
            case 1 -> AK47;
            default -> null;
        };
    }
}
