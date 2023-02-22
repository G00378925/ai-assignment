package ie.atu.sw.ai;

import jhealy.aicme4j.net.Aicme4jUtils;
import jhealy.aicme4j.net.Output;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/goblin.dat";

    private static final String NAME = "Goblin";
    private static int[] hiddenLayerSizes = {10};

    private double expected[][];
    private double maxExpected = 0;

    public Goblin(Location location) {
        super(location, NAME, ConsoleColour.GREEN, hiddenLayerSizes);

        this.expected = new double[Weapon.getWeapons().length][];
        for (int i = 0; i < Weapon.getWeapons().length; i++) {
            Weapon weapon = Weapon.getWeapons()[i];
            // Trolls response will be to the attack
            // and some extra to cover the defence that the player has
            double trollResponse = weapon.getAttackPoints() + weapon.getDefencePoints();
            trollResponse *= 0.75; // Goblins are 25% weaker than the player

            this.expected[i] = new double[] {trollResponse};
            
            if (expected[i][0] > this.maxExpected)
                this.maxExpected = expected[i][0] ;
        }
        

        Aicme4jUtils.normalise(this.expected, 0, 1);
        
        this.setTrainingData(this.getData(), expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        if (this.getHealth() > 0) {
            double input[] = {weapon.getAttackPoints(), weapon.getDefencePoints()};
            Aicme4jUtils.normalise(input, 0, 1);
            double trollResponse = this.process(input, Output.NUMERIC) * this.maxExpected;
            
            System.out.println("Response: " + trollResponse);
            opponent.causeDamage(trollResponse - weapon.getDefencePoints());
        }
    }
}
