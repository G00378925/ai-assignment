package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/troll.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/troll_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/troll_validation.csv";
    
    private static NeuralNetwork nn;
    
    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        System.out.println(ConsoleColour.YELLOW + "Training Troll . . ." + ConsoleColour.RESET);
        double[][][] trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 2);
        double[][] data = trainingData[0], expected = trainingData[1];
        
        Aicme4jUtils.normalise(data, -1, 1);
        Aicme4jUtils.normalise(expected, -1, 1);
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
            .inputLayer("Input", data[0].length)
            .hiddenLayer("Hidden", Activation.RELU, 16)
            .hiddenLayer("Hidden", Activation.TANH, 8)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.001, 0.95, 100000, 0.001, Loss.MSE)
            .save(NN_PATH)
            .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        double[][][] trainingData = GameCharacterNN.loadCSVData(NN_VALIDATION_PATH, 2, 1);
        double[][] data = trainingData[0], expected = trainingData[1];
        
        Aicme4jUtils.normalise(data, -1, 1);
        Aicme4jUtils.normalise(expected, -1, 1);
        
        validate(nn, data, expected, 0.2);
    }

    public Troll(Location location) {
        super(location, "Troll", ConsoleColour.BLUE_BRIGHT);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {weapon.getAttackPoints(), weapon.isSharp() ? 1 : 0};
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        
        double result[] = process(nn, getWeaponInput(weapon), Output.NUMERIC);
        double punch = ((result[0] + 1) / 2) * 65, kick = ((result[1] + 1) / 2) * 65;
        
        opponent.causeDamage(punch);
        opponent.causeDamage(kick);
    }
}
