package MainPackage.org;

import Attack_Defence_Graph.org.Graph;
import Attack_Defence_Graph.org.GraphData;
import ConcurrentAttacks.org.ConcurrentAttack;
import CostFunction.org.CostFunction;
import Defender.org.Defenders;
import GraphAnalysisAndDefense.org.InDegreeNodes;
import GraphAnalysisAndDefense.org.PageRank;

/**
 * This class is used to allocate resourcess according to Page Rank (ours) with in degree nodes
 */
public class PageRankWithInDegreeMain {
    public static void main(String[] args) {
        System.out.println("The performance of Page Rank and Neighbor Nodes ----------");
        // Select the test case or the graph; construct the defenders; construct the adjacensy matrix; display the graph.
        var task = new GraphData(Graph.Figure3);
        var attackDefenceGraph = task.getAttackDefenceGraph();
        var AdjMat = task.getAdjacencyMatrix(attackDefenceGraph);
        var assetLossVec = task.getNodeAssetsLossValues();
        task.Display(AdjMat);

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

        // Set the spare budget of security resourcess for each defender
        Defenders.spareBudget_D = 5d;

        var startTime = System.currentTimeMillis();
        // This code segment is referred to rank each asset according to page rank algorithm
        var PRMB = new PageRank(AdjMat,
                assetLossVec,
                100,
                PageRank.PR_DenominatorType.numOfOutEdges);
        var scores = PRMB.getScores();
        System.out.println(scores);

        // This code segment is referred to capture the conncetions among each asset and its neighbor nodes
        var IDN = new InDegreeNodes(AdjMat, assetLossVec);
        var nodes = IDN.retrieveInDegreeNodes();

        // This code print out the ranks of each node for each iteration which is used to display the convergence curves of PR algorithm
        var rankOfEachIteration = PRMB.getRankOfEachIteration();
        for (var node = 0; node < rankOfEachIteration.length; node++) {
            if (assetLossVec[node] > 0) {
                System.out.println("ranks for the node : " + (node + 1));
                rankOfEachIteration[node].forEach(System.out::println);
                System.out.println();
            }
        }
        // Till here for the covergence

        // Set the spare budget of security resourcess for each defender
        var budget = Defenders.spareBudget_D;

        // This code segment is referred to allocate spare defenders investments on paths involoved in "in" and "out" degree edges
        for (var assetNode : nodes.keySet()) {
            var parents = nodes.get(assetNode);
            for (var nod : parents) {
                var edge = attackDefenceGraph[nod - 1][assetNode - 1];
                var sizeOfparents = nodes.get(assetNode).stream().toList().size();
                var currentAssetCutOfTotalBudget = budget * scores.get(assetNode) / sizeOfparents;
                edge.setInvest_D(edge.addSpareInvestFor_D(currentAssetCutOfTotalBudget));
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
