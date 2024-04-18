package RankingAlgorithms.org;

import java.util.ArrayList;
import java.util.HashMap;

public class TrustRankAlgorithm {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;
    private final double dampingFactor;
    private final float epsilon;
    private final ArrayList<Double>[] rankOfEachIteration;
    private final int maxIter;

    /**
     * This constructor is used to initialize the adjacency matrix that is going to be passed to page rank
     * @param adjMatrix The adjacency matrix which represents the attack-defence graph
     * @param lossVector The assets' loss values as a vector
     * @param maxIter
     */
    public TrustRankAlgorithm(double[][] adjMatrix, double[] lossVector, int maxIter) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (lossVector == null) {
            throw new IllegalArgumentException("The loss vector is null");
        }
        this.adjMatrix = adjMatrix;
        this.assetLossVec = lossVector;
        this.dampingFactor = 0.85;
        this.epsilon = 0.00001f;
        this.rankOfEachIteration = new ArrayList[assetLossVec.length];
        for (int i = 0; i < rankOfEachIteration.length; i++) {
            rankOfEachIteration[i] = new ArrayList<>();
        }
        this.maxIter = maxIter;
    }

    /**
     * This method is used to call the betweenness centrality
     * @return The scores of the assets (ranks)
     */
    public HashMap<Integer, Double> startRanking() {
        // Initialize the oldRanks map to store the old ranks for convergence purposes
        HashMap<Integer, Double> oldRanks = new HashMap<>();
        // Initialize the mapNodeToRank map with initial ranks
        HashMap<Integer, Double> mapNodeToRank = new HashMap<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            mapNodeToRank.put(i + 1, 1.0 / adjMatrix.length);
        }
        // Initialize the initialSeedVector map with initial seed values
        HashMap<Integer, Double> initialSeedVector = new HashMap<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            initialSeedVector.put(i + 1, 2.0);
        }
        // Calculate the out-degree of each node and store in the outDegreeMatrix array
        double[] outDegreeMatrix = new double[adjMatrix.length];
        for (int i = 0; i < adjMatrix.length; i++) {
            outDegreeMatrix[i] = getNumOfOutEdges(i);
        }
        // Create the inverseOutDegreeMatrix, a diagonal matrix with inverse out-degrees as values
        double[][] inverseOutDegreeMatrix = new double[adjMatrix.length][adjMatrix.length];
        for (int i = 0; i < adjMatrix.length; i++) {
            inverseOutDegreeMatrix[i][i] = outDegreeMatrix[i] > 0 ? 1 / outDegreeMatrix[i] : 0.1;
        }
        // Transpose the adjacency matrix to compute the transpose matrix
        double[][] adjacentMatrixTranspose = new double[adjMatrix.length][adjMatrix.length];
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                adjacentMatrixTranspose[j][i] = adjMatrix[j][i];
            }
        }
        // Perform TrustRank iterations
        for (int iter = 0; iter < maxIter; iter++) {
            for (int i = 0; i < adjMatrix.length; i++) {
                // Initialize newRank for node i
                double newRank = 0.0;
                for (int j = 0; j < adjMatrix.length; j++) {
                    newRank += (1 - dampingFactor) *
                            inverseOutDegreeMatrix[i][i] *
                            adjacentMatrixTranspose[i][j] *
                            mapNodeToRank.get(j + 1);
                }
                // Add the damping factor multiplied by the initial seed value for node i
                newRank += dampingFactor * initialSeedVector.get(i + 1);
                // Update the rank of node i
                mapNodeToRank.replace(i + 1, newRank);
            }
            // This is for convergence curves and to hold the rank of each node corresponding to each iteration
            for (Integer key : mapNodeToRank.keySet()) {
                rankOfEachIteration[key - 1].add(mapNodeToRank.get(key));
            }
            // Take a copy of the ranks and apply Euclidean distance to check the convergence of PageRank given the epsilon
            if (iter == 1) {
                oldRanks.putAll(mapNodeToRank);
            }
            else if (iter > 1) {
                double sumOfSubtraction = 0d;
                for (int key : oldRanks.keySet()) {
                    double oldRank = oldRanks.get(key);
                    double newRank = mapNodeToRank.get(key);
                    sumOfSubtraction += Math.pow(oldRank - newRank, 2);
                }
                double euclideanDist = Math.sqrt(sumOfSubtraction);
                if (euclideanDist < epsilon) {
                    break;
                }
                oldRanks.putAll(mapNodeToRank);
            }
        }
        return mapNodeToRank;
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
                scores.put(node + 1, ranks.get(node));
            }
        }
        double totalSumOfRanks = scores.values().stream().mapToDouble(Double::doubleValue).sum();
        scores.keySet().forEach(key -> scores.replace(key, scores.get(key) / totalSumOfRanks));
        return scores;
    }

    /**
     * This method is used to count the number of out degree edges of the given node
     * @param node The node of interest
     * @return The number of out links
     */
    private int getNumOfOutEdges(int node) {
        int counter = 0;
        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[node][i] > 0) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * This method is used to retrieve the rank of each node at each iteration which is vital for convergence curves
     * @return All the rank of each node at each iteration
     */
    public ArrayList<Double>[] getRankOfEachIteration() {
        return rankOfEachIteration;
    }
}

