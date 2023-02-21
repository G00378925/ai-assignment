package ie.atu.sw.ai;

import jhealy.aicme4j.net.*;

public abstract class GameCharacter implements Runnable {
    private Location location;
    private String name, colourName;

    private float health = 100;
    
    public GameCharacter(Location location, String name, String colourName) {
        this.location = location;
        this.name = name.toUpperCase();
        this.colourName = colourName;
    }
    
    private void moveOn() {
        this.location.getEnemies().remove(this.toString());
        
        do {
    	    this.location = this.location.getRandomEdge();
        
       	    if (this.location.getEnemies().get(this.name) != null) {
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
            	Thread.sleep(10_000);
            		    
                // If player is around loiter
            	if (!this.location.isPlayerHere())
                    this.moveOn();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return this.colourName;
    }
    
    public String toString() {
        return this.name;
    }

    public void causeDamage(float damage) {
        System.out.printf("%s has taken %d damage.\n", this.name, damage);
        this.health -= damage;
    }

    public boolean equals(GameCharacter obj) {
        return this.name.equals(obj.toString());
    }

    public abstract String getAIType();
}