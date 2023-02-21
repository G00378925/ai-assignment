package ie.atu.sw.ai;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/goblin.dat";

    private static final String COLOUR_NAME = ConsoleColour.GREEN + "Goblin" + ConsoleColour.RESET;
    private static final String NAME = "Goblin";
    private static int[] hiddenLayerSizes = {12};

    private static double expected[][] = {
        {0}
    };

    public Goblin(Location location) {
        super(location, NAME, COLOUR_NAME, hiddenLayerSizes);
        this.setTrainingData(this.getData(), expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    }
    
    public void fight(Weapon weapon, Player opponent) {
        this.causeDamage(weapon.getAttackPoints());
    }
}
