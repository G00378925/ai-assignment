package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/troll.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/troll_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/troll_validation.csv";
    private static final String NAME = "Troll";
    
    private static NeuralNetwork nn;
    
    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        System.out.println(ConsoleColour.YELLOW + "Training " + NAME + " . . ." + ConsoleColour.RESET);
        var trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 2);
        var data = trainingData[0];
        var expected = trainingData[1];
       
        Aicme4jUtils.normalise(data, -1, 1);
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
            .inputLayer("Input", data[0].length)
            .hiddenLayer("Hidden", Activation.TANH, 16)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.0001, 0.95, 100000, 0.001, Loss.CEE)
            .save(NN_PATH)
            .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        var trainingData = GameCharacterNN.loadCSVData(NN_VALIDATION_PATH, 2, 1);
        var data = trainingData[0];
        var expected = trainingData[1];
        Aicme4jUtils.normalise(data, -1, 1);
        
        validate(nn, data, expected, 1);
    }

    public Troll(Location location) {
        super(location, NAME, ConsoleColour.BLUE_BRIGHT);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {
    		weapon.getAttackPoints(), weapon.isSharp() ? 1 : 0
    	};
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        double result[] = process(nn, getWeaponInput(weapon), Output.NUMERIC);
        double punch = result[0], kick = result[1];
        
        opponent.causeDamage(punch);
        opponent.causeDamage(kick);
    }
}
