package GraphAnalysisAndDefense.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to capture the edges covered by Markov blanket for each asset node.
 * <pre>
 *     Markov blankets in the brain, Inˆes Hipolito et al, 2021
 * </pre>
 */
public class MarkovBlanket {
    private final double[][] adjMat;
    private final double[] assets;
    private final HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> mapOfAssetsAndTheirFirstLevelDegreeNode;
    private final HashMap<Integer, ArrayList<Integer>> mapParentsOfChild;

    /**
     * This constructer is used to initiate an object that is used to retrieve edges covered by First Level Degree Node.
     * @param AdjMat The adjacency matrix of the given graph problem.
     * @param assetLossVector The assets' loss vector.
     */
    public MarkovBlanket(double[][] AdjMat, double[] assetLossVector) {
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
        assets = new double[assetLossVector.length];
        System.arraycopy(assetLossVector, 0, assets, 0, assetLossVector.length);
        mapOfAssetsAndTheirFirstLevelDegreeNode = new HashMap<>();
        mapParentsOfChild = new HashMap<>();
    }

    /**
     * This method is used to compute the nodes captured by Markov blanket
     */
    private HashMap<Integer, ArrayList<Integer>> getDirectedNeighbors(int assetId) {
        HashMap<Integer, ArrayList<Integer>> firstLevelDegreeNodes = new HashMap<>();
        ArrayList<Integer> childrenNodes = new ArrayList<>();
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        ArrayList<Integer> parentsOfChild = new ArrayList<>();
        for (int i = 0 ; i < adjMat.length; i++) {
            // get the children of the asset
            if (adjMat[assetId - 1][i] > 0) {
                childrenNodes.add(i + 1);
                for (int ii = 0; ii < adjMat.length; ii++) {
                    if (adjMat[ii][i] > 0 && ii != assetId - 1) {
                        parentsOfChild.add(ii + 1);
                    }
                }
                mapParentsOfChild.put(i + 1, parentsOfChild);
            }
            // get the parents of the asset
            if (adjMat[i][assetId - 1] > 0) {
                parentsNodes.add(i + 1);
            }
        }
        firstLevelDegreeNodes.put(1, parentsNodes);
        firstLevelDegreeNodes.put(0, childrenNodes);
        return firstLevelDegreeNodes;
    }

    /**
     * This method is used to retrieve the covered edges by MB
     * @return The map --> maps --> lists as the asset nodes --> type of neighbors --> nodes
     */
    public HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> retrieveNodeOfMarkovBlanket() {
        for (int id = 0; id < assets.length; id++) {
            if (assets[id] > 0) {
                mapOfAssetsAndTheirFirstLevelDegreeNode.put(id + 1, getDirectedNeighbors(id + 1));
            }
        }
        for (Map.Entry<Integer, ArrayList<Integer>> entry : mapParentsOfChild.entrySet()) {
            if (mapOfAssetsAndTheirFirstLevelDegreeNode.containsKey(entry.getKey()))
                // here two is to indicate that these nodes are parent to the node (key)
                mapOfAssetsAndTheirFirstLevelDegreeNode.get(entry.getKey()).put(2, entry.getValue());
        }
        return mapOfAssetsAndTheirFirstLevelDegreeNode;
    }
}
