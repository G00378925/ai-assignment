package ie.atu.sw.ai;

import java.util.*;

public class Menu {    
    private final Scanner console = new Scanner(System.in);

    public static void loadNeuralNetworks(boolean forceNNRebuild) {
        try {
            Goblin.loadNeuralNetwork(forceNNRebuild);
            Imp.loadNeuralNetwork(forceNNRebuild);
            Troll.loadNeuralNetwork(forceNNRebuild);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadFuzzyLogic() {
        Dragon.loadFuzzyLogic();
        Orc.loadFuzzyLogic();
    }
    
    public void validateNeuralNetworks() {
        Goblin.validate();
        Imp.validate();
        Troll.validate();
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

    private void train(String characterName) throws Exception {
        switch (characterName) {
            case "GOBLIN": {
                Goblin.loadNeuralNetwork(true);
                break;
            }
            case "IMP": {
                Imp.loadNeuralNetwork(true);
                break;
            }
            case "TROLL": {
                Troll.loadNeuralNetwork(true);
                break;
            }
        }
    }

    private void validate(String characterName) {
        switch (characterName) {
            case "GOBLIN": {
                Goblin.validate();
                break;
            }
            case "IMP": {
                Imp.validate();
                break;
            }
            case "TROLL": {
                Troll.validate();
                break;
            }
        }
    }

    public void go(Player player) throws Exception {
        while (true) {
            // Print all the enemies at the current location
            // before the prompt character
            for (int i = 0; i < player.getEnemies().size(); i++) {
                GameCharacterable enemy = (new ArrayList<>(player.getEnemies())).get(i);
                System.out.print(enemy.getName());
                if (i < player.getEnemies().size() - 1)
                    System.out.print(", ");
            }
            System.out.print("> ");
            
            // Split the input by whitespace, this includes spaces and tabs
            String[] input = console.nextLine().strip().split("\\s+");
            for (int i = 0; i < input.length; i++)
                input[i] = input[i].toUpperCase();
            
            // Extract the first word in the command
            String command = input[0];
            if (command.length() == 0)
                continue;

            switch (command) {
                case "Q!": {
                    System.exit(0);
                }
                case "EAT": {
                    if (input.length < 2)
                        System.err.println("EAT must be followed with the items name.");
                    else
                        player.eat(input[1]);
                    break;
                }
                case "FIGHT":
                case "KILL": {
                    if (input.length < 4) {
                        System.err.println("FIGHT must be followed with the characters name.");
                        System.err.println("Correct syntax: FIGHT GOBLIN WITH SWORD");
                        break;
                    }

                    player.fight(input[1], input[3]);
                    break;
                }
                case "GET":
                case "TAKE": {
                    if (input.length < 2) {
                        System.err.println("GET must be followed with the items name.");
                        break;
                    }

                    player.get(input[1]);
                    break;
                }
                case "LOOK": {
                    player.look();
                    break;
                }
                case "TELL": {
                    if (input.length < 4)
                        System.err.println("Correct syntax: TELL ENEMY_NAME POUR ALE");
                    else
                        player.tell(input);
                    break;
                }
                case "INVENTORY": {
                    player.inventory();
                    break;
                }
                case "TRAIN": {
                    if (input.length < 2)
                        loadNeuralNetworks(true);
                    else
                        this.train(input[1]);
                    break;
                }
                case "VALIDATE": {
                    if (input.length < 2)
                        this.validateNeuralNetworks();
                    else
                        this.validate(input[1]);
                    break;
                }
                default: {
                    if (player.isValidMove(command)) {
                        player.move(command);
                        player.look();
                    } else {
                        System.err.println("\"" + command + "\" is not a valid move/command.");
                    }
                }
            }
        }
    }
}
