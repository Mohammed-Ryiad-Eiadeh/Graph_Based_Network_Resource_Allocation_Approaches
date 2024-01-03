package MainPackage.org;

import Attack_Defence_Graph.org.Graph;
import Attack_Defence_Graph.org.GraphData;
import ConcurrentAttacks.org.ConcurrentAttack;
import CostFunction.org.CostFunction;
import Defender.org.Defenders;
import GraphAnalysisAndDefense.org.FirstOrderNeighbor;
import GraphAnalysisAndDefense.org.PageRankV1;

import java.util.Collection;

public class PageRankV1WithNeighborsMain {
    public static void main(String[] args) {
        System.out.println("The performance of Page Rank and Neighbor Nodes ----------");
        // Select the test case or the graph; construct the defenders; construct the adjacensy matrix; display the graph.
        var task = new GraphData(Graph.AWS03_rand);
        var attackDefenceGraph = task.getAttackDefenceGraph();
        var AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var assetLossVec = task.getNodeAssetsLossValues();

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

        // This code segment is referred to allocate spare defenders investments on paths involoved in Markov Blanket
        var FLDN = new FirstOrderNeighbor(AdjMat, assetLossVector);
        var nodes = FLDN.retrieveNodeOfFirstLevelDegree();

        // Set the spare budget of security resourcess for each defender
        var budget = Defenders.spareBudget_D;

        for (var assetNode : nodes.keySet()) {
            var allNeighbors = nodes.get(assetNode);
            for (var type : allNeighbors.keySet()) {
                var nodeList = allNeighbors.get(type);
                for (var nod : nodeList) {
                    var edge = (type == 0) ? attackDefenceGraph[assetNode - 1][nod - 1] :
                            attackDefenceGraph[nod - 1][assetNode - 1];
                    var sizeOfNeighborNodes = nodes.get(assetNode).values().stream().flatMap(Collection::stream).toList().size();
                    var currentAssetCutOfTotalBudget = budget * scores.get(assetNode - 1) / sizeOfNeighborNodes;
                    edge.setInvest_D(edge.addSpareInvestFor_D(currentAssetCutOfTotalBudget));
                }
                // System.out.println(assetNode + "\t" + type + "\t" + allNeighbors.get(type));
            }
        }
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
    }
}
