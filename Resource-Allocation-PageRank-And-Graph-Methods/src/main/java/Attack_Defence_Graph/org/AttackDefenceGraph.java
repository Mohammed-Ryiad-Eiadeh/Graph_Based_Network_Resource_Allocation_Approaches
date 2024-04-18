package Attack_Defence_Graph.org;

import java.util.*;
import java.util.stream.IntStream;

/**
 * This class is used to encode, represent the attack-defence-graph and generate a set of paths from some entry node (attacker) to some node (asset)
 */
public class AttackDefenceGraph {
    private final int[] nodes;
    private final double[][] adjMatrix;
    private static final int noNewNode = -1;

    /**
     * This instructor is used to initialize nodes and the adjacency matrix of the input graph
     * @param adjMatrix It is referred to the matrix representation of the given attack-defence graph
     */
    public AttackDefenceGraph(double[][] adjMatrix) {
        if (adjMatrix == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        this.nodes = IntStream.rangeClosed(1, adjMatrix[0].length).toArray();
        this.adjMatrix = adjMatrix;
    }

    /**
     * This method is used to encode graph_based adjacency matrix as a hashmap (keys are nodes and values are connections)
     * @return The nodes and their connections
     */
    public HashMap<Integer, List<Integer>> graphRepresenter() {
        HashMap<Integer, List<Integer>> nodeConnections = new HashMap<>();
        for (int rIndex = 0; rIndex < nodes.length; rIndex++) {
            List<Integer> set = new ArrayList<>();
            for (int cIndex = 0; cIndex < adjMatrix[rIndex].length; cIndex++) {
                if (adjMatrix[rIndex][cIndex] > 0)
                    set.add(nodes[cIndex]);
            }
            nodeConnections.put(nodes[rIndex], set);
        }
        return nodeConnections;
    }

    /**
     * This method is used to generate a set of solutions (paths from A to B)
     * @param start The entry node for the attacker
     * @param end The asset node to attack
     * @param nodeConnections The relations between nodes
     * @return A set of solutions (paths from A to B) without visiting any intermediate node more than once
     */
    private List<Integer> generateSolution(int start, int end, Map<Integer, List<Integer>> nodeConnections) {
        if (adjMatrix[0].length < end) {
            throw new IllegalStateException("No node matches the end node");
        }
        List<Integer> solution = new ArrayList<>() {{
            add(start);
        }};
        while (solution.get(solution.size() - 1) != end) {
            Integer currentNode = solution.get(solution.size() - 1);
            Integer newNode = getNewNode(solution, nodeConnections.get(currentNode));
            if (newNode == noNewNode) {
                nodeConnections.remove(currentNode); // current node is dead one here
                // If there is no path between the current entry node and the asset node, then return empty list (indicate no solution)
                if (solution.size() <= 1) {
                    solution.clear();
                    break;
                }
                solution.remove(currentNode);
                for (int i = start; i < end; i++) {
                    if (nodeConnections.containsKey(i))
                        nodeConnections.get(i).remove(currentNode);
                }
            }
            else {
                solution.add(newNode);
            }
        }
        return solution;
    }

    /**
     * This methode is used to ensure the feasibility of solutions
     * @param solution The path from A to B
     * @param connections The relations between all nodes in the input graph
     * @return A feasible path with no redundent node as it already visited
     */
    private Integer getNewNode(List<Integer> solution, List<Integer> connections) {
        if (connections == null)
            return noNewNode;
        else {
            connections.removeAll(solution);
            if (connections.isEmpty()) {
                return noNewNode;
            }
            else{
                return connections.remove(new Random().nextInt(connections.size()));
            }
        }
    }

    /**
     * This method is used as the function that developer calls to generate a set of feasible solution
     * @param start The start node (attacker entry)
     * @param end The end node (targetted asset)
     * @param numOfSolutions The Number of paths to be generated
     * @return A set of feasible solutions
     */
    public List<List<Integer>> initialPopulation(int start, int end, int numOfSolutions) {
        if (start < 1) {
            throw new IllegalArgumentException("The attackers' entry node must be positive number starting from 1 S.t: 1, 2, 3, 4, ..., V");
        }
        if (end < 1) {
            throw new IllegalArgumentException("The sink node or the asset node must be positive number starting from 1 S.t: 1, 2, 3, 4, ..., V");
        }
        if (start > nodes.length) {
            throw new IllegalArgumentException("The attackers' entry node must not be above the last node we have in our system");
        }
        if (end > nodes.length) {
            throw new IllegalArgumentException("The sink node or the asset node must not be above the last node we have in our system");
        }
        if (numOfSolutions < 1) {
            throw new IllegalArgumentException("The number of initial solutions must be positive number and preferable higher than 10");
        }
        List<List<Integer>> setSolutions = new ArrayList<>();
        for (int i = 0; i < numOfSolutions; i++) {
            setSolutions.add(this.generateSolution(start, end, this.graphRepresenter()));
        }
        return setSolutions;
    }
}