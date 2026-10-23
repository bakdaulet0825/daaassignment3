package com.daaassignment3;

import com.daaassignment3.dsu.DSU;
import com.daaassignment3.graph.Graph;
import com.daaassignment3.mst.MSTAlgorithms;
import com.daaassignment3.scc.GraphTraversal;
import com.daaassignment3.paths.DagShortestPath;
import com.daaassignment3.benchmark.Benchmark;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class GraphAlgorithmsTest {

    @Test
    public void testDSU() {
        DSU dsu = new DSU(5);
        dsu.union(0, 1);
        dsu.union(1, 2);
        assertEquals(dsu.find(0), dsu.find(2));
        assertEquals(3, dsu.getComponentsCount());
    }

    @Test
    public void testMSTAgreement() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 10);
        g.addEdge(1, 2, 20);
        g.addEdge(2, 3, 30);
        g.addEdge(0, 3, 40);

        MSTAlgorithms.MSTResult kRes = MSTAlgorithms.kruskal(g);
        MSTAlgorithms.MSTResult pRes = MSTAlgorithms.prim(g);
        assertEquals(kRes.totalWeight, pRes.totalWeight);
    }

    @Test
    public void testKahnCycleDetection() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        int[] isDag = new int[1];
        GraphTraversal.kahnTopologicalSort(g, isDag);
        assertEquals(0, isDag[0]);
    }

    @Test
    public void testStackSafety100k() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i < n - 1; i++) {
            g.addEdge(i, i + 1, 1);
        }
        GraphTraversal.SCCResult res = GraphTraversal.kosaraju(g);
        assertEquals(n, res.sccCount);
    }

    @Test
    public void testDagShortestPath() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, -2);
        g.addEdge(0, 2, 10);

        DagShortestPath.PathResult pathRes = DagShortestPath.computeShortestPaths(g);
        assertTrue(pathRes.totalDistSum >= 0);
    }

    @Test
    public void runFullBenchmark() throws IOException {
        Benchmark.runBenchmark();
    }
}