package ie.atu.sw.ai;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

import jhealy.aicme4j.net.*;

// Extend this, and you can be an NN enemy too!
public abstract class GameCharacterNN extends GameCharacter {
    public static NeuralNetwork loadNN(String path) {
        // Try and load the neural network from a file
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
        // This method loads the CSV data from a file
        // Returns two values, with input values in index 0
        // And output values in index 1

        double[][][] output = new double[2][][];
        ArrayList<double[]> inputAL = new ArrayList<double[]>(), outputAL = new ArrayList<double[]>();

        try {
            FileReader fileReader = new FileReader(csvPath);
            Scanner scanner = new Scanner(fileReader);

            // Read in each line in the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignore lines that are empty or begin with a # for a comment
                if (line.isEmpty() || line.charAt(0) == '#') continue;

                double[] values = Arrays.stream(line.split("#")[0].split(","))
                    .mapToDouble(Double::parseDouble).toArray();

                double[] _input = new double[inputSize];
                double[] _output = new double[outputSize];

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
            // If label index, return a single value array.
            if (outputType == Output.LABEL_INDEX)
                return new double[] {nn.process(input, outputType)};

            // This allows me to extract the output layer of the NN
            nn.process(input, outputType);
            return nn.getOutputLayer();
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void validate(NeuralNetwork nn, double[][] data, double[][] expected, double tolerance) {
        int errorCount = 0, sampleCount = expected.length * expected[0].length;
        
        for (int i = 0; i < data.length; i++) { // Iterate through the validation data
            double[] input = data[i]; // Pass input into the NN
            double[] output = process(nn, input, Output.NUMERIC);
            
            System.out.print("Input: ");
            for (double num : input)
                System.out.printf("%.2f, ", num);
            
            System.out.print("Output: ");
            for (int j = 0; j < output.length; j++) {
                // Print the colour, indicating a PASS or FAIL
                if (Math.abs(output[j] - expected[i][j]) < tolerance) {
                    System.out.print(ConsoleColour.GREEN);
                } else {
                    System.out.print(ConsoleColour.RED);
                    errorCount++; // Increment the error count, the goal is to keep this low
                }
                
                if (output.length > 1) System.out.printf("Index %d: ", j);
                System.out.printf("%.2f == %.2f ", output[j], expected[i][j]);
            }
            
            System.out.println(ConsoleColour.RESET);
        }
        
        System.out.printf("Error count: %d out of %d\n", errorCount, sampleCount);
    }
}
