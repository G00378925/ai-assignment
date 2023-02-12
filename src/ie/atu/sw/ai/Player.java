package ie.atu.sw.ai;

public class Player {
    private Location currentLocation;
    private Weapon weapon;

    private double health = 100;

    public Player(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }


    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double look() {
        System.out.println("You look around.");
        return 0;
    }

    public double get() {
        System.out.println("You get something.");
        return 0;
    }

    public double fight() {
        System.out.println("You fight something.");
        return 0;
    }

    public double eat() {
        System.out.println("You eat something.");
        return 0;
    }

    public double tell() {
        System.out.println("You tell something.");
        return 0;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}