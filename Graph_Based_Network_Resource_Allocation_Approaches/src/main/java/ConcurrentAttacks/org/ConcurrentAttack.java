package ConcurrentAttacks.org;

import Attack_Defence_Graph.org.AttackDefenceGraph;
import EvolutionaryOptimizers.org.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class is used to generate concurrent attacks to the network
 */
public class ConcurrentAttack {
    private final double[][] adjMat;
    private final double[] assetLossVectors;
    private final int popSize;
    private final double mutationRate;
    private final double crossOverProb;
    private final double selectionProportion;
    private final int maxIter;

    /**
     * This is the default constructor of the concurrent attack generation
     * @param adjMat AdjMatrix The adjacensy matrix of the attack graph
     * @param assetLossVectors The vector of the loss corresponding each asset
     * @param mutationRate The ratio of applying mutation or not
     * @param crossOverProb The ratio of applying crossover or not
     * @param selectionProportion The ratio of how money solutions should be selected and moved to next generation
     * @param maxIter The number of allowed generations
     */
    public ConcurrentAttack(double[][] adjMat, double[] assetLossVectors, int populationSize, double mutationRate, double crossOverProb, double selectionProportion, int maxIter) {
        if (adjMat == null) {
            throw new IllegalArgumentException("The matrix is null!");
        }
        if (assetLossVectors == null) {
            throw new IllegalArgumentException("The asset loss vector is null");
        }
        if (populationSize <= 0) {
            throw new IllegalArgumentException("The population size must be positive integer");
        }
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("The mutation rate must be relied in [0,1]");
        }
        if (crossOverProb < 0 || crossOverProb > 1) {
            throw new IllegalArgumentException("The cross over probability must be relied in [0,1]");
        }
        if (selectionProportion < 0 || selectionProportion > 1) {
            throw new IllegalArgumentException("The selection proportionate must be relied in [0,1]");
        }
        if (maxIter < 0) {
            throw new IllegalArgumentException("The max iteration must be positive integer");
        }
        this.adjMat = adjMat;
        this.assetLossVectors = assetLossVectors;
        this.popSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossOverProb = crossOverProb;
        this.selectionProportion = selectionProportion;
        this.maxIter = maxIter;
    }

    /**
     * This method is used to run the genetic algorithm to generate all potential attacks
     * @param entry The attackers' entry node
     * @param asset The asset that the attacker is targetting
     * @return Return a set of feasible paths
     */
    private List<Integer> getPotentialAttackPath(int entry, int asset) {
        // Costruct cyber graph object to generate a set of paths from the start to the end node and evantually get the best one
        AttackDefenceGraph cyberGraph = new AttackDefenceGraph(adjMat);
        var initialSetOfAttackPaths = cyberGraph.initialPopulation(entry, asset, popSize);

        // Construct a GA object to compute the most potential attack path for the given entry and asset nodes
        GeneticAlgorithm engin = new GeneticAlgorithm(adjMat,
                assetLossVectors,
                initialSetOfAttackPaths,
                mutationRate,
                crossOverProb,
                selectionProportion,
                maxIter);
        engin.StartOptimization();

        // Getting the most potential attack path
        List<Integer> bestPath = engin.getBestCurrent();
        return bestPath;
    }

    /**
     * This method is used to retrieve all feasible paths that are generated between every entry node to each asset node
     * @return Return a map of assets and all corresponding paths to it
     */
    public HashMap<Integer, ArrayList<List<Integer>>> getPaths() {
        HashMap<Integer, ArrayList<List<Integer>>> map = new HashMap<>();
        ArrayList<Integer> allAssets = new ArrayList<>();
        ArrayList<Integer> allEntries = new ArrayList<>();
        for (int i = 0; i < assetLossVectors.length; i++) {
            if (assetLossVectors[i] > 0) {
                allAssets.add(i + 1);
                map.put(i + 1, new ArrayList<>());
            }
            else {
                allEntries.add(i + 1);
            }
        }
        IntStream.rangeClosed(0, allEntries.size() - 1).parallel().forEach( i -> {
            for (Integer allAsset : allAssets) {
                List<Integer> path = getPotentialAttackPath(allEntries.get(i), allAsset);
                if (!path.isEmpty() && path.size() > 2) {
                    synchronized (map) {
                        map.get(allAsset).add(path);
                    }
                }
            }
        });
        map.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        return map;
    }
}
