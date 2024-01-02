package EvolutionaryOptimizers.org;

import FitnessFunction.org.FitnessFunction;

import java.util.*;

/**
 * This class is used to apply Defferential Evolution (similar to Genetic Algorithm) algorithm to find top-k paths between A and B
 */
public class GeneticAlgorithm extends FitnessFunction {
    private final double[][] AdjMat;
    private final List<List<Integer>> population;
    private final double mutationRate;
    private final double crossOverProb;
    private final double selectionProportion;
    private final int maxIter;
    private List<List<Integer>> lastGeneration;
    private List<Integer> bestCurrent;
    private boolean flag;
    private final double[] convergenceCurve;

    /**
     * This contructor is used to initialize several parameters which are required for the DE algorithm
     * @param adjMat The adjacency matrix that represents the attack-defence graph
     * @param population The set of feasible solutions (paths)
     * @param mutationRate The ratio of applying mutation or not
     * @param crossOverProb The ratio of applying crossover or not
     * @param selectionProportion The ratio of how money solutions should be selected and moved to next generation
     * @param maxIter The number of allowed generations
     */
    public GeneticAlgorithm(double[][] adjMat, double[] assetLosses, List<List<Integer>> population, double mutationRate, double crossOverProb, double selectionProportion, int maxIter) {
        super(adjMat, assetLosses);
        this.AdjMat = adjMat;
        if (population == null) {
            throw new IllegalArgumentException("The population is null");
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
        this.population = new ArrayList<>();
        this.population.addAll(population);
        this.mutationRate = mutationRate;
        this.crossOverProb = crossOverProb;
        this.selectionProportion = selectionProportion;
        this.maxIter = maxIter;
        this.lastGeneration = new ArrayList<>();
        this.flag = false;
        this.convergenceCurve = new double[maxIter];
    }

    /**
     * This method is used to start the optimization process of DE
     */
    public void StartOptimization() {
        for (int iter = 0; iter < maxIter; iter++) {
            RandomSelection_Tournament(population);
            for (int i = 0; i < population.size(); i++) {
                List<Integer> chromosome1 = new ArrayList<>(population.get(new Random().nextInt(population.size())));
                List<Integer> chromosome2 = new ArrayList<>(population.get(new Random().nextInt(population.size())));
                Crossover(chromosome1, chromosome2);
                population.set(i, Mutation(population.get(i)));
            }
            convergenceCurve [iter] = super.evaluateAllSolution(population);
        }
        flag = true;
    }

    /**
     * This method is used to apply mutation
     * @param solution The solution to be muted
     * @return The muted solution
     */
    private List<Integer> Mutation(List<Integer> solution) {
        if (solution == null) {
            throw new IllegalArgumentException("The fitnes scores holder is empty");
        }
        List<Integer> s = new ArrayList<>(solution);
        for (int i = 1; i < s.size() - 1; i++) {
            if (new Random().nextDouble() < mutationRate) {
                List<Integer> potentialsGens = new ArrayList<>();
                for (int j = 0; j < AdjMat[0].length; j++) {
                    if (AdjMat[s.get(i) - 1][j] > 0 && j + 1 != s.get(0) && j + 1 != s.get(s.size() - 1)) {
                        potentialsGens.add(j + 1);
                    }
                }
                for (int gene : potentialsGens) {
                    if (!s.contains(gene)) {
                        if (AdjMat[s.get(i - 1) - 1][gene - 1] > 0 && AdjMat[s.get(i + 1) - 1][gene - 1] > 0)
                            s.set(i, gene);
                    }
                }
            }
        }
        List<Integer> sol = new ArrayList<>(super.evaluateSolution(s) > super.evaluateSolution(solution) ? s : solution);
        solution.clear();
        solution.addAll(sol);
        s.clear();
        return solution;
    }

    /**
     * This method is used to applay crossover on two given parents
     * @param solution1 The first parent
     * @param solution2 The second parent
     */
    private void Crossover(List<Integer> solution1, List<Integer> solution2) {
        if (solution1 == null || solution2 == null) {
            throw new IllegalArgumentException("One of the parents are null");
        }
        List<Integer> child1 = new LinkedList<>();
        List<Integer> child2 = new LinkedList<>();
        Label :
        for (int i = 1; i < solution1.size() - 1; i++) {
            for (int j = 1; j < solution2.size() - 1; j++) {
                if (!solution1.get(i).equals(solution2.get(j))) {
                    if (new Random().nextDouble() > crossOverProb) {
                        child1.addAll(solution1.subList(0, i));
                        child1.addAll(solution2.subList(j, solution2.size()));
                        child2.addAll(solution2.subList(0, j));
                        child2.addAll(solution1.subList(i, solution1.size()));
                        List<Integer> s1 = new ArrayList<>(super.evaluateSolution(child1) > super.evaluateSolution(solution1) ? child1 : solution1);
                        List<Integer> s2 = new ArrayList<>(super.evaluateSolution(child2) > super.evaluateSolution(solution2) ? child2 : solution2);
                        solution1.clear();
                        solution2.clear();
                        solution1.addAll(s1);
                        solution2.addAll(s2);
                        break Label;
                    }
                }
            }
        }
    }

    /**
     * This method is used to randomly select a subset of individuals based on modified RandomSelection_Tournament
     * @param population The currest generation of solutions
     */
    private void RandomSelection_Tournament(List<List<Integer>> population) {
        if (population == null) {
            throw new IllegalArgumentException("The population is null!");
        }
        List<Chromosome_FitnessScores> individualsScores = new ArrayList<>();
        for (List<Integer> integers : population) {
            individualsScores.add(new Chromosome_FitnessScores(integers, evaluateSolution(integers)));
        }
        individualsScores.sort(Comparator.comparing(Chromosome_FitnessScores::fitnessScore).reversed());
        int numOfIndividualsToBeSelected = (int) (population.size() * selectionProportion);
        for (int i = 0; i < numOfIndividualsToBeSelected; i++) {
            population.set(i, individualsScores.get(new Random().nextInt(numOfIndividualsToBeSelected)).individual);
        }
        for (int i = 0; i < numOfIndividualsToBeSelected; i++) {
            population.set(i, Mutation(individualsScores.get(i).individual));
        }
        bestCurrent = new ArrayList<>(individualsScores.get(0).individual);
        lastGeneration = population;
    }

    /**
     * This method is used to get the best solution in the corresponding iteration
     * @return The best current solution (Path)
     */
    public List<Integer> getBestCurrent() {
        if (!flag) {
            throw new IllegalStateException("You should start the optimization process first");
        }
        return bestCurrent;
    }

    /**
     * This method is used to retrieve the last reached generation
     * @return The last provided generation
     */
    public List<List<Integer>> getLastGeneration() {
        if (!flag) {
            throw new IllegalStateException("You should start the optimization process first");
        }
        return lastGeneration;
    }

    /**
     * This method is used to retrieve the convergence curve of DE
     * @return The convergence curve
     */
    public double[] getConvergenceCurve() {
        if (!flag) {
            throw new IllegalStateException("You should start the optimization process first");
        }
        return convergenceCurve;
    }

    /**
     * This record is used as a container of an individual solution with its fitness score
     */
    record Chromosome_FitnessScores(List<Integer> individual, double fitnessScore) { }
}
