package MainPackage.org;

import Attack_Defence_Graph.org.Graph;
import Attack_Defence_Graph.org.GraphData;
import ConcurrentAttacks.org.ConcurrentAttack;
import CostFunction.org.CostFunction;
import Defender.org.Defenders;
import GraphAnalysisAndDefense.org.MarkovBlanket;

import java.util.ArrayList;

/**
 * This class is used as the main class for the Markov Blanket
 */
public class MarkovBlanketMain {
    public static void main(String[] args) {
        System.out.println("The performance of Markov Blanket ----------");
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

        // Asset loss vector
        var assetLossVector = task.getNodeAssetsLossValues();

        // Set the spare budget of security resourcess for each defender
        Defenders.spareBudget_D = 5d;

        // This code segment is referred to allocate spare defenders investments on paths involoved in Markov blanket
        var FLDN = new MarkovBlanket(AdjMat, assetLossVector);
        var nodes = FLDN.retrieveNodeOfMarkovBlanket();

        var size = nodes.values().stream().mapToInt(i -> i.values().stream().mapToInt(ArrayList::size).sum()).sum();
        var budget = Defenders.spareBudget_D / size;

        for (var assetNode : nodes.keySet()) {
            var allNeighbors = nodes.get(assetNode);
            for (var type : allNeighbors.keySet()) {
                var nodeList = allNeighbors.get(type);
                for (var nod : nodeList) {
                    var edge = (type == 0) ? attackDefenceGraph[assetNode - 1][nod - 1] :
                            attackDefenceGraph[nod - 1][assetNode - 1];
                    edge.setInvest_D(edge.addSpareInvestFor_D(budget));
                }
                // System.out.println(assetNode + "\t" + type + "\t" + allNeighbors.get(type));
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
