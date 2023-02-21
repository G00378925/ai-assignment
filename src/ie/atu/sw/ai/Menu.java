package ie.atu.sw.ai;

import java.util.*;

public class Menu {
    private final Scanner console = new Scanner(System.in);

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
                	if (input.length < 2) {
                		System.err.println("GET must be followed with the items name.");
                		break;
                	}
                	
                	player.get(input[1]);
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
                	if (input.length < 2) {
                		System.err.println("EAT must be followed with the items name.");
                		break;
                	}
                	
                    player.eat(input[1]);
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