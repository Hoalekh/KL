package algorithm.vrp.runners;

import algorithm.statistic.IterationStatistic;
import algorithm.tsp.utils.Maths;
import algorithm.vrp.mpdptw.DataReader;
import algorithm.vrp.mpdptw.ProblemInstance;
import algorithm.vrp.mpdptw.Solver;
import algorithm.vrp.mpdptw.aco.SequentialFeasibleMPDPTW;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MPDPTW_MMAS {

    private static String rootDirectory;

    private static int statisticInterval = 10;

    private static int maxIterations = 100;

    private static int seed = 1;

    static {
        try {
            rootDirectory = Paths.get(MPDPTW_MMAS.class.getClassLoader().getResource("mpdptw").toURI()).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {


        executeTest("n_4_25_1.txt");
        executeTest("n_4_50_1.txt");
        executeTest("n_4_100_1.txt");
        executeTest("n_4_400_1.txt");

        executeTest("n_8_25_1.txt");
        executeTest("n_8_50_1.txt");
        executeTest("n_8_100_1.txt");
        executeTest("n_8_400_1.txt");

        executeTest("l_4_25_1.txt");
        executeTest("l_4_50_1.txt");
        executeTest("l_4_100_1.txt");
        executeTest("l_4_400_1.txt");

        executeTest("l_8_25_1.txt");
        executeTest("l_8_50_1.txt");
        executeTest("l_8_100_1.txt");
        executeTest("l_8_400_1.txt");

        executeTest("w_4_25_1.txt");
        executeTest("w_4_50_1.txt");
        executeTest("w_4_100_1.txt");
        executeTest("w_4_400_1.txt");

        executeTest("w_8_25_1.txt");
        executeTest("w_8_50_1.txt");
        executeTest("w_8_100_1.txt");
        executeTest("w_8_400_1.txt");

    }

    private static void executeTest(String problem) throws IOException {
        System.out.println(problem);
        ProblemInstance instance = DataReader.getMpdptwInstance(Paths.get(rootDirectory, problem).toFile());
        Solver solver = new Solver(problem, instance, 10, seed, 0.8, 1, true);
        solver.setSolutionBuilderClass(SequentialFeasibleMPDPTW.class);
        solver.setParallel(false);
        solver.run();
//        ArrayList<Double> costs = new ArrayList<>();
//        ArrayList<Double> time = new ArrayList<>();
//        ArrayList<Double> penalty = new ArrayList<>();
//        int sampleSize = 5, feasible = 0;
//        for (int i = 0; i < sampleSize; i++) {
//
//            ProblemInstance instance = DataReader.getMpdptwInstance(Paths.get(rootDirectory, problem).toFile());
//            Solver solver = new Solver(problem, instance, maxIterations, i, 0.2, statisticInterval, true);
//            solver.setParallel(true);
//            solver.setLsActive(true);
//            solver.run();
//            costs.add(solver.getBestSolution().totalCost);
//            penalty.add(solver.getBestSolution().timeWindowPenalty);
//            time.add(solver.getGlobalStatistics().getTimeStatistics().get("Algorithm").doubleValue());
//            if (solver.getBestSolution().feasible) {
//                feasible++;
//            }
//        }
//        double meanCosts = Maths.getMean(costs);
//        double meanTime = Maths.getMean(time);
//        double meanPenalty = Maths.getMean(penalty);
//        StringBuilder resume = new StringBuilder();
//        resume.append("\n---------------------------------------");
//        resume.append("\nInstance = " + problem);
//        resume.append("\nMean costs = " + meanCosts);
//        resume.append("\nMean penalty = " + meanPenalty);
//        resume.append("\nMean time = " + meanTime);
//        resume.append("\nFeasible = " + feasible + " of " + sampleSize);
//        resume.append("\n---------------------------------------");
//        FileUtils.writeStringToFile(new File("C:\\Temp\\mpdptw\\_resume.txt"), resume.toString(), "UTF-8", true);
    }
}
