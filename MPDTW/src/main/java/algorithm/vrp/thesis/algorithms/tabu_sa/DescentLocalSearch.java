package algorithm.vrp.thesis.algorithms.tabu_sa;

import algorithm.vrp.thesis.algorithms.localsearch.ExchangeRequests;
import algorithm.vrp.thesis.algorithms.localsearch.ReArrangeRequests;
import algorithm.vrp.thesis.algorithms.localsearch.RelocateRequests;
import algorithm.vrp.thesis.problem.Instance;
import algorithm.vrp.thesis.problem.Solution;
import algorithm.vrp.thesis.problem.SolutionUtils;

import java.util.LinkedList;

public class DescentLocalSearch {

    private RelocateRequests relocateRequests;

    private ExchangeRequests exchangeRequests;

    private ReArrangeRequests reArrangeRequests;

    public DescentLocalSearch(Instance instance) {
        this.relocateRequests = new RelocateRequests(instance);
        this.exchangeRequests = new ExchangeRequests(instance);
        this.reArrangeRequests = new ReArrangeRequests(instance);
    }

    public Solution findBestByShiftAndExchange(Solution solution) {
        boolean improvement = true;
        Solution improved = SolutionUtils.copy(solution);
        Solution tempSolution = improved;
        while (improvement) {
            improvement = false;
            tempSolution = relocateRequests.relocate(tempSolution);
            tempSolution = exchangeRequests.exchange(tempSolution);
            if (SolutionUtils.getBest(improved, tempSolution) == tempSolution) {
                improved = SolutionUtils.copy(tempSolution);
                improvement = true;
            }
        }
        return improved;
    }

    public LinkedList<Solution> findNeighborByShiftAndExchange(Solution solution) {
        LinkedList<Solution> neighborhood = new LinkedList<>();
        Solution improved = SolutionUtils.copy(solution);
        Solution tempSolution = improved;
        relocateRequests.relocate(tempSolution, neighborhood);
        exchangeRequests.exchange(tempSolution, neighborhood);
        if (neighborhood.isEmpty()) {
            neighborhood.add(tempSolution);
        }
        return neighborhood;
    }

    public Solution executeReArrange(Solution solution) {
        boolean improvement = true;
        Solution improved = SolutionUtils.copy(solution);
        Solution tempSolution = improved;
        while (improvement) {
            improvement = false;
            tempSolution = reArrangeRequests.reArrange(tempSolution);
            if (SolutionUtils.getBest(improved, tempSolution) == tempSolution) {
                improved = SolutionUtils.copy(tempSolution);
                improvement = true;
            }
        }
        return improved;
    }

}
