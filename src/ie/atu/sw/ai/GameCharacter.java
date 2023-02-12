package ie.atu.sw.ai;

public abstract class GameCharacter implements Runnable {
    private Location location;

    public GameCharacter(Location location) {
		this.location = location;
	}

	public void run() {
		while (true) {
			
		}
	}

	public abstract String getAIType();
}