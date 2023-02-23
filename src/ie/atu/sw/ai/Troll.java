package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/troll.dat";
    private static String NN_VALIDATION_PATH = "./resources/neural/troll.dat";
    private static final String NAME = "Troll";
    
    private static NeuralNetwork nn;

    private static double[][] expected = {
        {0, 0}, {0, 0},
        {0, 0}, {0, 0},
        {0, 0}, {0, 0},
        {0, 0}, {0, 0},
        {0, 0}, {0, 0}
    };
    
    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = loadNN(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        double data[][] = getValidationData();
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
            .inputLayer("Input", data[0].length)
            .hiddenLayer("Hidden0", Activation.RELU, 10)
            .outputLayer("Output", Activation.LINEAR, expected[0].length)
            .train(data, expected, 0.0001, 0.95, 100000, 0.001, Loss.SSE)
            .save(NN_PATH)
            .build();
    }

    public Troll(Location location) {
        super(location, NAME, ConsoleColour.BLUE_BRIGHT);
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints(), opponent);
    }
}
