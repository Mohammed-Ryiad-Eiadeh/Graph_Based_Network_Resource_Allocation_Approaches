package MainPackage.org;

import Attack_Defence_Graph.org.Graph;
import Attack_Defence_Graph.org.GraphData;
import ConcurrentAttacks.org.ConcurrentAttack;
import CostFunction.org.CostFunction;
import Defender.org.Defenders;
import GraphAnalysisAndDefense.org.MarkovBlanket;
import GraphAnalysisAndDefense.org.PageRankV1;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * This class is used to allocate resourcess according to Page Rank (Original) with Markov Blanket
 */
public class PageRankV1WithMBMain {
    public static void main(String[] args) {
        var str = "1.85\t2.96\t4.30\t8.12\t5.47\t5.58\t7.54\t7.57\t4.41\t5.72\t5.77";
        var arrStr = str.split("\t");
        Arrays.stream(arrStr).forEach(item -> System.out.print(" & " + item));
        System.out.println("\r");
        System.exit(0);
        System.out.println("The performance of Page Rank and Markov Blanket ----------");
        // Select the test case or the graph; construct the defenders; construct the adjacensy matrix; display the graph.
        var task = new GraphData(Graph.AWS03_rand);
        var attackDefenceGraph = task.getAttackDefenceGraph();
        var AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var assetLossVec = task.getNodeAssetsLossValues();
        //task.Display(AdjMat);

        // Generate paths by genetic algorithm from each entry node to each asset
        var concurrentAttackers = new ConcurrentAttack(AdjMat,
                assetLossVec,
                500,
                0.2,
                0.4,
                0.6,
                1000);
        var cocurrentAttacks = concurrentAttackers.getPaths();

        // Calculate the cost for the generated paths before allocating the resources
        var assets = cocurrentAttacks.keySet();
        var costFunctionBeforeAllocation = new CostFunction(AdjMat, assetLossVec);
        var CostsBeforeAllocation = 0.0d;
        for (var asset : assets) {
            var pathsToThisAsset = cocurrentAttacks.get(asset);
            for (var path : pathsToThisAsset) {
                CostsBeforeAllocation += costFunctionBeforeAllocation.computeCost(path);
            }
            CostsBeforeAllocation += CostsBeforeAllocation / pathsToThisAsset.size();
        }
        System.out.println("Before the allocation, the cost of these attacks approaching the assets successfully is : "
                + CostsBeforeAllocation);

        // Asset loss vector
        var assetLossVector = task.getNodeAssetsLossValues();

        // Set the spare budget of security resourcess for each defender
        Defenders.spareBudget_D = 5d;

        // This code segment is referred to rank each asset according to page rank algorithm
        var PRMB = new PageRankV1(AdjMat, assetLossVector);
        var scores = PRMB.scoreResults();

        var startTime = System.currentTimeMillis();
        // This code segment is referred to capture the conncetions among each asset and its Markov blanket nodes
        var FLDN = new MarkovBlanket(AdjMat, assetLossVector);
        var nodes = FLDN.retrieveNodeOfMarkovBlanket();

        // Set the spare budget of security resourcess for each defender
        var budget = Defenders.spareBudget_D;

        // This code segment is referred to allocate the resources according to each assets' rank and its Markov blanket
        for (var assetNode : nodes.keySet()) {
            var allNeighbors = nodes.get(assetNode);
            for (var type : allNeighbors.keySet()) {
                var nodeList = allNeighbors.get(type);
                for (var nod : nodeList) {
                    var edge = (Objects.equals(type, "Children")) ? attackDefenceGraph[assetNode - 1][nod - 1] :
                            attackDefenceGraph[nod - 1][assetNode - 1];
                    var sizeOfCapturedNodesByMB = nodes.get(assetNode).values().stream().
                            flatMap(Collection::stream).toList().size();
                    var currentAssetCutOfTotalBudget = budget * scores.get(assetNode) / sizeOfCapturedNodesByMB;
                    edge.setInvest_D(edge.addSpareInvestFor_D(currentAssetCutOfTotalBudget));
                    budget -= currentAssetCutOfTotalBudget;
                }
                // System.out.println(assetNode + "\t" + type + "\t" + allNeighbors.get(type));
            }
        }
        var endTime = System.currentTimeMillis();
        // Update the adjacensy matrix
        AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var costFunctionAfterAllocation = new CostFunction(AdjMat, assetLossVec);
        var CostsAfterAllocation = 0.0d;
        for (var asset : assets) {
            var pathsToThisAsset = cocurrentAttacks.get(asset);
            for (var path : pathsToThisAsset) {
                CostsAfterAllocation += costFunctionAfterAllocation.computeCost(path);
            }
            CostsAfterAllocation += CostsAfterAllocation / pathsToThisAsset.size();
        }
        System.out.println("After the allocation, the cost of these attacks approaching the assets successfully is : "
                + CostsAfterAllocation);
        System.out.println("The relative reduction of the cost of these attacks approaching the assets successfully is : "
                + Math.abs(CostsBeforeAllocation - CostsAfterAllocation) / CostsBeforeAllocation * 100 + "\t%");
        System.out.println("The duration time of the allocation process took : " + (endTime - startTime) + " ms");
    }
}
