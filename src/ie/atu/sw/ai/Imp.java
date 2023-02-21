package ie.atu.sw.ai;

import jhealy.aicme4j.net.Output;

public class Imp extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/imp.dat";

    private static final String NAME = "Imp";

    private static int hiddenLayerSizes[] = {12};

    private static double expected[][] = {
        {0, 0, 0, 1},
        {0, 0, 1, 0},
        {0, 1, 0, 0},
        {1, 0, 0, 0}
    };
    
    public Imp(Location location) {
        super(location, NAME, ConsoleColour.BLUE, hiddenLayerSizes);
        this.setTrainingData(this.getData(), expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    }

    public void fight(Weapon weapon, Player opponent) {
        double attackValues[] = {
            10, 20, 30, 40
        };    

        double input[] = {
            weapon.getAttackPoints(),
            weapon.getDefencePoints()
        };

        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        if (this.getHealth() <= 0) {
        	opponent.setItem(Item.KEY);
        }

        int index = (int) this.process(input, Output.LABEL_INDEX);
        double damage = attackValues[index];
    }
}
