package GraphAnalysisAndDefense.org;

/**
 * This class is used to apply the behavioral defender according to the prospect theory.
 * <pre>
 *     Behavioral and Game-Theoretic Security Investments in Interdependent Systems Modeled by Attack Graphs, Mustafa Abdallah et al, 2020
 * </pre>
 */
public class BehavioralDefender {
    private final double[][] adjMat;
    private final float alpha;

    /**
     * This constructer is used to creat an object of the behavioral defender
     * @param AdjMatrix The adjacensy matrix of the attack graph
     * @param alpha The factor is nonlinear perception of probabilities in the probability weighting function
     */
    public BehavioralDefender(double[][] AdjMatrix, float alpha) {
        if (AdjMatrix == null) {
            throw new IllegalArgumentException("The weights vector is null");
        }
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha must be relied in 0 and 1");
        }
        adjMat = new double[AdjMatrix.length][AdjMatrix[0].length];
        int i = 0;
        for (double[] row : AdjMatrix) {
            System.arraycopy(row, 0, adjMat[i], 0, row.length);
            i++;
        }
        this.alpha = alpha;
    }

    /**
     * This method is used to apply the resource allocation based on the prospect theory
     * @return The weightening matrix according to the behavioral defenders
     */
    public double[][] applyBehavioralDefendingForResourceAllocation() {
        double[][] weightenMat = new double[adjMat.length][adjMat[0].length];
        for (int i = 0; i < adjMat.length; i++) {
            for (int j = 0; j < adjMat[0].length; j++) {
                if (adjMat[i][j] > 0) {
                    weightenMat[i][j] = Math.exp(-Math.pow(-Math.log(Math.exp(-adjMat[i][j])), alpha));
                }
            }
        }
        return weightenMat;
    }
}

    