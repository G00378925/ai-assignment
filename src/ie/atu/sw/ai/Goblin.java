package ie.atu.sw.ai;

import jhealy.aicme4j.net.Output;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/goblin.dat";

    private static final String NAME = "Goblin";
    private static int[] hiddenLayerSizes = {12};

    private double expected[][];

    public Goblin(Location location) {
        super(location, NAME, ConsoleColour.GREEN, hiddenLayerSizes);

        this.expected = new double[Weapon.getWeapons().length][];
        for (int i = 0; i < Weapon.getWeapons().length; i++) {
            Weapon weapon = Weapon.getWeapons()[i];
            // Trolls response will be to the attack
            // and some extra to cover the defence that the player has
            double trollResponse = weapon.getAttackPoints() + weapon.getDefencePoints();
            trollResponse *= 0.75; // Goblins are 25% weaker than the player

            this.expected[i] = new double[1];
            this.expected[i][0] = trollResponse;
        }

        this.setTrainingData(this.getData(), expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);

        double input[] = {weapon.getAttackPoints(), weapon.getDefencePoints()};
        double trollResponse = this.process(input, Output.NUMERIC);

        opponent.causeDamage(trollResponse - weapon.getDefencePoints());
    }
}
