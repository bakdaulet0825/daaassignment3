package com.daaassignment3.dsu;

public class DSU {
    private final int[] parent, rank;
    private int componentsCount;
    public long dsuOps = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        componentsCount = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        dsuOps++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        dsuOps++;
        if (rank[rx] < rank[ry]) {
            parent[rx] = ry;
        } else if (rank[rx] > rank[ry]) {
            parent[ry] = rx;
        } else {
            parent[ry] = rx;
            rank[rx]++;
        }
        componentsCount--;
        return true;
    }

    public int getComponentsCount() { return componentsCount; }
}