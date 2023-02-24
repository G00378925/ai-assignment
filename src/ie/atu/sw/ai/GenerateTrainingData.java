package ie.atu.sw.ai;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.function.BiFunction;

public class GenerateTrainingData {
	private static final String[] GC_NAMES = {
        "Goblin", "Imp", "Troll"
	};

    private static final BiFunction<Double, Double, Double> goblinAttack = 
        (attack, defence) -> (attack + defence) * 0.75;

    private static final BiFunction<Double, Double, Double> impAttack = 
        (attack, defence) -> Math.floor((attack + defence) / 50);

    private static final BiFunction<Double, Boolean, Double> trollAttackPunch =
        (attack, sharp) -> (attack + (sharp ? (attack * 0.5) : 0)) * 0.5;
    private static final BiFunction<Double, Boolean, Double> trollAttackKick =
        (attack, sharp) -> attack * 0.65;
	
    private static void genGoblinTrainingData(PrintWriter pw) {
        pw.println("# Goblin Training Data");
        
        // Trolls response will be to the attack
        // and some extra to cover the defence that the player has
    	for (int attack = 0; attack <= 100; attack += 10) {
            for (int defence = 0; defence <= 100; defence += 10) {
                double result = goblinAttack.apply((double) attack, (double) attack);
                pw.printf("%d, %d, %f\n", attack, defence, (attack + defence) * 0.75);
            }
        }
    }
    
    private static void genGoblinValidationData(PrintWriter pw) {
        pw.println("# Goblin Validation Data");

    	for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100, defence = Math.random() * 100;
            var result = goblinAttack.apply((double) attack, (double) defence);

            pw.printf("%f, %f, %f\n", attack, defence, result);
        }
    }

    private static void genImpTrainingData(PrintWriter pw) {
        pw.println("# Imp Training Data");

        for (int attack = 0; attack < 100; attack += 10) {
            for (int defence = 0; defence <= 100; defence += 10) {
                var result = impAttack.apply((double) attack, (double) defence);

                pw.printf("%d, %d, %.0f\n", attack, defence, result);
            }
        }
    }

    private static void genImpValidationData(PrintWriter pw) {
        pw.println("# Imp Validation Data");

        for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100, defence = Math.random() * 100;
            var result = impAttack.apply((double) attack, (double) defence);

            pw.printf("%f, %f, %.0f\n", attack, defence, result);
        }
    }

    private static void genTrollTrainingData(PrintWriter pw) {
        pw.println("# Troll Training Data");

        for (int attack = 0; attack <= 100; attack += 10) {
            for (int sharpI = 0; sharpI <= 1; sharpI++) {
                var sharp = sharpI == 1;
                var resultPunch = trollAttackPunch.apply((double) attack, sharp);
                var resultKick = trollAttackKick.apply((double) attack, sharp);

                pw.printf("%d, %d, %.0f, %.0f\n", attack, sharp ? 1 : 0, resultPunch, resultKick);
            }
        }
    }

    private static void genTrollValidationData(PrintWriter pw) {
        pw.println("# Troll Validation Data");

        for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100;
            var sharp = Math.random() > 0.5;
            var resultPunch = trollAttackPunch.apply(attack, sharp);
            var resultKick = trollAttackKick.apply(attack, sharp);
            
            pw.printf("%f, %d, %f, %f\n", attack, sharp ? 1 : 0, resultPunch, resultKick);
        }
    }

    public static void generateTrainingData(String path) throws FileNotFoundException {
    	for (int i = 0; i < GC_NAMES.length; i++) {
    		String gameCharacterName = GC_NAMES[i].toLowerCase();

            var trainingOS = new FileOutputStream(path + gameCharacterName + "_training.csv");
    		var trainingPW = new PrintWriter(trainingOS);

            var validationOS = new FileOutputStream(path + gameCharacterName + "_validation.csv");
            var validationPW = new PrintWriter(validationOS);
    		
    		switch (i) {
    		    case 0: {
    		    	genGoblinTrainingData(trainingPW);
                    genGoblinValidationData(validationPW);
                    break;
    		    }
                case 1: {
                    genImpTrainingData(trainingPW);
                    genImpValidationData(validationPW);
                    break;
                }
                case 2: {
                    genTrollTrainingData(trainingPW);
                    genTrollValidationData(validationPW);
                    break;
                }
    		}

            trainingPW.close();
            validationPW.close();
    	}
    }
}