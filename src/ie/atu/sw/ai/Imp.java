package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Imp extends GameCharacterNN implements GameCharacterable {
    private static final String NN_PATH = "./resources/neural/imp.dat";
    private static final String NN_TRAINING_PATH = "./resources/neural/imp_training.csv";
    private static final String NN_VALIDATION_PATH = "./resources/neural/imp_validation.csv";
    private static final String NAME = "Imp";

    private static NeuralNetwork nn;
    
    private static double[][] genOneHotVector(double[][] expected, int vectorSize) {
    	var oneHotVector = new double[expected.length][];
    	for (int i = 0; i < expected.length; i++) {
    		oneHotVector[i] = new double[vectorSize];
    		oneHotVector[i][(int) expected[i][0]] = 1;
    	}
    	return oneHotVector;
    }

    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        System.out.println(ConsoleColour.YELLOW + "Training " + NAME + " . . ." + ConsoleColour.RESET);
        var trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 1);
        var data = trainingData[0];
        var expected = genOneHotVector(trainingData[1], 4);
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
                .inputLayer("Input", data[0].length)
                .hiddenLayer("Hidden", Activation.SIGMOID, 10)
                .outputLayer("Output", Activation.SIGMOID, expected[0].length)
                .train(data, expected, 0.001, 0.95, 100000, 0.001, Loss.SSE)
                .save(NN_PATH)
                .build();
        
        System.out.println(nn);
    }
    
    public static void validate() {
        var trainingData = GameCharacterNN.loadCSVData(NN_TRAINING_PATH, 2, 1);
        var data = trainingData[0];
        var expected = trainingData[1];
        
        boolean passAllTests = true;
        for (int i = 0; i < data.length; i++) {
            double index = process(nn, data[i], Output.LABEL_INDEX);
            
            if ((int) index != expected[i][0]) {
            	System.err.printf("Input: %.2f %.2f, ", data[i][0], data[i][1]);
            	System.err.printf("Output: %.2f != %.2f\n", expected[i][0], index);
            	passAllTests = false;
            }
        }
    	
        if (passAllTests) System.out.println("All validation tests pass");
    }
    
    public Imp(Location location) {
        super(location, NAME, ConsoleColour.BLUE);
    }
    
    public double[] getWeaponInput(Weapon weapon) {
    	return new double[] {
    		weapon.getAttackPoints(), weapon.getDefencePoints()
    	};
    }

    public void fight(Weapon weapon, Player opponent) {
        double attackTier[] = {12.5, 25, 37.5, 50};    

        this.causeDamage(weapon.getAttackPoints(), opponent);

        int index = (int) process(nn, getWeaponInput(weapon), Output.LABEL_INDEX);
        opponent.causeDamage(attackTier[index]);
    }
}
