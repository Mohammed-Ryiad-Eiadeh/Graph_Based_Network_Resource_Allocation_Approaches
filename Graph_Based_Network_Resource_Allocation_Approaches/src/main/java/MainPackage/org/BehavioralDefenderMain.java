package MainPackage.org;

import Attack_Defence_Graph.org.Graph;
import Attack_Defence_Graph.org.GraphData;
import ConcurrentAttacks.org.ConcurrentAttack;
import CostFunction.org.CostFunction;
import Defender.org.Defenders;
import GraphAnalysisAndDefense.org.BehavioralDefender;

/**
 * This class is used as the main class for the first scenario according to bahavioral defender
 */
public class BehavioralDefenderMain {
    public static void main(String[] args) {
        System.out.println("The performance of Behavioral Defender ----------");
        // Select the test case or the graph; construct the defenders; construct the adjacensy matrix; display the graph.
        var task = new GraphData(Graph.AWS03_rand);
        var attackDefenceGraph = task.getAttackDefenceGraph();
        var AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var assetLossVec = task.getNodeAssetsLossValues();
        // task.Display(AdjMat);

        // Generate paths by genetic algorithm from each entry node to each asset
        var concurrentAttackers = new ConcurrentAttack(AdjMat,
                assetLossVec,
                300,
                0.2,
                0.4,
                0.6,
                500);
        var cocurrentAttacks = concurrentAttackers.getPaths();

        // Calculate the cost for the generated paths before allocating the resources
        var assets = cocurrentAttacks.keySet();
        var costFunctionBeforeAllocation = new CostFunction(AdjMat, assetLossVec);
        var sumOfCostsBeforeAllocation = 0.0d;
        for (var asset : assets) {
            var pathsToThisAsset = cocurrentAttacks.get(asset);
            for (var path : pathsToThisAsset) {
                sumOfCostsBeforeAllocation += costFunctionBeforeAllocation.computeCost(path);
            }
            sumOfCostsBeforeAllocation += sumOfCostsBeforeAllocation / pathsToThisAsset.size();
        }
        System.out.println("Before the allocation, the cost of these attacks approaching the assets successfully is : "
                + sumOfCostsBeforeAllocation);

        // Set the spare budget of security resourcess for each defender
        Defenders.spareBudget_D = 5d;

        // This code segment is referred to allocate spare defenders investments on the most potential path (top-1) according to behavioral defender
        // Set the spare budget of security resourcess for each defender
        var budget = Defenders.spareBudget_D;

        // Apply the behavioral defender
        var behavioralDefender = new BehavioralDefender(AdjMat, 0.5f);
        var newWeights = behavioralDefender.applyBehavioralDefendingForResourceAllocation();

        var sumOfProbWeighting = 0.0d;
        for (double[] newWeight : newWeights) {
            for (var j = 0; j < newWeights[0].length; j++) {
                sumOfProbWeighting += newWeight[j];
            }
        }

        for (var i = 0; i < AdjMat.length; i++) {
            for (var j = 0; j < AdjMat[0].length; j++) {
                if (AdjMat[i][j] > 0) {
                    var edge = attackDefenceGraph[i][j];
                    edge.setInvest_D(edge.addSpareInvestFor_D(newWeights[i][j] / sumOfProbWeighting * budget));
                }
            }
        }
        // Update the adjacensy matrix
        AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var costFunctionAfterAllocation = new CostFunction(AdjMat, assetLossVec);
        var sumOfCostsAfterAllocation = 0.0d;
        for (var asset : assets) {
            var pathsToThisAsset = cocurrentAttacks.get(asset);
            for (var path : pathsToThisAsset) {
                sumOfCostsAfterAllocation += costFunctionAfterAllocation.computeCost(path);
            }
            sumOfCostsAfterAllocation += sumOfCostsAfterAllocation / pathsToThisAsset.size();
        }
        System.out.println("After the allocation, the cost of these attacks approaching the assets successfully is : "
                + sumOfCostsAfterAllocation);
        System.out.println("The relative reduction of the cost of these attacks approaching the assets successfully is : "
                + Math.abs(sumOfCostsBeforeAllocation - sumOfCostsAfterAllocation) / sumOfCostsBeforeAllocation * 100 + "\t%");
    }
}
