package ie.atu.sw.ai;

public class Troll extends GameCharacterNN implements GameCharacterable {
    private static String NN_PATH = "./resources/neural/troll.dat";

    private static final String COLOUR_NAME = ConsoleColour.BLUE_BRIGHT + "Troll" + ConsoleColour.RESET;
    private static final String NAME = "Troll";
    private static int[] hiddenLayerSizes = {12, 6};

    private static double expected[][] = {
        {0, 0},
        {0, 0}
    };

    public Troll(Location location) {
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
