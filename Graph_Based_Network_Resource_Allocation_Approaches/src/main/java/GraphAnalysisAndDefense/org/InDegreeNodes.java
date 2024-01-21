package GraphAnalysisAndDefense.org;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to capture the edges of the distination nodes in degree nodes (parents).
 * <pre>
 *     Graph Degree Linkage: Agglomerative Clustering on a Directed Graph, Wei Zhang et al, 2012
 * </pre>
 */
public class InDegreeNodes {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;
    private final HashMap<Integer, ArrayList<Integer>> mapOfAssetsAndTheirInDegreeNode;

    /**
     * This constructer is used to initiate an object that is used to retrieve edges covered by First Level Degree Node.
     * @param adjMatrix The adjacency matrix of the given graph problem.
     * @param assetLossVector The assetLossVec' loss vector.
     */
    public InDegreeNodes(double[][] adjMatrix, double[] assetLossVector) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (assetLossVector == null) {
            throw new IllegalArgumentException("The assetLossVec loss vector is null");
        }
        this.adjMatrix = new double[adjMatrix.length][adjMatrix[0].length];
        int i = 0;
        for (double[] row : adjMatrix) {
            System.arraycopy(row, 0, this.adjMatrix[i], 0, row.length);
            i++;
        }
        assetLossVec = assetLossVector;
        mapOfAssetsAndTheirInDegreeNode = new HashMap<>();
    }

    /**
     * This method is used to compute the in-degree and out-degree nodes
     */
    private ArrayList<Integer> getDirectedNeighbors(int assetId) {
        HashMap<Integer, ArrayList<Integer>> firstLevelDegreeNodes = new HashMap<>();
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            // get the parents of the asset
            if (adjMatrix[i][assetId - 1] > 0) {
                parentsNodes.add(i + 1);
            }
        }
        return parentsNodes;
    }

    /**
     * This method is used to retrieve the covered edges by MRF
     * @return The map --> maps --> lists as the asset nodes --> type of neighbors --> nodes
     */
    public HashMap<Integer, ArrayList<Integer>> retrieveInDegreeNodes() {
        for (int id = 0; id < assetLossVec.length; id++) {
            if (assetLossVec[id] > 0) {
                if (!getDirectedNeighbors(id + 1).isEmpty()) {
                    mapOfAssetsAndTheirInDegreeNode.put(id + 1, getDirectedNeighbors(id + 1));
                }
            }
        }
        return mapOfAssetsAndTheirInDegreeNode;
    }
}

