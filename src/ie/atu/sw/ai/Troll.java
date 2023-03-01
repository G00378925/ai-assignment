package ie.atu.sw.ai;

import java.util.function.BiFunction;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/troll.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/troll_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/troll_validation.csv";
    
    private static NeuralNetwork nn;
    
    private static final BiFunction<Double, Double, Double> denormalise =
            (trollResponse, maxValue) -> (((trollResponse + 1) / 2) * maxValue);
    
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
            .hiddenLayer("Hidden0", Activation.RELU, 6)
            .hiddenLayer("Hidden1", Activation.TANH, 4)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.001, 0.95, 1_000_00, 0.001, Loss.MSE)
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
        
        validate(nn, data, expected, 0.05);
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
        
        double weaponInput[] = getWeaponInput(weapon);
        // Normalise the weapon values between -1 and 1
        weaponInput[0] = ((weaponInput[0] / 100) * 2) - 1;
        weaponInput[1] = ((weaponInput[1] * 2)) - 1;
        
        double result[] = process(nn, weaponInput, Output.NUMERIC);
        double trollPunch = denormalise.apply(result[0], 30.0),
        		trollKick = denormalise.apply(result[1], 37.5);

        opponent.causeDamage(trollPunch);
        opponent.causeDamage(trollKick);
    }
}
