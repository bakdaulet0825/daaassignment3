package com.daaassignment3.scc;

import com.daaassignment3.graph.Graph;
import java.util.*;

public class GraphTraversal {
    public static class SCCResult {
        public final int sccCount;
        public final long edgeScans, maxDepth;
        public SCCResult(int c, long es, long d) {
            this.sccCount = c; this.edgeScans = es; this.maxDepth = d;
        }
    }

    public static SCCResult kosaraju(Graph g) {
        int n = g.size();
        List<List<Integer>> adj = new ArrayList<>(), rev = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>()); rev.add(new ArrayList<>());
        }
        long edgeScans = 0;
        for (Graph.Edge e : g.getAllEdges()) {
            edgeScans++;
            adj.get(e.u).add(e.v);
            rev.get(e.v).add(e.u);
        }

        boolean[] vis = new boolean[n];
        List<Integer> finishOrder = new ArrayList<>();
        long maxDepth = 0;

        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                Deque<int[]> st = new ArrayDeque<>();
                st.push(new int[]{i, 0});
                vis[i] = true;
                while (!st.isEmpty()) {
                    maxDepth = Math.max(maxDepth, st.size());
                    int[] top = st.peek();
                    int u = top[0], idx = top[1];
                    List<Integer> neighbors = adj.get(u);
                    if (idx < neighbors.size()) {
                        top[1]++;
                        int v = neighbors.get(idx);
                        edgeScans++;
                        if (!vis[v]) {
                            vis[v] = true;
                            st.push(new int[]{v, 0});
                        }
                    } else {
                        st.pop();
                        finishOrder.add(u);
                    }
                }
            }
        }

        Arrays.fill(vis, false);
        int sccCount = 0;
        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            int root = finishOrder.get(i);
            if (!vis[root]) {
                sccCount++;
                Deque<Integer> st = new ArrayDeque<>();
                st.push(root);
                vis[root] = true;
                while (!st.isEmpty()) {
                    int u = st.pop();
                    for (int v : rev.get(u)) {
                        edgeScans++;
                        if (!vis[v]) {
                            vis[v] = true;
                            st.push(v);
                        }
                    }
                }
            }
        }
        return new SCCResult(sccCount, edgeScans, maxDepth);
    }

    public static List<Integer> kahnTopologicalSort(Graph g, int[] isDagOut) {
        int n = g.size();
        int[] inDeg = new int[n];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Graph.Edge e : g.getAllEdges()) {
            adj.get(e.u).add(e.v);
            inDeg[e.v]++;
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (inDeg[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                inDeg[v]--;
                if (inDeg[v] == 0) q.add(v);
            }
        }
        if (isDagOut != null) isDagOut[0] = (order.size() == n) ? 1 : 0;
        return order;
    }
}