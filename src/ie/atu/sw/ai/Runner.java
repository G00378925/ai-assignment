package ie.atu.sw.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Runner {
    private static final int MAX_DEPTH = 3, MAX_NUM_OF_CHARACTERS = 2;

    private Collection<GameCharacterable> characters;
    private Collection<Location> locations;
    
    public Runner() {
    	this.characters = new ArrayList<GameCharacterable>();
    	this.locations = new ArrayList<Location>();
    }

    private void spawnCharacters(boolean forceNNRebuild) throws Exception {
        int randomLocationIndex = new Random().nextInt(this.locations.size());
        Location randomLocation = (Location) this.locations.toArray()[randomLocationIndex];

        for (int i = 0; i < MAX_NUM_OF_CHARACTERS; i++) {
            GameCharacterable character = switch (new Random().nextInt(3)) {
                case 0 -> new Goblin(randomLocation);
                case 1 -> new Imp(randomLocation);
                case 2 -> new Dragon(randomLocation);
                default -> null;
            };

            characters.add(character);
        }

        for (GameCharacterable character : this.characters) {
            if (character.getAIType() == "NN")
                ((GameCharacterNN) character).loadNeuralNetwork(forceNNRebuild);
                

            //    ((GameCharacterNN) character).loadNeuralNetwork(forceNNRebuild);
            //else if (character.getAIType() == "FL")
            //    ((GameCharacterFL) character).setFuzzyLogic(fl);
        }

        // Spawn goblin and Imp threads and put into pool
        ExecutorService pool = Executors.newFixedThreadPool(MAX_NUM_OF_CHARACTERS);
	    for (GameCharacterable character : this.characters) {
            pool.submit((GameCharacter) character);
        }
    }

    private void go(String[] args) throws Exception {
        boolean forceNNRebuild = false;

        for (String arg : args) {
            switch (arg) {
                case "--nn-rebuild": {
                    forceNNRebuild = true;
                    break;
                }
            }
        }

        Location location = Location.setupLocationGraph(0, MAX_DEPTH, this.locations);
        
        this.spawnCharacters(forceNNRebuild);

        Player player = new Player(location);
        (new Menu()).showMenuHeader().go(player);
    }
    
    public static void main(String[] args) throws Exception {
        (new Runner()).go(args);
    }
}