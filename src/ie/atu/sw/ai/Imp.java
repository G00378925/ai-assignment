package ie.atu.sw.ai;

public class Imp extends GameCharacterNN implements GameCharacterable {
    // Neural Network
    private static String NN_PATH = "./resources/neural/imp.dat";

    public Imp(Location location) {
        super(location);

        double data[][] = {
            {0.25}, {0.5},
            {0.75}, {1}
        };

        double expected[][] = {
            {0, 0, 0, 1},
            {0, 0, 1, 0},
            {0, 1, 0, 0},
            {1, 0, 0, 0}
        };

        this.setTrainingData(data, expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    }

    public float fight(Weapon weapon, Player opponent) {
        float damage = 0;
        return damage;
    }

    public String toString() {
        return ConsoleColour.BLUE + "Imp" + ConsoleColour.RESET;
    }
}
