package ie.atu.sw.ai;

import java.util.Scanner;

public class Menu {
    private static final Scanner console = new Scanner(System.in);

    public Menu showMenuHeader() {
        System.out.println(ConsoleColour.RED);
        System.out.println("************************************************************");
        System.out.println("*      ATU - Dept. Computer Science & Applied Physics      *");
        System.out.println("*                                                          *");
        System.out.println("*                   AI Text Adventure Game                 *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.println("Enter Text (Type Q! to Quit>");
        System.out.println();
        return this;
    }

    public void go(Player player) {
        while (true) {
            // Use a Scanner or something similar to read in text
            String[] input = console.nextLine().split(" ");

            /*
             * The locations can be created as an instance of a class Location or something
             * similar. Items can be added to a Collection associated with each location. The
             * game characters can keep an inventory using an instance of Collection.  
             */

            if (input.length == 0) {
                continue;
            }

            switch (input[0].toUpperCase()) {
                case "LOOK": {
                    player.look();
                    break;
                }
                case "GET": {
                    break;
                }
                case "FIGHT": {
                    player.fight();
                    break;
                }
                case "EAT": {
                    player.eat();
                    break;
                }
                case "TELL": {
                	break;
                }
                case "Q!": {
                    System.exit(0);
                }
            };
            // player.setHealth(player.getHealth() - damage);
        }
    }
}