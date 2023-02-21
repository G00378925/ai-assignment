package ie.atu.sw.ai;

import java.util.*;

public class Menu {
    private static final Scanner console = new Scanner(System.in);
    private final Collection<Location> locations;

    public Menu(Collection<Location> locations) {
        this.locations = locations;
    }

    public Menu showMenuHeader() {
        System.out.println(ConsoleColour.RED);
        System.out.println("************************************************************");
        System.out.println("*      ATU - Dept. Computer Science & Applied Physics      *");
        System.out.println("*                                                          *");
        System.out.println("*                   AI Text Adventure Game                 *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.println("Enter Text (Type Q! to Quit>" + ConsoleColour.RESET);
        System.out.println();
        return this;
    }

    public void go(Player player) {
        while (true) {
            for (int i = 0; i < player.getEnemies().size(); i++) {
            	GameCharacterable enemy = (new ArrayList<>(player.getEnemies())).get(i);
            	System.out.print(enemy.getName());
            	if (i < player.getEnemies().size() - 1) System.out.print(", ");
            }
            System.out.print("> ");

            String[] input = console.nextLine().strip().split("\\s+");
            for (int i = 0; i < input.length; i++)
            	input[i] = input[i].toUpperCase();

            /*
             * The locations can be created as an instance of a class Location or something
             * similar. Items can be added to a Collection associated with each location. The
             * game characters can keep an inventory using an instance of Collection.  
             */
            String command = input[0];
            if (command.length() == 0) continue;
            
            switch (command) {
                case "Q!": {
                    System.exit(0);
                }
                case "LOOK": {
                    player.look();
                    break;
                }
                case "GET": {
                	player.get();
                	break;
                }
                case "FIGHT": {
                	if (input.length < 2) {
                		System.err.println("FIGHT must be followed with the characters name.");
                		break;
                	}
                	
                    player.fight(input[1]);
                    break;
                }
                case "EAT": {
                    player.eat();
                    break;
                }
                case "TELL": {
                	if (input.length < 2) {
                		// syntaxError(command);
                		break;
                	}
                	
                    player.tell(input[1]);
                    break;
                }
                default: {
                    if (player.isValidMove(command)) {
                        player.move(command);
                    } else {
                        System.err.println("\"" + command + "\" is not a valid move/command.");
                    }
                }
            };
        }
    }
}