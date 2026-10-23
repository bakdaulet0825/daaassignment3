# Assignment 3: Graph Algorithms & Performance Analysis

A modular Java framework for graph representation, graph algorithms (MST, SCC, Topological Sort, Shortest Paths in DAGs), automated benchmarking, and empirical performance metrics analysis.

---

## 🛠 Project Structure

com.daaassignment3/
├── graph/        # Graph representation (Adjacency List & Edge List)
├── dsu/          # Disjoint Set Union with Path Compression & Union-by-Rank
├── mst/          # Minimum Spanning Tree algorithms (Kruskal & Prim)
├── scc/          # Strongly Connected Components (Kosaraju) & Kahn's Algorithm
├── paths/        # DAG Shortest Paths via Topological Ordering
└── benchmark/    # Automated graph generation & benchmarking framework


---

## 🚀 Getting Started

### Prerequisites
* **Java 17+**
* **Maven** (for building and testing)
* **Python 3.8+** with `pandas` and `matplotlib` (optional, for regenerating plots)

### 1. Run Unit Tests
To execute all algorithmic correctness tests via JUnit 5:
```bash
mvn test
```
2. Run Benchmarks
   To run the automated performance benchmark and output metrics to results/results.csv:

mvn compile exec:java -Dexec.mainClass="com.daaassignment3.benchmark.Benchmark"
(Or run Benchmark.java directly inside IntelliJ IDEA / Eclipse)

3. Generate Visualizations (Plots)
   To generate performance plots in results/plots/:

python generate_plots.py
📊 Dataset & Benchmark Input Formats
Input graph datasets are located in data/:

cyclic.txt: Directed cyclic graph used for SCC validation.

dag.txt: Directed Acyclic Graph used for Kahn's topological sort and DAG shortest paths.

disconnected.txt: Undirected disconnected graph for testing MST on multiple connected components.

📜 Documentation
Detailed analysis, complexity tables, theoretical proofs, and algorithm selection guidelines are available in REPORT.md.

Generated visualization plots are saved under results/plots/.