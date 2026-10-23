package com.daaassignment3.benchmark;

import com.daaassignment3.graph.Graph;
import com.daaassignment3.mst.MSTAlgorithms;
import com.daaassignment3.scc.GraphTraversal;
import com.daaassignment3.paths.DagShortestPath;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

public class Benchmark {

    public static void runBenchmark() throws IOException {
        int[] sizes = {1000, 3162, 10000, 31623, 100000};
        double[] multipliers = {4.0, 20.0};

        File resultsDir = new File("results");
        if (!resultsDir.exists()) resultsDir.mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter("results/results.csv"))) {
            pw.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");

            for (int n : sizes) {
                for (double mult : multipliers) {
                    int m = (int) (n * mult);

                    Graph undirectedGraph = generateGraph(n, m, GraphType.UNDIRECTED);

                    long start = System.nanoTime();
                    MSTAlgorithms.MSTResult kruskalRes = MSTAlgorithms.kruskal(undirectedGraph);
                    long timeKruskal = (System.nanoTime() - start) / 1_000_000;
                    pw.printf("Kruskal,undirected,%d,%d,%d,-,%d,%d,-,%d\n",
                            n, m, timeKruskal, kruskalRes.edgeScans, kruskalRes.comparisons, kruskalRes.totalWeight);

                    start = System.nanoTime();
                    MSTAlgorithms.MSTResult primRes = MSTAlgorithms.prim(undirectedGraph);
                    long timePrim = (System.nanoTime() - start) / 1_000_000;
                    pw.printf("Prim,undirected,%d,%d,%d,-,%d,%d,-,%d\n",
                            n, m, timePrim, primRes.edgeScans, primRes.comparisons, primRes.totalWeight);

                    Graph directedGraph = generateGraph(n, m, GraphType.DIRECTED);
                    start = System.nanoTime();
                    GraphTraversal.SCCResult sccRes = GraphTraversal.kosaraju(directedGraph);
                    long timeKosaraju = (System.nanoTime() - start) / 1_000_000;
                    pw.printf("Kosaraju,directed,%d,%d,%d,-,%d,-,%d,%d\n",
                            n, m, timeKosaraju, sccRes.edgeScans, sccRes.maxDepth, sccRes.sccCount);

                    Graph dagGraph = generateGraph(n, m, GraphType.DAG);
                    start = System.nanoTime();
                    int[] isDag = new int[1];
                    List<Integer> topo = GraphTraversal.kahnTopologicalSort(dagGraph, isDag);
                    long timeKahn = (System.nanoTime() - start) / 1_000_000;
                    pw.printf("Kahn,dag,%d,%d,%d,-,%d,-,-,%d\n",
                            n, m, timeKahn, dagGraph.getAllEdges().size(), topo.size());

                    start = System.nanoTime();
                    DagShortestPath.PathResult pathRes = DagShortestPath.computeShortestPaths(dagGraph);
                    long timeSp = (System.nanoTime() - start) / 1_000_000;
                    pw.printf("DagShortestPath,dag,%d,%d,%d,-,%d,-,-,%d\n",
                            n, m, timeSp, pathRes.edgeScans, pathRes.totalDistSum);
                }
            }
            System.out.println("Benchmark completed successfully. Results saved to results/results.csv");
        }
    }

    private enum GraphType { UNDIRECTED, DIRECTED, DAG }

    private static Graph generateGraph(int n, int m, GraphType type) {
        Random rnd = new Random(42);
        boolean directed = (type != GraphType.UNDIRECTED);
        Graph g = new Graph(n, directed);

        if (type == GraphType.UNDIRECTED) {
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int temp = perm[i]; perm[i] = perm[j]; perm[j] = temp;
            }
            for (int i = 1; i < n; i++) {
                int p = perm[rnd.nextInt(i)];
                int c = perm[i];
                g.addEdge(p, c, 1 + rnd.nextInt(1000));
            }
            int added = n - 1;
            while (added < m) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                if (a != b) {
                    g.addEdge(a, b, 1 + rnd.nextInt(1000));
                    added++;
                }
            }
        } else if (type == GraphType.DIRECTED) {
            int added = 0;
            while (added < m) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                if (a != b) {
                    g.addEdge(a, b, 1 + rnd.nextInt(1000));
                    added++;
                }
            }
        } else {
            int[] rank = new int[n];
            for (int i = 0; i < n; i++) rank[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int temp = rank[i]; rank[i] = rank[j]; rank[j] = temp;
            }
            int added = 0;
            while (added < m) {
                int a = rnd.nextInt(n);
                int b = rnd.nextInt(n);
                if (a != b) {
                    if (rank[a] > rank[b]) {
                        int t = a; a = b; b = t;
                    }
                    g.addEdge(a, b, 1 + rnd.nextInt(1000));
                    added++;
                }
            }
        }
        return g;
    }
}