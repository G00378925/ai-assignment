package ie.atu.sw.ai;

import java.util.function.Function;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/troll.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/troll_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/troll_validation.csv";
    
    private static NeuralNetwork nn;
    
    private static final Function<Double, Double> denormalise =
            (trollResponse) -> ((trollResponse + 1) / 2) * 65;
    
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
            .hiddenLayer("Hidden", Activation.RELU, 6)
            .hiddenLayer("Hidden", Activation.TANH, 4)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.001, 0.99, 10000, 0.001, Loss.MSE)
            .save(NN_PATH)
            .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        System.out.println(ConsoleColour.PURPLE + "Validating Troll . . ." + ConsoleColour.RESET);

        double[][][] trainingData = GameCharacterNN.loadCSVData(NN_VALIDATION_PATH, 2, 2);
        double[][] data = trainingData[0], expected = trainingData[1];
        
        Aicme4jUtils.normalise(data, -1, 1);
        Aicme4jUtils.normalise(expected, -1, 1);
        
        validate(nn, data, expected, 0.1);
    }

    public Troll(Location location) {
        super(location, "Troll", ConsoleColour.BLUE_BRIGHT);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {weapon.getAttackPoints(), weapon.isSharp() ? 1 : 0};
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
        if (!this.isAlive()) return;
        
        double result[] = process(nn, getWeaponInput(weapon), Output.NUMERIC);
        double trollPunch = denormalise.apply(result[0]), trollKick = denormalise.apply(result[1]);
        
        opponent.causeDamage(trollPunch);
        opponent.causeDamage(trollKick);
    }
}
