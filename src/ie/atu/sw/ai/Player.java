package ie.atu.sw.ai;

import java.util.*;

public class Player {
    private Location currentLocation;
    private Dictionary<String, Item> items = new Hashtable<String, Item>();
    private Dictionary<String, Weapon> weapons = new Hashtable<String, Weapon>();

    private double health = 100;

    public Player(Location currentLocation, double health) {
        this.currentLocation = currentLocation;
        this.health = health;
    }
    
    public void giveItem(Item item) {
        this.items.put(item.toString(), item);
    }

    public Collection<GameCharacterable> getEnemies() {
        return Collections.list(currentLocation.getEnemies().elements());
    }

    public void look() {
    	// This method gives you a description of the current location
    	// the player is currently at
        System.out.println(currentLocation.getDescription());
        
        System.out.print("Enemies: ");
        for (GameCharacterable enemy : this.getEnemies())
            System.out.printf("%s ", enemy.getName());
        System.out.println();
        
        var items = currentLocation.getItems().elements();
        System.out.print("Items: ");
        for (Item item : Collections.list(items))
            System.out.printf("%s ", item.getName());
        System.out.println();

        var weapons = currentLocation.getWeapons().elements();
        System.out.print("Weapons: ");
        for (Weapon weapon : Collections.list(weapons))
            System.out.printf("%s ", weapon.toString());
        System.out.println();
    }

    public void get(String objName) {
    	// Allows you retrieve a weapon such as an item or weapon
    	// by its name
    	
        Item item = currentLocation.getItems().get(objName.toUpperCase());
        Weapon weapon = currentLocation.getWeapons().get(objName.toUpperCase());
        
        if (item != null) {
            System.out.printf("You recieved: %s\n", item.toString());
            currentLocation.getItems().remove(item.toString().toUpperCase());
            this.items.put(item.toString().toUpperCase(), item);
        }
        
        if (weapon != null) {
            System.out.printf("You recieved: %s\n", weapon.toString());
            currentLocation.getWeapons().remove(weapon.toString().toUpperCase());
            this.weapons.put(weapon.toString().toUpperCase(), weapon);
        }
    }

    public void fight(String enemyName, String weaponName) {
    	// Fight an enemy at the current location, with a specified weapon name.
        GameCharacterable enemy = currentLocation.getEnemies().get(enemyName);
        Weapon weapon = this.weapons.get(weaponName.toUpperCase());

        if (weapon == null && enemy != null) {
            System.out.printf("The %s looks in confusion, and asks where is your weapon?\n", enemy.getName());
            System.out.println("They want a fair fight, so you must acquire a weapon first.");
            return;
        }
        
        if (enemy != null) {
            System.out.printf("You are fighting the %s.\n", enemy.getName());
            enemy.fight(weapon, this);
        } else {
            System.out.printf("%s is not around, maybe try elsewhere?\n", enemyName);
        }
    }

    public void eat(String itemName) {
        Item foodObj = this.items.get(itemName);

        if (foodObj == null) {
            System.err.printf("You don't have the %s.\n", itemName);
        } else if (foodObj.isEdible()) {
            this.health += foodObj.getHealthValue();
            
            if (foodObj.getHealthValue() >= 0)
                System.out.printf("You have eaten the %s.\n", foodObj.getName());
            else
                System.out.printf("The %s poisoned you!\n", foodObj.getName());
            
            this.items.remove(itemName);
        } else if (!foodObj.isEdible()) {
            System.err.printf("You can't eat the %s.\n", foodObj.getName());
        }
    }

    public void tell(String commands[]) {
        GameCharacterable enemy = currentLocation.getEnemies().get(commands[1]);
        String objName = commands[3];       
 
        switch (commands[2]) {
            case "POUR": {
                enemy.pour(objName);
                break;
            }
            default: {
                System.err.printf("I don't have the ability to do %s.\n", objName);
                break;
            }
        }
    }

    public boolean isValidMove(String direction) {
        return currentLocation.getEdges().containsKey(direction);
    }
    
    public void move(String direction) {
    	// This method will move the player in the specified the direction.
    	
        Location newLocation = currentLocation.getEdges().get(direction);
        if (newLocation.isExit()) {
            if (this.items.get(Item.KEY.toString()) != null) {
                System.out.println("You won.");
                System.exit(0);
            } else {
                System.err.println("You need the key to unlock.");
                return;
            }
        }

        this.currentLocation.togglePlayerHere();
        this.currentLocation = newLocation;
        this.currentLocation.togglePlayerHere();
        
        System.out.printf("You have now entered the %s.\n", currentLocation.getName());
    }

    public void causeDamage(double damage) {
        System.out.printf("Player has taken %.2f damage.\n", damage);
        this.health -= damage;
        
        if (this.health < 0) {
            System.err.println("You died.");
            System.exit(0);
        }
    }
}
