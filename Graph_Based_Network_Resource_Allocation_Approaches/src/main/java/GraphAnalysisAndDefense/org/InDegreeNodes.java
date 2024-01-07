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
    private final double[][] adjMat;
    private final double[] assets;
    private final HashMap<Integer, ArrayList<Integer>> mapOfAssetsAndTheirInDegreeNode;

    /**
     * This constructer is used to initiate an object that is used to retrieve edges covered by First Level Degree Node.
     * @param AdjMat The adjacency matrix of the given graph problem.
     * @param assetLossVector The assets' loss vector.
     */
    public InDegreeNodes(double[][] AdjMat, double[] assetLossVector) {
        if (AdjMat == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (assetLossVector == null) {
            throw new IllegalArgumentException("The assets loss vector is null");
        }
        adjMat = new double[AdjMat.length][AdjMat[0].length];
        int i = 0;
        for (double[] row : AdjMat) {
            System.arraycopy(row, 0, adjMat[i], 0, row.length);
            i++;
        }
        assets = assetLossVector;
        mapOfAssetsAndTheirInDegreeNode = new HashMap<>();
    }

    /**
     * This method is used to compute the in-degree and out-degree nodes
     */
    private ArrayList<Integer> getDirectedNeighbors(int assetId) {
        HashMap<Integer, ArrayList<Integer>> firstLevelDegreeNodes = new HashMap<>();
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        for (int i = 0 ; i < adjMat.length; i++) {
            // get the parents of the asset
            if (adjMat[i][assetId - 1] > 0) {
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
        for (int id = 0; id < assets.length; id++) {
            if (assets[id] > 0) {
                if (!getDirectedNeighbors(id + 1).isEmpty()) {
                    mapOfAssetsAndTheirInDegreeNode.put(id + 1, getDirectedNeighbors(id + 1));
                }
            }
        }
        return mapOfAssetsAndTheirInDegreeNode;
    }
}

