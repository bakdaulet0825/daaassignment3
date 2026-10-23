import pandas as pd
import matplotlib.pyplot as plt
import os

os.makedirs('results/plots', exist_ok=True)
df = pd.read_csv('results/results.csv')

df['edge_scans'] = pd.to_numeric(df['edge_scans'])
df['scans_per_edge'] = df['edge_scans'] / df['m']
df['density'] = df.apply(lambda r: 'Sparse (m=4n)' if r['m'] == 4 * r['n'] else 'Dense (m=20n)', axis=1)

fig, ax = plt.subplots(figsize=(8, 6))
for alg in df['algorithm'].unique():
    for dens in ['Sparse (m=4n)', 'Dense (m=20n)']:
        sub = df[(df['algorithm'] == alg) & (df['density'] == dens)]
        linestyle = '-' if dens == 'Sparse (m=4n)' else '--'
        ax.plot(sub['n'], sub['edge_scans'], label=f"{alg} ({dens})", marker='o', linestyle=linestyle)

ax.set_xscale('log')
ax.set_yscale('log')
ax.set_xlabel('Number of Vertices (n)')
ax.set_ylabel('Edge Scans')
ax.set_title('Plot 1: Operations vs n (Log-Log Scale)')
ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
plt.tight_layout()
plt.savefig('results/plots/plot1_operations_vs_n.png', dpi=300)
plt.close()

fig, ax = plt.subplots(figsize=(8, 6))
for alg in df['algorithm'].unique():
    sub = df[(df['algorithm'] == alg) & (df['density'] == 'Sparse (m=4n)')]
    ax.plot(sub['n'], sub['scans_per_edge'], label=alg, marker='s')

ax.set_xscale('log')
ax.set_ylim(0, 3)
ax.set_xlabel('Number of Vertices (n)')
ax.set_ylabel('Scans per Edge (edgeScans / m)')
ax.set_title('Plot 2: Scans per Edge vs n')
ax.legend()
plt.tight_layout()
plt.savefig('results/plots/plot2_scans_per_edge.png', dpi=300)
plt.close()

fig, ax = plt.subplots(figsize=(8, 6))
k_sparse = df[(df['algorithm'] == 'Kruskal') & (df['density'] == 'Sparse (m=4n)')]
k_dense = df[(df['algorithm'] == 'Kruskal') & (df['density'] == 'Dense (m=20n)')]

ax.plot(k_sparse['n'], k_sparse['scans_per_edge'], label='Kruskal Sparse (m=4n)', marker='o', color='navy')
ax.plot(k_dense['n'], k_dense['scans_per_edge'], label='Kruskal Dense (m=20n)', marker='^', color='crimson')

ax.set_xscale('log')
ax.set_ylim(0, 1.2)
ax.set_xlabel('Number of Vertices (n)')
ax.set_ylabel('Examine Ratio (edgeScans / m)')
ax.set_title('Plot 3: Share of Edges Kruskal Examines')
ax.legend()
plt.tight_layout()
plt.savefig('results/plots/plot3_kruskal_edge_share.png', dpi=300)
plt.close()

print("All plots saved to results/plots/")