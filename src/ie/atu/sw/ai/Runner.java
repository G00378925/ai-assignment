package ie.atu.sw.ai;

import java.util.*;
import java.util.concurrent.*;

public class Runner {
    private static final int MAX_DEPTH = 2, MAX_NUM_OF_CHARACTERS = 40;
    private static final double INITIAL_PLAYER_HEALTH = 100.0;
    
    // Force retrain on game start, is not needed as
    // these are cached in the ./resources/neural directory
    private static final boolean FORCE_RETRAIN_ON_START = true;

    private Collection<GameCharacterable> characters =
            new ArrayList<GameCharacterable>(); // All enemies in the game
    private Collection<Location> locations =
            new ArrayList<Location>();// All locations in the game
    
    private ExecutorService pool = // Pool of threads
            Executors.newFixedThreadPool(MAX_NUM_OF_CHARACTERS);
   
    private void spawnCharacters() throws Exception {
        // Pick a random character to spawn in and add it to the characters array.
        for (int i = 0; i < MAX_NUM_OF_CHARACTERS; i++) {
            do {
                int randomLocationIndex = new Random().nextInt(this.locations.size());
                Location randomLocation = (Location) this.locations.toArray()[randomLocationIndex];
                
                // Fetch random character instance
                GameCharacterable character = switch (new Random().nextInt(3)) {
                    case 0 -> new Goblin(randomLocation);
                    case 1 -> new Imp(randomLocation);
                    case 2 -> new Troll(randomLocation);
                    default -> null;
                };
                
                if (randomLocation.getEnemies().get(character.toString()) != null)
                    continue;
        
                characters.add(character);
                randomLocation.getEnemies().put(character.toString(), character);
            } while (false);
        }

        for (GameCharacterable character : this.characters) {
             // Spawn goblin and Imp threads and put into pool
            pool.submit((GameCharacter) character);
        }
    }

    private void go(String[] args) throws Exception {
        // Generate the training data and load in the neural networks.
        GenerateTrainingData.generateTrainingData("./resources/neural/");
        Menu.loadNeuralNetworks(FORCE_RETRAIN_ON_START);
        
        // Generate the location graph, add player to middle of the graph
        Location location = Location.setupLocationGraph(MAX_DEPTH, this.locations);
        location.togglePlayerHere();

        this.spawnCharacters(); // Spawn the characters
        
        // Spawn player character and pass it to the menu
        Player player = new Player(location, INITIAL_PLAYER_HEALTH);
        (new Menu()).showMenuHeader().go(player);
    }
    
    public static void main(String[] args) throws Exception {
        (new Runner()).go(args);
    }
}
