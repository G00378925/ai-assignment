package ie.atu.sw.ai;

import java.util.*;
import java.util.concurrent.*;

public class Runner {
    private static final int MAX_DEPTH = 2, MAX_NUM_OF_CHARACTERS = 40;
    private static final double INITIAL_PLAYER_HEALTH = 100.0;

    private Collection<GameCharacterable> characters;
    private Collection<Location> locations;
    
    private ExecutorService pool;
    
    public Runner() {
        this.characters = new ArrayList<GameCharacterable>();
        this.locations = new ArrayList<Location>();
        
        this.pool = Executors.newFixedThreadPool(MAX_NUM_OF_CHARACTERS);
    }

    private void spawnCharacters() throws Exception {
        // Pick a random character to spawn in and add it to the characters array.
        for (int i = 0; i < MAX_NUM_OF_CHARACTERS; i++) {
            do {
                int randomLocationIndex = new Random().nextInt(this.locations.size());
                Location randomLocation = (Location) this.locations.toArray()[randomLocationIndex];

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
    	GenerateTrainingData.generateTrainingData("./resources/neural/");
        Menu.loadNeuralNetworks(false);

        Location location = Location.setupLocationGraph(MAX_DEPTH, this.locations);
        location.togglePlayerHere();

        this.spawnCharacters();

        Player player = new Player(location, INITIAL_PLAYER_HEALTH);
        (new Menu()).showMenuHeader().go(player);
    }
    
    public static void main(String[] args) throws Exception {
        (new Runner()).go(args);
    }
}
