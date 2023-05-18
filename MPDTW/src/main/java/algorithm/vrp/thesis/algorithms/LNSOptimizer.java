package algorithm.vrp.thesis.algorithms;

import algorithm.vrp.thesis.problem.Solution;

public interface LNSOptimizer {

    Solution getFeasibleSolutionBest();

    void resetToInitialSolution(Solution solution);

    void optimize(int iteration);

    void setBaseSolution(Solution newSolution);

    Solution getSolutionBest();

    Solution getSolutionLocal();

    double getTemperature();

    void init(Solution solutionBase);

    void setUseLocalSearch(boolean useLocalSearch);

    enum Type {
        LNS, ALNS
    }

}
