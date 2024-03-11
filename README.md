# PR-DRA: PageRank-based Defense Resource Allocation Methods for Securing Interdependent Systems Modeled by Attack Graphs

# Abstract

Interdependent systems confront rapidly growing cybersecurity threats. This paper delves into the realm of security decision-making within these complex interdependent systems. We design a resource allocation framework to improve the security of interdependent systems managed by a single rational (logical) defender. Our framework models these systems and their potential attack vulnerabilities using the notion of cyber attack graphs. We propose three defense mechanisms, incorporating a popular network analysis algorithm called PageRank which is used to identify the importance of different critical assets in the system. These mechanisms stem from existing graph theories widely used in graphical models (including Adjacent Nodes, In-degree Nodes, and Markov Blanket). We adopt two versions of the PageRank algorithm to extract useful information about the attack graphs we use. Our approaches show low sensitivity to the number of concurrent attacks on interdependent systems. We evaluate our decision-making framework via ten attack graphs, which include multiple real-world interdependent systems. We quantify the level of security improvement under our defense methods compared to two well-known resource allocation algorithms and other proposed approaches. Our proposed framework leads to better resource allocations compared to these algorithms in most test cases. According to our results and the Wilcoxon and Friedman statistical tests, our approach’s outcomes are superior. Our framework enhances security decision-making under various circumstances. We release the full implementation of our framework for the research community.

# Framework

The figure depicts an attack graph consisting of entry nodes (used by attackers) and asset nodes (critical for defenders). Interconnections between these nodes represent security vulnerabilities. The Genetic Algorithm (GA) generates potential attack paths based on this graph. GA employs a path encoding criterion to prevent revisiting nodes and encodes sink nodes. A multi-objective fitness function evaluates feasible attack paths, considering asset loss. Defenders allocate security budget across attack paths, guided by graph theory concepts (in-degree nodes, adjacent nodes, Markov Blanket). The PageRank algorithm informs asset importance, aiding proportional security investments. Finally, relative reduction in expected security cost is computed for performance analysis.

![Screenshot (425)](https://github.com/Mohammed-Ryiad-Eiadeh/Graph_Based_Network_Resource_Allocation_Approaches/assets/93108547/dad953a0-0643-4e6c-aea9-044d4312383d)

# Fitness Function

$F_2(P) = \max_{P \in P_m} \big(\exp\big(-\sum_{(v_i,v_j)\in P} {x_{i,j}}\big) + Wf\sum_{v_m\in P} L_m\big).$
   
   $P$ is the given attack path.

   $P_m$ is a set of attack paths.

   $v_i,v_j$ are the nodes in $P$.

   $L_m$ is the loss corresponding to node $v_m$

   $Wf$ is the weight factor lies in [0,1]
   
This function accounts for the total asset loss that the system will lose if the attack is occured successfully.

# Our Contribution

1) We introduce a method for \textit{security resource allocation} for a defender of interdependent systems, where the assets she is protecting are interconnected. We illustrate the influence of \name on decision-making processes concerning system security, and we measure the percentage of enhancement that can be credited to our security resource allocation method.

2) We suggest two variants of the PageRank algorithm for improving resource allocation decisions. One variant relies on the sum of the edge weights of the outgoing nodes, while the other depends on the count of the outgoing edges. These algorithms are used under three defense strategies based on adjacent nodes, in-degree nodes, and the Markov blanket. They are designed to counter various attack models that pose threats to the security of interdependent systems.

3) We utilize an evolutionary computation algorithm, the Genetic Algorithm (GA), to identify the most likely attack paths from the attacker’s entry nodes to the critical targeted assets, ensuring a reasonable time complexity. In essence, we employ GA to determine the top-1 attack path between each potential entry node and corresponding critical asset, simulating a real-world scenario indicative of concurrent attacks. We introduce a multi-objective fitness function for this algorithm, which considers the total estimated financial loss associated with a specific attack path.

4) We assess our proposed defense strategies for interdependent systems using ten attack graphs. We benchmark the performance of two resource allocation methods on these graphs, namely ‘defense in depth’ and ‘the behavioral defender'.

5) Our entire framework is implemented using the Java language, adhering to the principles of object-oriented programming. The implementation incorporates reliable and well-tested libraries for executing graph-theoretic algorithms. The efficiency of our implementation is largely due to the use of several data structures.

# Datasets We Used In Our Work

For our assessment, we used ten distinct attack graphs, each symbolizing a different interdependent system and network structure. We divided these datasets into three groups. The first group contains four attack graphs from real-world interconnected systems, namely DER.1, SCADA, E-commerce, and VOIP. Signifies an attack step, and we consider every edge to be directional. The second group consists of two graph typologies, referred to as HG1 and HG2, which were introduced in earlier studies. The third group includes four datasets from a renowned interactive scientific graph data repository, named aves-sparrow-social-2009 (ASC2009), aves-sparrowlyon-flock-season3 (ASFS3), aves-weaver-social-03 (AWS03), and aves-barn-swallow-non-physical (ABSNP). This repository is a network data collection produced by top-tier US niversities.

| System | # Nodes | # Edges | # Critical Assets | Graph Type |
| --- | --- | --- | --- | --- |
| SCADA [12] | 13 | 20 | 6 | Directed |
| DER.1 [13] | 22 | 32 | 6 | Directed |
| E-Commerce [14] | 20 | 32 | 4 | Directed |
| VOIP [14] | 22 | 35 | 4 | Directed |
| HG1 [15] | 7 | 10 | 2 | Directed |
| HG2 [15] | 15 | 22 | 5 | Directed |
| ABSNP [16] | 17 | 122 | 6 | Directed |
| ASFS3 [16] | 27 | 163 | 9 | Directed |
| ASS2009 [16] | 31 | 211 | 9 | Directed |
| AWS03 [16] | 42 | 152 | 15 | Directed |

Note: all of these datasets are stored in the project directory and is called dynamically so no need to set up their paths.

# Parameter Configuration of Our Experiments

We begin by detailing the primary hyperparameters utilized in various components of our framework. The parameters for the GA were selected as follows: maximum iterations ($M=500$), population size which refers to a set of potential attack paths ($N=2000$), mating probability ($m_p=0.2$), mutation rate ($m_r=0.2$), and weight factor ($Wf=0.001$). The defender's security security budgets is set at $S=5$ (unless otherwise stated for specific experiments), and the maximum iterations ($PR_{iter}$) for the PR algorithm is set at 100, with epsilon ($\epsilon$) set at 0.0001. We underscore that the benefits of our suggested defense (resource allocation) strategies are applicable for any given security budget. For the behavioral defender baseline~\cite{Abdallah2020}, we have set the behavioral level ($a$) at 0.5.

# Comparison of AARA-PR and baseline systems on all datasets

The row "Measurements" show the relative difference of the expected cost $CR$ for all defense scenarios. The larger $CR$, the better the defense method with significance level equals 0.05 for the Friedman test.

Comparison of the proposed approaches and the two baseline algorithms over the used ten datasets under Friedman test with $SL=0.05$. Our proposed methods show superior performance. While ``PR (JGraphT) + In-Degree Nodes'' has the highest mean rank, yet ``PRV1 + In-Degree Nodes'' method is ranked first more often, making it a more reliable choice for diverse real-world datasets.

| Dataset / System                      | Defense in Depth~\cite{lippmann2006validating} | Behavioral Defender~\cite{Abdallah2020} | Adjacent Nodes | In Degree Nodes | MB Nodes | PR (JGraphT) + MB Nodes | PR (JGraphT) + Adjacent Nodes | PR (JGraphT) + In-Degree Nodes | PRV1 + MB Nodes | PRV1 + Adjacent Nodes | PRV1 + In-Degree Nodes |
|---------------------------------------|-----------------------------------------------|-----------------------------------------|----------------|-----------------|----------|--------------------------|--------------------------------|--------------------------------|-----------------|------------------------|-------------------------|
| SCADA [12]         | 22.12                                         | 22.12                                   | 29.70          | 36.53           | 24.95    | 28.51                    | 39.42                          | 44.49                          | 31.20           | 45.33                  | **49.61**               |
| DER.1 [13]         | 14.47                                         | 14.47                                   | 32.64          | 42.62           | 33.19    | 28.60                    | 41.39                          | 55.71                          | 29.76           | 43.16                  | **57.24**               |
| E-Commerce [14] | 14.47                                     | 14.47                                   | 42.62          | 42.62           | 42.62    | 36.58                    | **69.73**                      | **69.73**                      | 31.84           | 41.99                  | 41.99                   |
| VOIP [14]    | 13.31                                         | 13.31                                   | 46.47          | 46.47           | 46.47    | 40.63                    | **69.73**                      | **69.73**                      | 32.91           | 33.09                  | 46.75                   |
| HG1 [15]                  | 39.35                                         | 39.35                                   | 64.68          | 56.54           | 54.41    | 45.85                    | 54.12                          | 56.32                          | 69.73           | **77.50**              | 63.19                   |
| HG2 [15]                  | 20.33                                         | 20.33                                   | 32.18          | **42.62**       | 28.49    | 22.50                    | 27.59                          | 38.88                          | 19.13           | 21.89                  | 31.07                   |
| ABSNP [16]                | 1.50                                          | 1.50                                    | 2.13           | 3.93            | 2.78     | 4.84                     | 5.79                           | 5.79                           | 4.84            | **69.73**              | **69.73**               |
| ASFS3 [16]                | 0.73                                          | 0.64                                    | 0.96           | **69.73**       | 1.42     | 1.03                     | 1.62                           | 1.85                           | 0.78            | 1.32                   | 1.38                    |
| ASS2009 [16]              | 0.15                                          | 0.15                                    | 0.31           | 0.61            | 0.42     | 0.33                     | 0.44                           | 0.63                           | 0.64            | 0.76                   | **69.73**               |
| AWS03 [16]                | 1.85                                          | 1.85                                    | 3.24           | **69.73**       | 3.98     | 3.70                     | 5                              | 5.36                           | 3.05            | 3.80                   | 4.10                    |
| Rank First                            | 0                                             | 0                                       | 0              | 0               | 3        | 0                        | 0                              | 2                              | 2               | 2                      | **4**                   |
| Sum of Rank                           | 16.5                                          | 15.5                                    | 57             | 82              | 60       | 44.5                     | 78.5                           | **92.5**                        | 48.5            | 76                     | 89                      |
| Mean Rank                             | 1.65                                          | 1.55                                    | 5.70           | 8.20            | 6        | 4.45                     | 7.85                           | **9.25**                        | 4.85            | 7.60                   | 8.90                    |

