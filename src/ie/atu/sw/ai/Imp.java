package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Imp extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/imp.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/imp_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/imp_validation.csv";

    private static NeuralNetwork nn;
    
    private static double[][] genOneHotVector(double[][] expected, int vectorSize) {
    	double[][] oneHotVector = new double[expected.length][];
    	for (int i = 0; i < expected.length; i++) {
    		oneHotVector[i] = new double[vectorSize];
    		oneHotVector[i][(int) expected[i][0]] = 1;
    	}
    	return oneHotVector;
    }

    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {    	
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        System.out.println(ConsoleColour.YELLOW + "Training Imp . . ." + ConsoleColour.RESET);
        double[][][] trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 1);
        double[][] data = trainingData[0], expected = genOneHotVector(trainingData[1], 4);
        Aicme4jUtils.normalise(data, -1, 1);
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
                .inputLayer("Input", data[0].length)
                .hiddenLayer("Hidden", Activation.LEAKY_RELU, 8)
                .outputLayer("Output", Activation.LINEAR, expected[0].length)
                .train(data, expected, 0.001, 0.95, 100000, 0.001, Loss.CEE)
                .save(NN_PATH)
                .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        double[][][] trainingData = GameCharacterNN.loadCSVData(NN_VALIDATION_PATH, 2, 1);
        double[][] data = trainingData[0], expected = trainingData[1];
        Aicme4jUtils.normalise(data, -1, 1);
        
        for (int i = 0; i < data.length; i++) {
            double index = process(nn, data[i], Output.LABEL_INDEX)[0];
            
            System.out.print(ConsoleColour.YELLOW);
            System.err.printf("Input: %.2f %.2f, ", data[i][0], data[i][1]);
            
            if (index == expected[i][0]) System.out.print(ConsoleColour.GREEN);
            else System.out.print(ConsoleColour.RED);
            
            System.err.printf("Output: %.2f == %.2f", expected[i][0], index);
            System.out.println(ConsoleColour.RESET);
        }
    }
    
    public Imp(Location location) {
        super(location, "Imp", ConsoleColour.BLUE);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {weapon.getAttackPoints(), weapon.getDefencePoints()};
    }

    public void fight(Weapon weapon, Player opponent) {
        double attackTier[] = {12.5, 25, 37.5, 50};    
        this.causeDamage(weapon.getAttackPoints(), opponent);

        int index = (int) process(nn, getWeaponInput(weapon), Output.LABEL_INDEX)[0];
        opponent.causeDamage(attackTier[index]);
    }
}
