package ie.atu.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;

public class Orc extends GameCharacter implements GameCharacterable {
	private static final String FCL_PATH = "./resources/fuzzy/orc.fcl";

	private static FIS fis; // This is the FIS obj
	private double hoursTrained;

	public static void loadFuzzyLogic() {
		fis = FIS.load(FCL_PATH);
	}

	public Orc(Location location) {
		super(location, "Orc", ConsoleColour.RED);

		// Pick a random amount of hours trained.
		// Sugeno doesn't play well with numbers between these :(
		double[] possibleHoursTrained = { 0, 1000, 10_000 };
		this.hoursTrained = possibleHoursTrained[(int) Math.floor(Math.random() * 3)];
	}

	public void fight(Weapon weapon, Player opponent) {
		this.causeDamage(weapon.getAttackPoints(), opponent);
		if (!this.isAlive())
			return; // Check if the Orc is alive

		fis.setVariable("trainingLevel", this.hoursTrained);
		fis.setVariable("attack", weapon.getAttackPoints());
		fis.evaluate();

		// Fetch the attackResponse output from the FIS
		double damage = fis.getVariable("attackResponse").getValue();
		opponent.causeDamage(damage);
	}
}
