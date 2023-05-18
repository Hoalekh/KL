package algorithm.rinsim.dvrptwacs;

import algorithm.vrp.dvrptwacs.Ant;

public interface BestSoFarListener {

    void onBestSoFarFound(Ant ant);

}
