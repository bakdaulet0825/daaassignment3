package com.daaassignment3.paths;

import com.daaassignment3.graph.Graph;
import com.daaassignment3.scc.GraphTraversal;
import java.util.*;

public class DagShortestPath {
    public static class PathResult {
        public final long totalDistSum, edgeScans;
        public PathResult(long sum, long es) { this.totalDistSum = sum; this.edgeScans = es; }
    }

    public static PathResult computeShortestPaths(Graph g) {
        int n = g.size();
        List<Integer> topo = GraphTraversal.kahnTopologicalSort(g, null);
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE / 4);

        int[] inDeg = new int[n];
        for (Graph.Edge e : g.getAllEdges()) inDeg[e.v]++;
        for (int i = 0; i < n; i++) {
            if (inDeg[i] == 0) dist[i] = 0;
        }

        long edgeScans = 0;
        for (int u : topo) {
            if (dist[u] != Long.MAX_VALUE / 4) {
                for (Graph.Edge e : g.getAdj().get(u)) {
                    edgeScans++;
                    if (dist[u] + e.w < dist[e.v]) {
                        dist[e.v] = dist[u] + e.w;
                    }
                }
            }
        }
        long sum = 0;
        for (long d : dist) if (d < Long.MAX_VALUE / 4) sum += d;
        return new PathResult(sum, edgeScans);
    }
}