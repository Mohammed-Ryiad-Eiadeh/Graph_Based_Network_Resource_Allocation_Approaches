package GraphAnalysisAndDefense.org;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to capture the edges covered by Markov blanket for each asset node.
 * <pre>
 *     Markov blankets in the brain, Inˆes Hipolito et al, 2021
 * </pre>
 */
public class MarkovBlanket {
    private final double[][] adjMatrix;
    private final double[] assetLossVec;
    private final HashMap<Integer, HashMap<String, ArrayList<Integer>>> mapOfAssetsAndTheirChildrenAndParents;

    /**
     * This constructer is used to initiate an object that is used to retrieve edges covered by First Level Degree Node.
     * @param adjMatrix The adjacency matrix of the given graph problem.
     * @param assetLossVector The assetLossVec' loss vector.
     */
    public MarkovBlanket(double[][] adjMatrix, double[] assetLossVector) {
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
        this.assetLossVec = new double[assetLossVector.length];
        System.arraycopy(assetLossVector, 0, assetLossVec, 0, assetLossVector.length);
        this.mapOfAssetsAndTheirChildrenAndParents = new HashMap<>();
    }

    /**
     * This method is used to compute the nodes captured by Markov blanket
     */
    private HashMap<String, ArrayList<Integer>> getDirectedNeighbors(int assetId) {
        HashMap<String , ArrayList<Integer>> firstLevelDegreeNodes = new HashMap<>();
        ArrayList<Integer> childrenNodes = new ArrayList<>();
        ArrayList<Integer> parentsNodes = new ArrayList<>();
        ArrayList<Integer> parentsOfChild = new ArrayList<>();
        for (int i = 0; i < adjMatrix.length; i++) {
            // get the children of the asset
            if (adjMatrix[assetId - 1][i] > 0) {
                childrenNodes.add(i + 1);
            }
            // get the parents of the asset
            if (adjMatrix[i][assetId - 1] > 0) {
                parentsNodes.add(i + 1);
            }
        }
        firstLevelDegreeNodes.put("Parents", parentsNodes);
        firstLevelDegreeNodes.put("Children", childrenNodes);
        return firstLevelDegreeNodes;
    }

    /**
     * This method is used to retrieve the parents of the given node (child)
     * @param child The child we want to detect its parents
     * @return Return the parent nodes of this child
     */
    private ArrayList<Integer> getParentsOfChildrean(int child) {
        ArrayList<Integer> parentsNodes = new ArrayList<>();
            for (int i = 0; i < adjMatrix.length; i++) {
                // get the parents of the asset
                if (adjMatrix[i][child - 1] > 0) {
                    parentsNodes.add(i + 1);
                }
            }
        return parentsNodes;
    }

    /**
     * This method is used to retrieve the covered edges by MB
     * @return The map --> maps --> lists as the asset nodes --> type of neighbors --> nodes
     */
    public HashMap<Integer, HashMap<String, ArrayList<Integer>>> retrieveNodeOfMarkovBlanket() {
        for (int id = 0; id < assetLossVec.length; id++) {
            if (assetLossVec[id] > 0) {
                mapOfAssetsAndTheirChildrenAndParents.put(id + 1, getDirectedNeighbors(id + 1));
            }
        }
        HashMap<Integer, ArrayList<Integer>> parentsOfChildren = getParentsOfChildren();
        for (Integer node : parentsOfChildren.keySet()) {
            HashMap<String, ArrayList<Integer>> map = mapOfAssetsAndTheirChildrenAndParents.get(node);
            if (map != null) {
                map.put("ParentsOfChildren", parentsOfChildren.get(node));
            }
        }
        mapOfAssetsAndTheirChildrenAndParents.values().forEach(map -> map.values().removeIf(ArrayList::isEmpty));
        return mapOfAssetsAndTheirChildrenAndParents;
    }

    /**
     * This method is used to retrieve a map of the child and its parents
     * @return Return the map of child and its parents
     */
    private HashMap<Integer, ArrayList<Integer>> getParentsOfChildren() {
        HashMap<Integer, ArrayList<Integer>> mapOfChildrenAndTheirParents = new HashMap<>();
        for (var v : mapOfAssetsAndTheirChildrenAndParents.entrySet()) {
            ArrayList<Integer> children = v.getValue().get("Children");
            for (Integer val : children) {
                mapOfChildrenAndTheirParents.put(val, getParentsOfChildrean(val));
            }
        }
        return mapOfChildrenAndTheirParents;
    }
}