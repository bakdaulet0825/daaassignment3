package com.daaassignment3.mst;

import com.daaassignment3.dsu.DSU;
import com.daaassignment3.graph.Graph;
import java.util.*;

public class MSTAlgorithms {
    public static class MSTResult {
        public final long totalWeight;
        public final int components;
        public final long edgeScans, comparisons;
        public MSTResult(long w, int c, long es, long comp) {
            this.totalWeight = w; this.components = c; this.edgeScans = es; this.comparisons = comp;
        }
    }

    public static MSTResult kruskal(Graph g) {
        List<Graph.Edge> edges = new ArrayList<>(g.getAllEdges());
        long comparisons = (long)(edges.size() * Math.max(1, Math.log(Math.max(2, edges.size()))));
        edges.sort(Comparator.comparingInt(e -> e.w));

        DSU dsu = new DSU(g.size());
        long totalWeight = 0, edgeScans = 0;

        for (Graph.Edge e : edges) {
            edgeScans++;
            if (dsu.union(e.u, e.v)) {
                totalWeight += e.w;
            }
        }
        return new MSTResult(totalWeight, dsu.getComponentsCount(), edgeScans, comparisons);
    }

    public static MSTResult prim(Graph g) {
        int n = g.size();
        boolean[] vis = new boolean[n];
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.w));
        long totalWeight = 0, edgeScans = 0, comparisons = 0;
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                components++;
                vis[i] = true;
                for (Graph.Edge e : g.getAdj().get(i)) {
                    pq.add(e); edgeScans++;
                }
                while (!pq.isEmpty()) {
                    Graph.Edge e = pq.poll();
                    comparisons++;
                    if (vis[e.v]) continue;
                    vis[e.v] = true;
                    totalWeight += e.w;
                    for (Graph.Edge next : g.getAdj().get(e.v)) {
                        edgeScans++;
                        if (!vis[next.v]) pq.add(next);
                    }
                }
            }
        }
        return new MSTResult(totalWeight, components, edgeScans, comparisons);
    }
}