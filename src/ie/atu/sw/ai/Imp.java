package ie.atu.sw.ai;

import jhealy.aicme4j.net.Output;

public class Imp extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/imp.dat";

    private static final String COLOUR_NAME = ConsoleColour.BLUE + "Imp" + ConsoleColour.RESET;
    private static final String NAME = "Imp";

    private static int hiddenLayerSizes[] = {12};

    private static double expected[][] = {
        {0, 0, 0, 1},
        {0, 0, 1, 0},
        {0, 1, 0, 0},
        {1, 0, 0, 0}
    };
    
    public Imp(Location location) {
        super(location, NAME, COLOUR_NAME, hiddenLayerSizes);
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

        this.causeDamage(weapon.getAttackPoints());

        int index = 0;
		try {
			index = (int) this.getNeuralNetwork().process(input, Output.LABEL_INDEX);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double damage = attackValues[index];
    }
}
