package algorithm.vrp.dvrptwacs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class InsertionHeuristic {

    private LoggerOutput loggerOutput;

    public InsertionHeuristic(LoggerOutput loggerOutput) {
        this.loggerOutput = loggerOutput;
    }

    public void computeRouteVariables(Ant a, VRPTW vrp, int startTourIndex) {
        ArrayList<Request> reqList = vrp.getRequests();
        //for every customer already included in the solution, compute route variables (earliest time a
        //delivery can be made at a customer and the latest time a delivery can be made at a customer)
        //used when checking feasibility of an insertion
        int city, previousCity, nextCity;
        double value, value1, value2;
        //move forward in every tour and compute the values for the earliest time
        for (int index = startTourIndex; index < a.usedVehicles; index++) {
            int tourLength = a.tours.get(index).size();
            if (a.earliestTime == null) {
                loggerOutput.log("Earliest time is null");
            }
            a.earliestTime.add(index, new ArrayList<>(tourLength));
            for (int i = 0; i < tourLength; i++) {
                city = a.tours.get(index).get(i);
                //the current city (customer) is the depot
                if (((city + 1) == 0) && (i == 0)) {
                    value = new Double(reqList.get(0).getStartWindow());
                    a.earliestTime.get(index).add(i, value);
                } else {
                    previousCity = a.tours.get(index).get(i - 1);
                    value1 = new Double(reqList.get(city + 1).getStartWindow());
                    value2 = a.earliestTime.get(index).get(i - 1) + vrp.instance.distance[previousCity + 1][city + 1] + reqList.get(previousCity + 1).getServiceTime();
                    value = Math.max(value1, value2);
                    a.earliestTime.get(index).add(i, value);
                }
            }
        }
        //move backward in every tour and compute the values for the latest time
        for (int index = startTourIndex; index < a.usedVehicles; index++) {
            int tourLength = a.tours.get(index).size();
            a.latestTime.add(index, new ArrayList<>(Collections.nCopies(tourLength, 0.0)));
            for (int i = tourLength - 1; i >= 0; i--) {
                city = a.tours.get(index).get(i);
                if (i == tourLength - 1) {
                    value = new Double(reqList.get(0).getEndWindow());
                    value1 = new Double(reqList.get(city + 1).getEndWindow());
                    value2 = value - vrp.instance.distance[city + 1][0] - reqList.get(city + 1).getServiceTime();
                    value = Math.min(value1, value2);
                    a.latestTime.get(index).set(i, value);
                } else {
                    nextCity = a.tours.get(index).get(i + 1);
                    value1 = new Double(reqList.get(city + 1).getEndWindow());
                    value2 = a.latestTime.get(index).get(i + 1) - vrp.instance.distance[city + 1][nextCity + 1] - reqList.get(city + 1).getServiceTime();
                    value = Math.min(value1, value2);
                    a.latestTime.get(index).set(i, value);
                }
            }
        }
    }

    public void updateRouteVariables(Ant a, VRPTW vrp, Insertion ins) {
        int previousCity, nextCity, bestPos, bestIndexTour;
        double value1, value2, oldLatestTime, newLatestTime, oldEarliestTime, newEarliestTime;
        ArrayList<Request> reqList = vrp.getRequests();
        bestIndexTour = ins.getIndexTour();
        bestPos = ins.getPreviousNode();
        for (int k = bestPos - 1; k >= 0; k--) {
            previousCity = a.tours.get(bestIndexTour).get(k);
            nextCity = a.tours.get(bestIndexTour).get(k + 1);
            oldLatestTime = a.latestTime.get(bestIndexTour).get(k);
            value1 = a.latestTime.get(bestIndexTour).get(k + 1) - vrp.instance.distance[previousCity + 1][nextCity + 1] - reqList.get(previousCity + 1).getServiceTime();
            newLatestTime = Math.min(oldLatestTime, value1);
            if (oldLatestTime != newLatestTime) {
                a.latestTime.get(bestIndexTour).set(k, newLatestTime);
            } else {
                break;
            }
        }
        for (int k = bestPos + 1; k < a.tours.get(bestIndexTour).size(); k++) {
            previousCity = a.tours.get(bestIndexTour).get(k - 1);
            nextCity = a.tours.get(bestIndexTour).get(k);
            oldEarliestTime = a.earliestTime.get(bestIndexTour).get(k);
            value2 = a.earliestTime.get(bestIndexTour).get(k - 1) + vrp.instance.distance[previousCity + 1][nextCity + 1] + reqList.get(previousCity + 1).getServiceTime();
            newEarliestTime = Math.max(oldEarliestTime, value2);
            if (oldEarliestTime != newEarliestTime) {
                a.earliestTime.get(bestIndexTour).set(k, newEarliestTime);
            } else {
                break;
            }
        }
    }

    //check if it is feasible to insert the client denoted by customer, in the tour designated by
    //indexTour; the customer is checked if it can be inserted between the nodes at position previousPos and nextPos
    public boolean isFeasibleInsertion(Ant a, VRPTW vrp, int customer, int indexTour, int previousPos, int nextPos) {
        boolean isFeasible1 = false;
        double currentQuantity, arrivalTime, beginService, earliestTime, latestTime;
        double value1, value2, value3, value4;
        ArrayList<Request> reqList = vrp.getRequests();
        int previousCity = a.tours.get(indexTour).get(previousPos);
        int nextCity = a.tours.get(indexTour).get(nextPos);
        currentQuantity = a.currentQuantity.get(indexTour) + reqList.get(customer + 1).getDemand();
        arrivalTime = a.beginService[previousCity + 1] + reqList.get(previousCity + 1).getServiceTime() + vrp.instance.distance[previousCity + 1][customer + 1];
        beginService = Math.max(arrivalTime, reqList.get(customer + 1).getStartWindow());
        value1 = new Double(reqList.get(customer + 1).getStartWindow());
        value2 = a.earliestTime.get(indexTour).get(previousPos) + vrp.instance.distance[previousCity + 1][customer + 1] + reqList.get(previousCity + 1).getServiceTime();
        earliestTime = Math.max(value1, value2);
        value3 = new Double(reqList.get(customer + 1).getEndWindow());
        value4 = a.latestTime.get(indexTour).get(nextPos) - vrp.instance.distance[customer + 1][nextCity + 1] - reqList.get(customer + 1).getServiceTime();
        latestTime = Math.min(value3, value4);
        if ((currentQuantity <= vrp.getCapacity()) && (earliestTime <= latestTime) && (beginService <= reqList.get(customer + 1).getEndWindow())) {
            isFeasible1 = true;
        }
        return isFeasible1;
    }

    public void insertUnroutedCustomers(Ant a, VRPTW vrp, ArrayList<Integer> unvisitedNodes, int startIndexTour, ArrayList<Integer> startPos) {
        boolean ok = true;
        double c11, c12, c1, c1_, c2, value1, value2, value3, value4, earliestTime, latestTime;
        double bestC1Score;
        double bestC2Score;
        double arrivalTime, beginService = 0.0, newArrivalTime, newBeginService, oldBeginService, newQuantity;
        int previousCity, nextCity, bestPos = 1, bestCustomer, bestIndexTour = 0, bestIndexInsertion = 0;
        int cust;
        double mu = 1.0, alfa1 = 0.1, alfa2 = 0.9, lambda = 2.0;
        ArrayList<Request> reqList = vrp.getRequests();
        Insertion ins, bestInsertion;
        int startIndex;
        //it contains for each unrouted customer its best feasible insertion place
        //this list is used when deciding the best unrouted customer to be inserted in the unfeasible solution
        ArrayList<Insertion> bestInsertions = new ArrayList<>();
        //keep track of nodes (customers) inserted/included in the solution by the insertion heuristic
        HashMap<Integer, Boolean> visited = new HashMap<>(unvisitedNodes.size());
        for (int node : unvisitedNodes) {
            visited.put(node, false);
        }
        computeRouteVariables(a, vrp, startIndexTour);
        while (ok) {
            if (bestInsertions != null && bestInsertions.size() > 0) {
                bestInsertions.clear();
            }
            //Find the best possible feasible position to insert an unrouted customer into a tour
            //then insert it and update the score
            for (int customer : unvisitedNodes) {
                //check if the customer was not included/inserted in the solution in the meantime by the insertion heuristic
                if (!visited.get(customer)) {
                    bestC1Score = Double.MAX_VALUE;
                    for (int indexTour = startIndexTour; indexTour < a.usedVehicles; indexTour++) {
                        if (indexTour > startPos.size() - 1) {
                            startIndex = 1;
                        } else {
                            startIndex = startPos.get(indexTour) + 1;
                        }
                        //check for a feasible insertion place within the considered tour
                        for (int pos = startIndex; pos < a.tours.get(indexTour).size(); pos++) {
                            if (isFeasibleInsertion(a, vrp, customer, indexTour, pos - 1, pos)) {
                                //compute the score (value) for the c1 metric
                                previousCity = a.tours.get(indexTour).get(pos - 1);
                                nextCity = a.tours.get(indexTour).get(pos);
                                c11 = vrp.instance.distance[previousCity + 1][customer + 1] + vrp.instance.distance[customer + 1][nextCity + 1] - mu * vrp.instance.distance[previousCity + 1][nextCity + 1];
                                arrivalTime = a.beginService[previousCity + 1] + reqList.get(previousCity + 1).getServiceTime() + vrp.instance.distance[previousCity + 1][customer + 1];
                                beginService = Math.max(arrivalTime, reqList.get(customer + 1).getStartWindow());
                                newArrivalTime = beginService + reqList.get(customer + 1).getServiceTime() + vrp.instance.distance[customer + 1][nextCity + 1];
                                newBeginService = Math.max(newArrivalTime, reqList.get(nextCity + 1).getStartWindow());
                                oldBeginService = a.beginService[nextCity + 1];
                                c12 = newBeginService - oldBeginService;
                                c1 = alfa1 * c11 + alfa2 * c12;
                                if (c1 < bestC1Score) {
                                    bestC1Score = c1;
                                    bestIndexTour = indexTour;
                                    bestPos = pos;
                                }
                            }
                        }
                    }
                    //we have a best feasible insertion position for the unrouted node denoted by "customer"
                    if (bestC1Score != Double.MAX_VALUE) {
                        ins = new Insertion(customer, bestIndexTour, bestPos, bestC1Score);
                        bestInsertions.add(ins);
                    }
                }

            }
            bestC2Score = Double.MAX_VALUE;
            Insertion insert;
            //decide the best customer to be inserted in the solution
            if (bestInsertions != null && bestInsertions.size() > 0) {
                for (int i = 0; i < bestInsertions.size(); i++) {
                    insert = bestInsertions.get(i);
                    cust = insert.getCustomer();
                    c1_ = insert.getScore();
                    c2 = lambda * vrp.instance.distance[0][cust + 1] - c1_;
                    if (c2 < bestC2Score) {
                        bestC2Score = c2;
                        bestIndexInsertion = i;
                    }
                }
            }
            //we have the best customer to be inserted in the solution, now we should perform the insertion
            //of the selected best customer
            if (bestC2Score != Double.MAX_VALUE) {
                bestInsertion = bestInsertions.get(bestIndexInsertion);
                bestCustomer = bestInsertion.getCustomer();
                bestIndexTour = bestInsertion.getIndexTour();
                bestPos = bestInsertion.getPreviousNode();
                a.tours.get(bestIndexTour).add(bestPos, bestCustomer);
                a.visited[bestCustomer] = true;
                a.toVisit--;
                newQuantity = a.currentQuantity.get(bestIndexTour) + reqList.get(bestCustomer + 1).getDemand();
                a.currentQuantity.set(bestIndexTour, newQuantity);
                visited.put(bestCustomer, true);
                //update earliest time and latest time lists to include the value for the newly inserted customer
                previousCity = a.tours.get(bestIndexTour).get(bestPos - 1);
                nextCity = a.tours.get(bestIndexTour).get(bestPos + 1);
                value1 = new Double(reqList.get(bestCustomer + 1).getStartWindow());
                //value2 = a.earliestTime.get(bestIndexTour).get(bestPos - 1) + VRPTW.instance.distance[previousCity + 1][bestCustomer + 1];
                value2 = a.earliestTime.get(bestIndexTour).get(bestPos - 1) + vrp.instance.distance[previousCity + 1][bestCustomer + 1] + reqList.get(previousCity + 1).getServiceTime();
                earliestTime = Math.max(value1, value2);
                value3 = new Double(reqList.get(bestCustomer + 1).getEndWindow());
                value4 = a.latestTime.get(bestIndexTour).get(bestPos) - vrp.instance.distance[bestCustomer + 1][nextCity + 1] - reqList.get(bestCustomer + 1).getServiceTime();
                latestTime = Math.min(value3, value4);
                a.earliestTime.get(bestIndexTour).add(bestPos, earliestTime);
                a.latestTime.get(bestIndexTour).add(bestPos, latestTime);
                updateRouteVariables(a, vrp, bestInsertion);
                //update the begin service times for the nodes that come after the inserted customer on the route
                //also update the begin service time for the inserted node itself
                for (int j = bestPos; j < a.tours.get(bestIndexTour).size(); j++) {
                    previousCity = a.tours.get(bestIndexTour).get(j - 1);
                    cust = a.tours.get(bestIndexTour).get(j);
                    arrivalTime = a.beginService[previousCity + 1] + reqList.get(previousCity + 1).getServiceTime() + vrp.instance.distance[previousCity + 1][cust + 1];
                    beginService = Math.max(arrivalTime, reqList.get(cust + 1).getStartWindow());
                    a.beginService[cust + 1] = beginService;
                }
                a.currentTime.set(bestIndexTour, beginService);
                ok = true;
            } else {
                ok = false;
            }
        }
    }

}
