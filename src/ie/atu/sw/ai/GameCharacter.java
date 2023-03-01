package ie.atu.sw.ai;

public abstract class GameCharacter implements Runnable {
    private Location location;
    private String name;
    private ConsoleColour consoleColour;

    private double health = 100;
    private boolean alive = true;
    
    public GameCharacter(Location location, String name, ConsoleColour consoleColour) {
        this.location = location;
        this.name = name;
        this.consoleColour = consoleColour;
    }
    
    private void moveOn() {
    	String name = this.name.toUpperCase();
        this.location.getEnemies().remove(this.toString());
        
        if (!alive) return;
        do {
    	    this.location = this.location.getRandomEdge();
        
       	    if (this.location.getEnemies().get(name) != null)
                continue;
            
            this.location.getEnemies().put(name, (GameCharacterable) this);
        } while(false);
    }

    public void run() {
        while (this.alive) {
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
    
    public boolean isAlive() {
    	return this.alive;
    }

    public void causeDamage(double damage, Player opponent) {
        System.out.printf("%s has taken %.2f damage.\n", this.getName(), damage);
        this.health -= damage;

        if (this.health <= 0) {
            System.out.printf("%s has been killed.\n", this.getName());
            this.location.getEnemies().remove(this.toString());
            this.location = null;
            this.alive = false;

            System.out.printf("%s dropped a key.\n", this.getName());
            opponent.giveItem(Item.KEY);
        }
    }
    
    public void pour(String objName) {
    	System.err.printf("I don't have any %s.\n", objName);
    }
}
