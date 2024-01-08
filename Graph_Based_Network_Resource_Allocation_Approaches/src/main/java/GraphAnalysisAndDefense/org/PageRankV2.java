package GraphAnalysisAndDefense.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to rank each asset node acording to a well-known algorith called Page Rank.
 * <pre>
 *     ours
 * </pre>
 */
public class PageRankV2 {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;
    private final HashMap<Integer, HashMap<Integer, Integer>> mapNodeToParent;
    private final int numberOfNodes;
    private final int maxIter;
    private final double Wf;
    private final double dampingFactor;
    private final float epsilon;

    /**
     * This constructor is used to initialize the adjacency matrix that is going to be passed to page rank
     * @param adjMatrix The adjacency matrix which represents the attack-defence graph
     * @param lossVector The assets' loss values as a vector
     * @param maxIteration The number of iteration to keep ranking the nodes
     * @param weightFactor The weight factor which is relied on in [0,1]
     */
    public PageRankV2(double[][] adjMatrix, double[] lossVector, int maxIteration, double weightFactor) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (lossVector == null) {
            throw new IllegalArgumentException("The loss vector is null");
        }
        if (maxIteration < 0) {
            throw new IllegalArgumentException("The number of iteration should be positive integer");
        }
        if (weightFactor < 0 || weightFactor > 1) {
            throw new IllegalArgumentException("The weight factor should be relied in 0 and 1");
        }
        this.adjMatrix = linkBack(adjMatrix);
        this.assetLossVec = lossVector;
        this.maxIter =  maxIteration;
        this.Wf = weightFactor;
        this.dampingFactor = 0.85;
        this.epsilon = 0.0001F;
        this.numberOfNodes = adjMatrix.length;
        this.mapNodeToParent = new HashMap<>();
        for (int nod = 0; nod < numberOfNodes; nod++) {
            mapNodeToParent.put(nod + 1, new HashMap<>());
        }
    }

    /**
     * This method is used to apply the Link Back technique in order to handle the issue of sink or dangling nodes.
     * @param adjMatrix The adjacency matrix which represents the attack-defence graph
     * @return The adjacency matrix after applying the Link Back operation
     */
    private double[][] linkBack(double[][] adjMatrix) {
        double[][] mat = new double[adjMatrix.length][adjMatrix[0].length];
        for (int vec = 0; vec < mat.length; vec++) {
            System.arraycopy(adjMatrix[vec], 0, mat[vec], 0, mat[0].length);
        }
        for (int node = 0; node < mat.length; node++) {
            boolean flag = false;
            for (int nod = 0; nod < mat[0].length; nod++) {
                if (mat[node][nod] > 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (int nod = 0; nod < mat[0].length; nod++) {
                    if (mat[nod][node] > 0) {
                        mat[nod][node] = mat[node][nod];
                    }
                }
            }
        }
        return mat;
    }

    /**
     * This method is used to calculate the nodes ranks
     * @return The nodes ranks
     */
    public HashMap<Integer, Double> startRanking() {
        HashMap<Integer, Double> mapNodeToRank = new HashMap<>();
        HashMap<Integer, Double> oldRanks = new HashMap<>();
        for (int i = 0; i < numberOfNodes; i++) {
            mapNodeToRank.put(i + 1, 1.0 / numberOfNodes);
        }
        for (int iter = 0; iter < maxIter; iter++) {
            for (int nod : mapNodeToRank.keySet()) {
                // We get the parents (in edges) of the current node
                var parents = getInDegree(nod);
                // We get the number of incoming edges for each parent node of the given nod
                for (Integer parent : parents) {
                    mapNodeToParent.get(nod).put(parent, getInDegree(parent).size());
                }
                // Hold the parents of the given nod
                HashMap<Integer, Integer> givenNode = mapNodeToParent.get(nod);
                // if they don't have incoming nodes, then do what inside the if statement
                if (givenNode.isEmpty()) {
                    mapNodeToRank.put(nod, (1 - dampingFactor) / numberOfNodes + Wf * assetLossVec[nod - 1]);
                }
                // If the parents of the given nod have parents (in degree links) then do the following
                else {
                    double totalRank = 0d;
                    for (Map.Entry<Integer, Integer> incomeNode : givenNode.entrySet()) {
                        int currentNode = incomeNode.getKey();
                        double sumOfOutEdgesWeights = getSumOfOutEdges(currentNode);
                        double weightOfInDegreeEdges = Math.exp(-adjMatrix[currentNode - 1][nod - 1]);
                        totalRank += weightOfInDegreeEdges * mapNodeToRank.get(currentNode) / sumOfOutEdgesWeights;
                    }
                    totalRank = (1 - dampingFactor) / numberOfNodes + dampingFactor * totalRank + Wf * assetLossVec[nod - 1];
                    mapNodeToRank.put(nod, totalRank);
                }
            }
            // Take a copy of the ranks and apply Euclidean distance to check the convergence of PageRank given the ebsilon
            if (iter == 1) {
                oldRanks.putAll(mapNodeToRank);
            }
            else if (iter > 1) {
                double sumOfSubtraction = 0d;
                for (int key : oldRanks.keySet()) {
                    sumOfSubtraction += Math.pow(oldRanks.get(key) - mapNodeToRank.get(key), 2);
                }
                double euclideanDist = Math.sqrt(sumOfSubtraction);
                if (euclideanDist < epsilon) {
                    break;
                }
                oldRanks.putAll(mapNodeToRank);
            }
        }
        // Normalizing the ranks of nodes
        double sumOfAllRanks = mapNodeToRank.values().stream().mapToDouble(value -> value).sum();
        mapNodeToRank.replaceAll((n, v) -> mapNodeToRank.get(n) / sumOfAllRanks);
        return mapNodeToRank;
    }

    /**
     * This method is used to get the in degree nodes for the given node.
     * @param node The node of interest.
     * @return List of in degree nodes.
     */
    private ArrayList<Integer> getInDegree(int node) {
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[i][node - 1] > 0) {
                parentsNodes.add(i + 1);
            }
        }
        return parentsNodes;
    }

    /**
     * This method is used to calculate the sum of out degree edges weights for the given node
     * @param node The node of interest
     * @return The summation of the weights
     */
    private double getSumOfOutEdges(int node) {
        double sumOfWeights = 0d;
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[node - 1][i] > 0) {
                sumOfWeights += Math.exp(-adjMatrix[node - 1][i]);
            }
        }
        return sumOfWeights;
    }

    /**
     * This method is used to normalize the nodes ranks and retrieve them
     * @return The normalized ranks of the asset nodes
     */
    public HashMap<Integer, Double> getScores() {
        HashMap<Integer, Double> ranks = startRanking();
        HashMap<Integer, Double> scores = new HashMap<>();
        for (int node = 0; node < assetLossVec.length; node++) {
            if (assetLossVec[node] > 0) {
                scores.put(node, ranks.get(node));
            }
        }
        double totalSumOfRanks = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        scores.keySet().forEach(key -> scores.replace(key, scores.get(key) / totalSumOfRanks));
        return scores;
    }
}

