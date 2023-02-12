package ie.atu.sw.ai;

public class Goblin extends GameCharacterNN implements GameCharacterable {
    // Neural Network
    private static String NN_PATH = "./resources/neural/goblin.dat";

    public Goblin(Location location) {
        super(location);

        double data[][] = {
        	{0, 0, 0}
        };

        double expected[][] = {
        	{0}
        };

        this.setTrainingData(data, expected);
    }

    public void loadNeuralNetwork(boolean forceNNRebuild) throws Exception {
        this.loadNeuralNetwork(NN_PATH, forceNNRebuild);
    } 
    
    public float fight(Weapon weapon, Player opponent) {
		return 0;
    }

    public String toString() {
        return ConsoleColour.GREEN + "Goblin" + ConsoleColour.RESET;
    }
}
