package ie.atu.sw.ai;

public abstract class GameCharacter implements Runnable {
	private Location location; // Where the character currently is.
	private String name; // Name of the character.
	private ConsoleColour consoleColour; // Colour of the character.

	private double health = 100; // All game characters start with 100 health
	private boolean alive = true; // Other threads will check this value

	public GameCharacter(Location location, String name, ConsoleColour consoleColour) {
		this.location = location;
		this.name = name;
		this.consoleColour = consoleColour;
	}

	private void moveOn() {
		String name = this.name.toUpperCase();
		this.location.getEnemies().remove(this.toString());

		// If not alive return
		if (!alive)
			return;
		do {
			// Pick a random direction and go there
			this.location = this.location.getRandomEdge();

			// If an enemy of the same type is here, move on
			if (this.location.getEnemies().get(name) != null)
				continue;

			// The enemy will now make itself known
			this.location.getEnemies().put(name, (GameCharacterable) this);
		} while (false);
	}

	public void run() {
		while (this.alive) {
			try {
				// Characters will sleep for a random amount of time
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

	// Return the health of the enemy
	public double getHealth() {
		return this.health;
	}

	public boolean isAlive() {
		return this.alive;
	}

	public void causeDamage(double damage, Player opponent) {
		System.out.printf("%s has taken %.2f damage.\n", this.getName(), damage);
		this.health -= damage; // Apply damage values to the enemy

		if (this.health <= 0) {
			System.out.printf("%s has been killed.\n", this.getName());
			this.location.getEnemies().remove(this.toString());
			this.location = null;
			this.alive = false;

			System.out.printf("%s dropped a key.\n", this.getName());
			opponent.giveItem(Item.KEY); // On death give the player a key
		}
	}

	public void pour(String objName, Player opponent) {
		System.err.printf("I don't have any %s.\n", objName);
		System.err.println("But, I know the dragon might have some ALE.");
	}

	public String toString() {
		return this.name.toUpperCase();
	}
}
