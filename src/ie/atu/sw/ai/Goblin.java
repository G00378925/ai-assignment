package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Aicme4jUtils;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/goblin.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/goblin.csv";
    private static final String NAME = "Goblin";

    private static double HIGHEST_POSSIBLE = (100 + 100) * 0.75;
    
    private static NeuralNetwork nn;
    
    private static double[][] generateExpected(double[][] data) {
    	double expected[][] = new double[data.length][];
        for (int i = 0; i < data.length; i++) {
            // Trolls response will be to the attack
            // and some extra to cover the defence that the player has
            double trollResponse = data[i][0] + data[i][1];
            trollResponse *= 0.75; // Goblins are 25% weaker than the player

            expected[i] = new double[] {trollResponse};
        }
        
        Aicme4jUtils.normalise(expected, 0, 1);
        return expected;
    }

    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        var trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 1);
        var data = trainingData[0];
        var expected = trainingData[1];
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
            .inputLayer("Input", data[0].length)
            .hiddenLayer("Hidden", Activation.RELU, 10)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.0001, 0.95, 100000, 0.001, Loss.SSE)
            .save(NN_PATH)
            .build();
        
        System.out.println(nn);
    }
    
    public Goblin(Location location) {
        super(location, NAME, ConsoleColour.GREEN);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	double[] nnInput = new double[] {
    		weapon.getAttackPoints(), weapon.getDefencePoints()
    	};
    	return nnInput;
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        if (this.getHealth() > 0) {
            var trollResponse = this.process(nn, getWeaponInput(weapon), Output.NUMERIC);
            System.out.println("Troll response: " + trollResponse);
            trollResponse -= weapon.getDefencePoints();
            trollResponse = trollResponse < 0 ? 0 : trollResponse;
            
            opponent.causeDamage(trollResponse);
        }
    }
}
