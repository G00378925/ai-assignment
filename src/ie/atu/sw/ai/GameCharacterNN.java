package ie.atu.sw.ai;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

import jhealy.aicme4j.net.*;

public abstract class GameCharacterNN extends GameCharacter {
    public static NeuralNetwork loadNN(String path) {
    	try {
    		return Aicme4jUtils.load(path);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    public GameCharacterNN(Location location, String name, ConsoleColour consoleColour) {
        super(location, name, consoleColour);
    }
    
    public static double[][][] loadCSVData(String csvPath, int inputSize, int outputSize) {
    	var output = new double[2][][];

        var inputAL = new ArrayList<double[]>();
        var outputAL = new ArrayList<double[]>();

        try {
            var fileReader = new FileReader(csvPath);
            var scanner = new Scanner(fileReader);

            while (scanner.hasNextLine()) {
                var line = scanner.nextLine().trim();

                if (line.isEmpty() || line.charAt(0) == '#') continue;

                var values = Arrays.stream(line.split("#")[0].split(","))
                    .mapToDouble(Double::parseDouble).toArray();

                var _input = new double[inputSize];
                var _output = new double[outputSize];

                for (int i = 0; i < inputSize; i++) _input[i] = values[i];
                for (int i = 0; i < outputSize; i++) _output[i] = values[inputSize + i];

                inputAL.add(_input);
                outputAL.add(_output);
            }

            scanner.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        output[0] = inputAL.toArray(new double[inputAL.size()][]);
        output[1] = outputAL.toArray(new double[outputAL.size()][]);
    	return output;
    }

    public static double[] process(NeuralNetwork nn, double[] input, Output outputType) {
        try {
        	if (outputType == Output.LABEL_INDEX) {
                return new double[] {nn.process(input, outputType)};
        	}
            nn.process(input, outputType);
            return nn.getOutputLayer();
        } catch (Exception e) {
    		return null;
        }
    }
    
    public static void validate(NeuralNetwork nn, double[][] data, double[][] expected, double tolerance) {
    	boolean passAllTests = true;
        for (int i = 0; i < data.length; i++) {
            double[] input = data[i];
            double output[] = process(nn, input, Output.NUMERIC);
            
            for (int j = 0; j < output.length; j++) {
            	if (output.length > 1) System.out.printf("Output Index %d: ", j);
            	
                if (Math.abs(output[j] - expected[i][0]) > tolerance) {
                    System.out.println(ConsoleColour.RED + "%.2f == %.2f".formatted(output[j], expected[i][0]));
                    passAllTests = false;
                } else {
                	System.out.println(ConsoleColour.GREEN + "%.2f == %.2f".formatted(output[j], expected[i][0]));
                }
                System.out.print(ConsoleColour.RESET);
            }
        }
    	
        if (passAllTests) System.out.println("All validation tests pass");
    }
}
