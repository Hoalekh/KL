package algorithm.vrp.mpdptw.aco;

import algorithm.vrp.mpdptw.MMAS;
import algorithm.vrp.mpdptw.ProblemInstance;

import java.util.Random;

public interface SolutionBuilder {

    void init(ProblemInstance instance, Random random, MMAS mmas);

    void constructSolutions();

    void onSearchControlExecute();

}
