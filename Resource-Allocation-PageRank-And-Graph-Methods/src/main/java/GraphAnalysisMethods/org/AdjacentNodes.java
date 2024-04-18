package GraphAnalysisMethods.org;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to capture the edges of the first order neighbor nodes of the distinction node.
 * <pre>
 *     Graph Degree Linkage: Agglomerate Clustering on a Directed Graph, Wei Zhang et al, 2012
 * </pre>
 */
public class AdjacentNodes {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;
    private final HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> mapOfAssetsAndTheirFirstLevelDegreeNode;

    /**
     * This constructor is used to initiate an object that is used to retrieve edges covered by First Level Degree Node.
     * @param AdjMat The adjacency matrix of the given graph problem.
     * @param assetLossVector The assets' loss vector.
     */
    public AdjacentNodes(double[][] AdjMat, double[] assetLossVector) {
        if (AdjMat == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (assetLossVector == null) {
            throw new IllegalArgumentException("The assets loss vector is null");
        }
        adjMatrix = new double[AdjMat.length][AdjMat[0].length];
        int i = 0;
        for (double[] row : AdjMat) {
            System.arraycopy(row, 0, adjMatrix[i], 0, row.length);
            i++;
        }
        assetLossVec = assetLossVector;
        mapOfAssetsAndTheirFirstLevelDegreeNode = new HashMap<>();
    }

    /**
     * This method is used to compute the in-degree and out-degree nodes
     */
    private HashMap<Integer, ArrayList<Integer>> getDirectedNeighbors(int assetId) {
        HashMap<Integer, ArrayList<Integer>> firstLevelDegreeNodes = new HashMap<>();
        ArrayList<Integer> childrenNodes = new ArrayList<>();
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        for (int i = 0 ; i < adjMatrix.length; i++) {
            // get the children of the asset
            if (adjMatrix[assetId - 1][i] > 0) {
                childrenNodes.add(i + 1);
            }
            // get the parents of the asset
            if (adjMatrix[i][assetId - 1] > 0) {
                parentsNodes.add(i + 1);
            }
        }
        firstLevelDegreeNodes.put(1, parentsNodes);
        firstLevelDegreeNodes.put(0, childrenNodes);
        return firstLevelDegreeNodes;
    }

    /**
     * This method is used to retrieve the covered edges by MRF
     * @return The map --> maps --> lists as the asset nodes --> type of neighbors --> nodes
     */
    public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> retrieveNodeOfFirstLevelDegree() {
        for (int id = 0; id < assetLossVec.length; id++) {
            if (assetLossVec[id] > 0) {
                if (!getDirectedNeighbors(id + 1).isEmpty()) {
                    mapOfAssetsAndTheirFirstLevelDegreeNode.put(id + 1, getDirectedNeighbors(id + 1));
                }
            }
        }
        return mapOfAssetsAndTheirFirstLevelDegreeNode;
    }
}
