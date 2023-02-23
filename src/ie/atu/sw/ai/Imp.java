package ie.atu.sw.ai;

import jhealy.aicme4j.NetworkBuilderFactory;
import jhealy.aicme4j.net.*;

public class Imp extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/imp.dat";

    private static final String NAME = "Imp";
    private static NeuralNetwork nn;

    private static double expected[][] = {
        {0, 0, 0, 1},
        {0, 0, 1, 0},
        {0, 1, 0, 0},
        {1, 0, 0, 0}
    };
    
    public static void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        nn = Aicme4jUtils.load(NN_PATH);
        if (nn != null && !forceNNRebuild) return;
        
        var data = getValidationData();
        
        nn = NetworkBuilderFactory.getInstance().newNetworkBuilder()
                .inputLayer("Input", data[0].length)
                .hiddenLayer("Hidden0", Activation.RELU, 10)
                .outputLayer("Output", Activation.LINEAR, expected[0].length)
                .train(data, expected, 0.0001, 0.95, 100000, 0.001, Loss.CEE)
                .save(NN_PATH)
                .build();
    }
    
    public Imp(Location location) {
        super(location, NAME, ConsoleColour.BLUE);
    }

    public void fight(Weapon weapon, Player opponent) {
        double attackTier[] = {12.5, 25, 37.5, 50};    

        double input[] = {weapon.getAttackPoints(), weapon.getDefencePoints()};
        Aicme4jUtils.normalise(input, 0, 1);

        this.causeDamage(weapon.getAttackPoints(), opponent);

        int index = (int) this.process(nn, input, Output.LABEL_INDEX);
        opponent.causeDamage(attackTier[index]);
    }
}
