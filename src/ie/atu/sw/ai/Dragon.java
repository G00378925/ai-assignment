package ie.atu.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;

public class Dragon extends GameCharacter implements GameCharacterable {
	private static final String FCL_PATH = "./resources/fuzzy/dragon.fcl";

	private static FIS fis;

	private int bloodAlcoholLevel;

	public static void loadFuzzyLogic() {
		fis = FIS.load(FCL_PATH);
	}

	public Dragon(Location location) {
		super(location, "Dragon", ConsoleColour.RED);

		// Setting the initial BAC
		this.bloodAlcoholLevel = 0;
	}

	public void fight(Weapon weapon, Player opponent) {
		this.causeDamage(weapon.getAttackPoints(), opponent);
		if (!this.isAlive())
			return; // Check if the Dragon is alive

		// These are the inputs to the FIS
		fis.setVariable("attack", weapon.getAttackPoints());
		fis.setVariable("opponentHealth", opponent.getHealth());
		fis.setVariable("bloodAlcoholLevel", this.bloodAlcoholLevel);
		fis.evaluate();

		// Fetch the output attackResponse
		double damage = fis.getVariable("attackResponse").getValue();
		opponent.causeDamage(damage);
	}

	public void pour(String objName, Player opponent) {
		// If the BAC goes over 80 the Dragon will know to stop
		if (this.bloodAlcoholLevel > 80) {
			System.err.println(this.getName() + ": Stop thats enough!");
			return;
		}

		this.bloodAlcoholLevel += switch (objName) {
		case "ALE" -> 4;
		case "BRANDY" -> 10;
		case "WHISKEY" -> 14;
		default -> 0;
		};

		System.out.println(this.getName() + ": That was a nice glass of " + objName);
	}
}
