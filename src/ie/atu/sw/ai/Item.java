package ie.atu.sw.ai;

public enum Item {
    GOLD(0), SHIELD(1), KEY(2);

    private int itemID;
    
    private Item(int itemID) {
        this.itemID = itemID;
    }

    public int getItemID() {
        return this.itemID;
    }

    public static Item getItem(int itemID) {
        return switch (itemID) {
            case 0 -> GOLD;
            case 1 -> SHIELD;
            case 2 -> KEY;
            default -> null;
        };
    }
    
    public String toString() {
        return switch (this.itemID) {
            case 0 -> "Gold";
            case 1 -> "Shield";
            case 2 -> "Key";
            default -> "Undefined";
        };
    }
}