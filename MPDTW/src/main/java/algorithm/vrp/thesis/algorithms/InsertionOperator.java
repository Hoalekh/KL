package algorithm.vrp.thesis.algorithms;

import algorithm.vrp.thesis.problem.Solution;

public abstract class InsertionOperator extends Operator {

    public abstract void insert(Solution solution, int useNoise);

}
