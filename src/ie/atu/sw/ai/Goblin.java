package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.Activation;
import jhealy.aicme4j.net.Aicme4jUtils;
import jhealy.aicme4j.net.Loss;
import jhealy.aicme4j.net.NeuralNetwork;
import jhealy.aicme4j.net.Output;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/goblin.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/goblin_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/goblin_validation.csv";
    private static final String NAME = "Goblin";

    private static NeuralNetwork nn;
    
    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        System.out.println(ConsoleColour.YELLOW + "Training " + NAME + " . . ." + ConsoleColour.RESET);
        var trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 1);
        var data = trainingData[0];
        var expected = trainingData[1];
        
        Aicme4jUtils.normalise(data, 0, 1);

        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
            .inputLayer("Input", data[0].length)
            .hiddenLayer("Hidden", Activation.RELU, 16)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.0001, 0.95, 100000, 0.00001, Loss.MSE)
            .save(NN_PATH)
            .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        var trainingData = GameCharacterNN.loadCSVData(NN_VALIDATION_PATH, 2, 1);
        Aicme4jUtils.normalise(trainingData[0], 0, 1);
        validate(nn, trainingData[0], trainingData[1], 2);
    }
    
    public Goblin(Location location) {
        super(location, NAME, ConsoleColour.GREEN);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {
    		weapon.getAttackPoints(), weapon.getDefencePoints()
    	};
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        if (this.getHealth() > 0) {
            var trollResponse = process(nn, getWeaponInput(weapon), Output.NUMERIC)[0];
            System.out.println("Troll response: " + trollResponse);
            trollResponse -= weapon.getDefencePoints();
            trollResponse = trollResponse < 0 ? 0 : trollResponse;
            
            opponent.causeDamage(trollResponse);
        }
    }
}
