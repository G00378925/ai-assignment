package ie.atu.sw.ai;

import java.nio.file.*;

import jhealy.aicme4j.*;
import jhealy.aicme4j.net.*;

public abstract class GameCharacterNN extends GameCharacter {
    private double[][] data, expected;
    private int[] hiddenLayerSizes;
    protected double learningRate = 0.01, momentum = 0.95, maxError = 0.00001;
    protected int epochs = 100000;

    private NeuralNetwork nn;
    
    public GameCharacterNN(Location location, String name, String colourName, int[] hiddenLayerSizes) {
        super(location, name, colourName);
        this.hiddenLayerSizes = hiddenLayerSizes;
    }

    public void setTrainingData(double[][] data, double[][] expected) {
        this.data = data;
        this.expected = expected;
    }
    
    public synchronized void loadNeuralNetwork(String path, boolean forceRetrain) throws Exception {
        if (Files.exists(Paths.get(path)) && !forceRetrain) {
            this.nn = Aicme4jUtils.load(path);
        } else {
            if (expected.length == 0 || data.length == 0)
                throw new Exception("Valid training data is required.");

            var nnBuilder = NetworkBuilderFactory
                .getInstance().newNetworkBuilder()
                .inputLayer("Input", data[0].length);
            
            for (int i = 0; i < this.hiddenLayerSizes.length; i++) {
                nnBuilder.hiddenLayer("Hidden" + i, Activation.ARCTAN, this.hiddenLayerSizes[i]);
            }
            
            this.nn = nnBuilder.outputLayer("Output", Activation.TANH, expected[0].length, false)
                .train(data, expected, this.learningRate, this.momentum, this.epochs, this.maxError, Loss.SSE)
                .save(path)
                .build();
        }
    }

    public abstract void loadNeuralNetwork(boolean forceNNRebuild) throws Exception;

    public NeuralNetwork getNeuralNetwork() {
        return this.nn;
    }
    
    public String getAIType() {
        return "NN";
    }
}
