package ie.atu.sw.ai;

import jhealy.aicme4j.net.*;

public abstract class GameCharacter implements Runnable {
    private Location location;
    private String name, colourName;
    private ConsoleColour consoleColour;

    private double health = 100;
    
    public GameCharacter(Location location, String name, ConsoleColour consoleColour) {
        this.location = location;
        this.name = name;
        this.consoleColour = consoleColour;
    }
    
    private void moveOn() {
    	String name = this.name.toUpperCase();
        this.location.getEnemies().remove(this.toString());
        
        do {
    	    this.location = this.location.getRandomEdge();
        
       	    if (this.location.getEnemies().get(name) != null) {
                continue;
       	    }
            
            this.location.getEnemies().put(name, (GameCharacterable) this);
        } while(false);
    }

    public double[][] getData() {
        Weapon[] weapons = Weapon.getWeapons();

        double[][] data = new double[weapons.length][2];
        for (int i = 0; i < weapons.length; i++) {
            data[i][0] = weapons[i].getAttackPoints();
            data[i][1] = weapons[i].getDefencePoints();
        }

        Aicme4jUtils.normalise(data, 0, 1);
        return data;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 10_000));
            		    
                // If player is around loiter
            	if (!this.location.isPlayerHere())
                    this.moveOn();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return consoleColour + this.name + ConsoleColour.RESET;
    }
    
    public String toString() {
        return this.name.toUpperCase();
    }
    
    public double getHealth() {
    	return this.health;
    }

    public void causeDamage(double damage, Player opponent) {
        System.out.printf("%s has taken %.2f damage.\n", this.name, damage);
        this.health -= damage;

        if (this.health <= 0) {
            System.out.printf("%s has been killed.\n", this.name);
            this.location.getEnemies().remove(this.toString());
            this.location = null;

            System.out.printf("%s dropped a key.\n", this.name);
            opponent.setItem(Item.KEY);
        }
    }

    public boolean equals(GameCharacter obj) {
        return this.name.equals(obj.toString());
    }

    public abstract String getAIType();
}