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
| HG1 [15] | 7 | 10 | 2 | Bidirectional |
| HG2 [15] | 15 | 22 | 5 | Bidirectional |
| ABSNP [16] | 17 | 122 | 6 | Bidirectional |
| ASFS3 [16] | 27 | 163 | 9 | Bidirectional |
| ASS2009 [16] | 31 | 211 | 9 | Bidirectional |
| AWS03 [16] | 42 | 152 | 15 | Bidirectional |

Note: all of these datasets are stored in the project directory and is called dynamically so no need to set up their paths.


