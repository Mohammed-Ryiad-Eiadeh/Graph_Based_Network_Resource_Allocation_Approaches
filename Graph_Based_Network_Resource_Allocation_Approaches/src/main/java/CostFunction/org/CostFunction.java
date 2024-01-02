package CostFunction.org;

import java.util.List;

/**
 * This class is used to calculate the total cost that she loses if according to the given attck path
 */
public class CostFunction {
    private final double[][] adjMatrix;
    private final double[] assetsLossVectors;

    /**
     * This is the default constructor of the CostFunction class
     * @param adjMatrix The graph representation as adjacensy matrix
     * @param assetLoss The vector of the loss corresponding each asset
     */
    public CostFunction(double[][] adjMatrix, double[] assetLoss) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (assetLoss == null) {
            throw new IllegalArgumentException("The asset loss vector is null");
        }
        this.adjMatrix = adjMatrix;
        this.assetsLossVectors = assetLoss;
    }

    /**
     * This method is used to compute the cost
     * @param solution The attck path
     * @return The total cost
     */
    public double computeCost(List<Integer> solution) {
        if (solution == null) {
            throw new IllegalArgumentException("The solution is null!");
        }
        double cost = 0d;
        for (int i = 0; i < solution.size() - 1; i++) {
            int from = solution.get(i);
            int to = solution.get(i + 1);
            cost += Math.exp(-adjMatrix[from - 1][to - 1]) * assetsLossVectors[to - 1];
        }
        return cost;
    }
}
