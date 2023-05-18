package algorithm.vrp.thesis.algorithms;

import algorithm.vrp.thesis.problem.Solution;

public abstract class RemovalOperator extends Operator {

    public abstract void remove(Solution solution, int q);

}
