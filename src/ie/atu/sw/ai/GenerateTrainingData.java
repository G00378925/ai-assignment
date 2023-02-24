package ie.atu.sw.ai;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.function.BiFunction;

public class GenerateTrainingData {
	private static final String[] GC_NAMES = {
        "Goblin", "Imp", "Troll"
	};

    private static final BiFunction<Double, Double, Double> goblinAttack = 
        (attack, defence) -> ((attack + defence) * 0.75);

    private static final BiFunction<Double, Double, Double> impAttack = 
        (attack, defence) -> Math.floor((attack + defence) / 50);

    private static final BiFunction<Double, Boolean, Double> trollAttackPunch =
        (attack, sharp) -> (attack + (sharp ? (attack * 0.5) : 0)) * 0.5;
    private static final BiFunction<Double, Boolean, Double> trollAttackKick =
        (attack, sharp) -> attack * 0.65;
	
    private static void genGoblinTrainingData(PrintWriter pw) {
        pw.println("# Goblin Training Data");
        pw.println("# This is the training data for the Goblin.");
        pw.println("# Goblins determine their attack based on the attack and defence of a weapon.");
        pw.println("# Goblins are 25% weaker than the player, hence the 0.75.");
        pw.println();
        pw.println("# 1. Attack, 2. Defence, 3. Output");
        pw.println("# Output = (Attack + Defence) * 0.75");
        
    	for (double attack = 0; attack <= 100; attack += 10) {
            for (double defence = 0; defence <= 100; defence += 10) {
                double output = goblinAttack.apply(attack, defence);
                pw.printf("%.0f, %.0f, %.2f\n", attack, defence, output);
            }
        }
    }
    
    private static void genGoblinValidationData(PrintWriter pw) {
        pw.println("# Goblin Validation Data");

    	for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100, defence = Math.random() * 100;
            double output = goblinAttack.apply(attack, defence);
            pw.printf("%f, %f, %f\n", attack, defence, output);
        }
    }

    private static void genImpTrainingData(PrintWriter pw) {
        pw.println("# Imp Training Data");

        for (double attack = 0; attack < 100; attack += 10) {
            for (double defence = 0; defence <= 100; defence += 10) {
                double output = impAttack.apply(attack, defence);
                pw.printf("%.0f, %.0f, %.0f\n", attack, defence, output);
            }
        }
    }

    private static void genImpValidationData(PrintWriter pw) {
        pw.println("# Imp Validation Data");

        for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100, defence = Math.random() * 100;
            double output = impAttack.apply(attack, defence);
            pw.printf("%f, %f, %.0f\n", attack, defence, output);
        }
    }

    private static void genTrollTrainingData(PrintWriter pw) {
        pw.println("# Troll Training Data");

        for (double attack = 0; attack <= 100; attack += 5) {
            for (int sharp = 0; sharp <= 1; sharp++) {
                double resultPunch = trollAttackPunch.apply(attack, sharp == 1);
                double resultKick = trollAttackKick.apply(attack, sharp == 1);
                pw.printf("%.0f, %d, %.2f, %.2f\n", attack, sharp, resultPunch, resultKick);
            }
        }
    }

    private static void genTrollValidationData(PrintWriter pw) {
        pw.println("# Troll Validation Data");

        for (int i = 0; i < 100; i++) {
            double attack = Math.random() * 100;
            boolean sharp = Math.random() > 0.5;
            double resultPunch = trollAttackPunch.apply(attack, sharp);
            double resultKick = trollAttackKick.apply(attack, sharp);
            pw.printf("%f, %d, %.2f, %.2f\n", attack, sharp ? 1 : 0, resultPunch, resultKick);
        }
    }

    public static void generateTrainingData(String path) throws Exception {
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
            trainingOS.close();

            validationPW.close();
            validationOS.close();
    	}
    }
}