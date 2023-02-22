package ie.atu.sw.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Player {
    private Location currentLocation;
    private Item item;
    private Weapon weapon = Weapon.SWORD;

    private double health = 100;

    public Player(Location currentLocation, double health) {
        this.currentLocation = currentLocation;
        this.health = health;
    }
    
    public void setItem(Item item) {
    	if (this.item == Item.KEY) return;
    	this.item = item;
    }

    public Collection<GameCharacterable> getEnemies() {
    	return Collections.list(currentLocation.getEnemies().elements());
    }

    public double look() {
        System.out.println(currentLocation.getDescription());
        
        System.out.print("There is ");
        if (this.getEnemies().size() != 0) System.out.print("a "); 
        for (int i = 0; i < this.getEnemies().size(); i++) {
        	GameCharacterable enemy = (new ArrayList<>(this.getEnemies())).get(i);
        	System.out.print(enemy.getName());
        	if (i < this.getEnemies().size() - 1) System.out.print(", ");
        }
        if (this.getEnemies().size() == 0) System.out.print("nobody"); 
        System.out.print(" here.\n");
        
        return 0;
    }

    public void get(String itemName) {
        Item item = currentLocation.getItems().get(itemName);
        
        if (item != null) {
        	currentLocation.getItems().remove(itemName);
        	setItem(item);
        }
    }

    public void fight(String enemyName) {
    	GameCharacterable enemy = currentLocation.getEnemies().get(enemyName);

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
        System.out.println("You eat something.");
    }

    public double tell(String enemyName) {
    	GameCharacterable enemy = currentLocation.getEnemies().get(enemyName);
    	
    	if (enemy != null) {
            System.out.printf("You told the %s that you are looking for a key\n", enemy.toString());
    	} else {
    		System.err.println("There is nobody there");
    	}
        
        return 0;
    }

    public boolean isValidMove(String direction) {
        return currentLocation.getEdges().containsKey(direction);
    }
    
    public void move(String direction) {
    	Location newLocation = currentLocation.getEdges().get(direction);
    	if (newLocation.isExit()) {
            if (this.item == Item.KEY) {
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
    }
}