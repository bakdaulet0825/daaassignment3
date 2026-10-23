
---

# Assignment 3: Graph Algorithms — Technical Report

## 1. Introduction & Architecture

This project implements and evaluates fundamental graph algorithms in Java, focusing on strict architectural modularity, performance benchmarking, and correctness verification using JUnit 5. The codebase is organized into the following packages under `com.daaassignment3`:

* **`com.daaassignment3.graph`**: Core graph representation using adjacency lists and edge lists, supporting both directed and undirected graphs with weight parsing from input files.


* **`com.daaassignment3.dsu`**: Disjoint Set Union (DSU) data structure equipped with path compression and union-by-rank heuristics.


* **`com.daaassignment3.mst`**: Minimum Spanning Tree (MST) algorithms, featuring Kruskal's and Prim's algorithms.


* **`com.daaassignment3.scc`**: Graph traversal and connectivity modules, implementing Kosaraju's algorithm (using an iterative DFS to ensure stack safety up to $100\,000$ vertices) and Kahn's topological sort.


* **`com.daaassignment3.paths`**: Directed Acyclic Graph (DAG) shortest path computation in linear time $O(V + E)$.


* **`com.daaassignment3.benchmark`**: Automated graph generator using a fixed seed (`Random(42)`) and performance benchmarking engine that writes metrics to `results/results.csv`.



---

## 2. Complexity Analysis Table

| Algorithm | Time Complexity (Sparse) | Time Complexity (Dense) | Space Complexity | Key Invariant / Data Structure |
| --- | --- | --- | --- | --- |
| **Kruskal (MST)** | $O(m \log m)$ | $O(m \log m)$ | $O(n + m)$ | Edge sorting + DSU with path compression |
| **Prim (MST)** | $O((n + m) \log n)$ | $O(n^2)$ | $O(n + m)$ | Binary Min-Heap / Priority Queue |
| **Kosaraju (SCC)** | $O(n + m)$ | $O(n + m)$ | $O(n + m)$ | Two-pass iterative DFS + Finish stack |
| **Kahn (Topo Sort)** | $O(n + m)$ | $O(n + m)$ | $O(n + m)$ | In-degree array + Zero-in-degree Queue |
| **DAG Shortest Paths** | $O(n + m)$ | $O(n + m)$ | $O(n + m)$ | Topological ordering relaxation |

---

## 3. Empirical Analysis & Counters Verification

To validate theoretical bounds independently of machine load, the benchmarking suite tracks primitive operations (`edge_scans`, `comparisons`, `max_depth`) rather than raw execution time.

* **Linear Work Validation (`edge_scans / m`)**:
* **Kosaraju's algorithm** consistently performs exactly $2m$ edge scans across all tested graph sizes and densities, confirming precisely two passes over the edge set (forward and transpose graphs).
* **Kahn's algorithm** and **DAG Shortest Paths** strictly execute $m$ edge scans ($1.0 \times m$), verifying single-pass linear processing over DAG edges.
* **Prim's algorithm** scans $2m$ edges due to undirected bidirectional adjacency traversal.


* **Stack Safety**: Iterative DFS implementation in Kosaraju successfully handled extreme graphs up to $N = 100\,000$, keeping the maximum stack depth within safe operational limits ($\max\_depth \approx 91\,857$), preventing `StackOverflowError`.

---

## 4. Theoretical Proofs

### A. Cut Property (Minimum Spanning Tree)

**Theorem**: Let $G = (V, E)$ be a connected, undirected graph with real-valued edge weights. For any cut $(S, V \setminus S)$ in $G$, if an edge $e = (u, v)$ has the minimum weight among all cross-edges connecting $S$ and $V \setminus S$, then $e$ belongs to *every* MST of $G$.

**Proof**: Assume by contradiction that there exists an MST $T$ that does not contain $e$. Since $T$ spans the graph, adding $e$ to $T$ must create a unique simple cycle. This cycle contains at least one other cross-edge $e' = (x, y)$ across the cut $(S, V \setminus S)$ because the path must cross the cut an odd number of times. Removing $e'$ and adding $e$ forms a new spanning tree $T' = T \cup \{e\} \setminus \{e'\}$. Because $e$ was chosen as the minimum-weight cross-edge, $w(e) \le w(e')$, implying $w(T') \le w(T)$. Since $T$ is an MST, $w(T')$ must equal $w(T)$, meaning $T'$ is also a valid MST containing $e$. This contradicts the assumption that $e$ is absent from all valid MSTs. $\blacksquare$

### B. Correctness of DAG Shortest Paths via Topological Induction

**Theorem**: The relaxation-based single-source shortest path algorithm on a DAG, visiting vertices in topological order, correctly computes shortest paths from the source in $O(V + E)$ time.

**Proof**: We prove by induction on the topological position index $k$ of vertex $v$ that upon processing vertex $v$, the shortest path estimate `dist[v]` is equal to the true shortest path distance $\delta(s, v)$.

* *Base Case*: For the source vertex $s$ (or any vertex with in-degree 0 processed first), `dist[s] = 0`, which is correct.
* *Inductive Step*: Assume that for all preceding vertices $u$ in the topological order, `dist[u] = \delta(s, u)` has been correctly finalized. When vertex $v$ is processed, all incoming edges $(u, v)$ are relaxed. Because edges only flow from earlier to later vertices in a topological ordering, all possible paths leading into $v$ have already had their prefix subpaths finalized. By the optimal substructure property of shortest paths, $\delta(s, v) = \min_{(u,v) \in E} (\delta(s, u) + w(u, v))$. Thus, relaxing all incoming edges ensures `dist[v]` reaches its optimal minimum value. $\blacksquare$

---

## 5. Algorithm Selection Guide (*Which Algorithm When*)

| Scenario / Graph Property | Recommended Algorithm | Rationale |
| --- | --- | --- |
| **Sparse Undirected Graph ($m \ll n^2$)** | **Kruskal's Algorithm** | Edge sorting dominates, but DSU operations are nearly constant time $\alpha(n)$, making it extremely efficient for sparse topologies. |
| **Dense Undirected Graph ($m \approx n^2$)** | **Prim's Algorithm with Binary Heap** | Performance depends primarily on vertices rather than edges when implemented with efficient priority queues. |
| **Directed Graph Connectivity** | **Kosaraju's Algorithm** | Simple two-pass DFS structure easily adapted to iterative processing for deep recursion safety. |
| **Task Scheduling / Dependencies** | **Kahn's Topological Sort** | Directly identifies ordering constraints and naturally detects cycles via in-degree tracking. |
| **Pathfinding in DAGs (with negative weights)** | **DAG Shortest Paths** | Eliminates the need for Dijkstra or Bellman-Ford, running in linear time $O(V + E)$ by leveraging topological ordering. |

---

## 6. AI-Use Acknowledgement

The project was primarily developed independently by the author. AI tools were used occasionally for assistance with debugging, improving documentation, and formatting the report. The author was responsible for the implementation of the algorithms, testing, and analysis of benchmark results.