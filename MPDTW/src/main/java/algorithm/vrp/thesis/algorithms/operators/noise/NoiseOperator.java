package algorithm.vrp.thesis.algorithms.operators.noise;

import algorithm.vrp.thesis.algorithms.Operator;

public class NoiseOperator extends Operator {

    private int useNoise;

    public NoiseOperator(int useNoise) {
        this.useNoise = useNoise;
    }

    public int getUseNoise() {
        return useNoise;
    }
}
