package algorithm.vrp.thesis.algorithms.stop;

public interface StopCriteria {

    void reset();

    void update();

    boolean isContinue();

    double getScaledTime();

    StopCriteria copy();

}
