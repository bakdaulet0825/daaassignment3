package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    public static class Edge implements Comparable<Edge> {
        public final int u, v, w;
        public Edge(int u, int v, int w) {
            this.u = u; this.v = v; this.w = w;
        }
        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.w, o.w);
        }
    }

    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> allEdges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        this.allEdges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int w) {
        if (u < 0 || u >= n || v < 0 || v >= n) return;
        Edge edge = new Edge(u, v, w);
        allEdges.add(edge);
        adj.get(u).add(edge);
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
        }
    }

    public static Graph fromFile(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] header = br.readLine().trim().split("\\s+");
            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);
            boolean directed = Boolean.parseBoolean(header[2]);
            Graph g = new Graph(n, directed);
            for (int i = 0; i < m; i++) {
                String line = br.readLine();
                if (line == null) break;
                String[] p = line.trim().split("\\s+");
                g.addEdge(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
            return g;
        }
    }

    public int size() { return n; }
    public boolean isDirected() { return directed; }
    public List<List<Edge>> getAdj() { return adj; }
    public List<Edge> getAllEdges() { return allEdges; }
}