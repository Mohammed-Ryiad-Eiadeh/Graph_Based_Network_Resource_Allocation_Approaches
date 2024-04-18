package RankingAlgorithms.org;

import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

/**
 * This class is used to rank each asset node according to a well-known algorithm called Page Rank.
 * <pre>
 *     The anatomy of a large-scale hypertextual Web search engine, Sergey Brin and Lawrence Page, 1998
 *     {J}{G}raph{T}--{A} {J}ava {L}ibrary for {G}raph {D}ata {S}tructures and {A}lgorithms, Michail et al, 2020
 * </pre>
 */
public class PageRank_JGraphT {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;

    /**
     * This constructor is used to initialize the adjacency matrix that is going to be passed to page rank
     * @param adjMatrix The adjacency matrix which represents the attack-defence graph
     * @param lossVector The assets' loss values as a vector
     */
    public PageRank_JGraphT(double[][] adjMatrix, double[] lossVector) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (lossVector == null) {
            throw new IllegalArgumentException("The loss vector is null");
        }
        this.adjMatrix = adjMatrix;
        this.assetLossVec = lossVector;
    }

    /**
     * This method is used to call page rank algorithm
     * @return The scores of the assets (ranks)
     */
    public HashMap<Integer, Double> scoreResults() {
        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        double[][] Data = this.adjMatrix;
        for (int node = 0; node < Data[0].length; node++) {
            graph.addVertex(node);
        }
        for (int node = 0; node < Data.length; node++) {
            for (int nod = 0; nod < Data[0].length; nod++) {
                if (Data[node][nod] > 0) {
                    graph.addEdge(node, nod);
                    graph.setEdgeWeight(node, nod, Math.exp(-Data[node][nod]));
                }
            }
        }
        PageRank<Integer, DefaultWeightedEdge> pageRank = new PageRank<>(graph);
        HashMap<Integer, Double> scores = new HashMap<>();
        for (int node = 0; node < assetLossVec.length; node++) {
            if (assetLossVec[node] > 0) {
                scores.put(node + 1, pageRank.getVertexScore(node));
            }
        }
        double totalSumOfRanks = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        for (Integer key : scores.keySet()) {
            scores.replace(key, scores.get(key) / totalSumOfRanks);
        }
        return scores;
    }
}