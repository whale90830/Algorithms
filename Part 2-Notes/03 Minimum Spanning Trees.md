# Minimum Spanning Trees

## 1. introduction

### 1.1 Minimum spanning tree

**Given**. Undirected graph G with positive edge weights (connected).

**Def**. A spanning tree of G is a subgraph T that is both a tree (connected and acyclic) and spanning (includes all of the vertices).

**Goal**. Find a min weight spanning tree.

![image](https://s1.ax1x.com/2018/12/17/F0TUl4.png)

**Brute force**. Try all spanning trees?

### 1.2 Applications

##### MST is fundamental problem with diverse applications.

- Dithering.
- Cluster analysis.
- Max bottleneck paths.
- Real-time face verification.
- LDPC codes for error correction.
- Image registration with Renyi entropy.
- Find road networks in satellite and aerial imagery.
- Reducing data storage in sequencing amino acids in a protein.
- Model locality of particle interactions in turbulent fluid flows.
- Autoconfig protocol for Ethernet bridging to avoid cycles in a network.
- Approximation algorithms for NP-hard problems (e.g., TSP, Steiner tree).
- Network design (communication, electrical, hydraulic, computer, road).

## 2. greedy algorithm

### 2.1 Simplifying assumptions

##### Simplifying assumptions.
- Edge weights are distinct.
- Graph is connected.

**Consequence**. MST exists and is unique.

![image](https://s1.ax1x.com/2018/12/20/FDHhJx.png)

### 2.2 Cut property

**Def**. A **cut** in a graph is a partition of its vertices into two (nonempty) sets.

**Def**. A **crossing edge** connects a vertex in one set with a vertex in the other.

**Cut property**. Given any cut, the crossing edge of min weight is in the MST.

![image](https://s1.ax1x.com/2018/12/20/FDbt1K.png)

### 2.3 Cut property: correctness proof

**Cut property**. Given any cut, the crossing edge of min weight is in the MST.

**Pf**. Suppose min-weight crossing edge e is not in the MST.
- Adding e to the MST creates a cycle.
- Some other edge f in cycle must be a crossing edge.
- Removing f and adding e is also a spanning tree.
- Since weight of e is less than the weight of f, that spanning tree is lower weight.
- Contradiction.

![image](https://s1.ax1x.com/2018/12/20/FDqPgK.png)

### 2.4 Greedy MST algorithm demo

- Start with all edges colored gray.
- Find cut with no black crossing edges; color its min-weight edge black.
- Repeat until V - 1 edges are colored black.

### 2.5 Greedy MST algorithm: correctness proof

**Proposition**. The greedy algorithm computes the MST.

**Pf**.
- Any edge colored black is in the MST (via cut property).
- Fewer than V - 1 black edges ⇒ cut with no black crossing edges. (consider cut whose vertices are one connected component)

![image](https://s1.ax1x.com/2018/12/20/FDqEHH.png)

### 2.6 Greedy MST algorithm: efficient implementations

**Proposition**. The greedy algorithm computes the MST.

**Efficient implementations**. Choose cut? Find min-weight edge?

**Ex 1**. Kruskal's algorithm. [stay tuned]

**Ex 2**. Prim's algorithm. [stay tuned]

**Ex 3**. Borüvka's algorithm.

### 2.7 Removing two simplifying assumptions

**Q**. What if edge weights are not all distinct?

**A**. Greedy MST algorithm still correct if equal weights are present! (our correctness proof fails, but that can be fixed)

![image](https://s1.ax1x.com/2018/12/20/FDXvM6.png)

**Q**. What if graph is not connected?

**A**. Compute minimum spanning forest = MST of each component.

![image](https://s1.ax1x.com/2018/12/20/FDXzqO.png)

## 3. edge-weighted graph API

### 3.1 Weighted edge API

Edge abstraction needed for weighted edges.

![image](https://s1.ax1x.com/2018/12/20/FDjCIH.png)

Idiom for processing an edge e: `int v = e.either()`, `w = e.other(v)`;

### 3.2 Weighted edge: Java implementation

```java
public class Edge implements Comparable<Edge>
{
    private final int v, w;
    private final double weight;
    
    // constructor
    public Edge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    
    // either endpoint
    public int either()
    { return v; }
    
    // other endpoint
    public int other(int vertex)
    {
        if (vertex == v) return w;
        else return v;
    }
    
    // compare edges by weight
    public int compareTo(Edge that)
    {
        if (this.weight < that.weight) return -1;
        else if (this.weight > that.weight) return +1;
        else return 0;
    }
}
```

### 3.3 Edge-weighted graph API

![image](https://s1.ax1x.com/2018/12/20/FDjT6P.png)

**Conventions**. Allow self-loops and parallel edges.

### 3.4 Edge-weighted graph: adjacency-lists representation

Maintain vertex-indexed array of Edge lists.

![image](https://s1.ax1x.com/2018/12/20/FDjbm8.png)

> There is one `Edge` object for each edge in the graph. The adjacency lists contain two references to each `Edge` but there is still only one `Edge` object per graph edge.

### 3.5 Edge-weighted graph: adjacency-lists implementation

```java
public class EdgeWeightedGraph
{
    private final int V;
    private final Bag<Edge>[] adj;
    // same as Graph, but adjacency lists of Edges instead of integers
    
    // constructor
    public EdgeWeightedGraph(int V)
    {
        this.V = V;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Edge>();
    }
    
    // add edge to both adjacency lists
    public void addEdge(Edge e)
    {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
    }
    
    public Iterable<Edge> adj(int v)
    { return adj[v]; }
}
```

### 3.6 Minimum spanning tree API

**Q**. How to represent the MST?

![image](https://s1.ax1x.com/2018/12/20/FDjXkQ.png)

```java
public static void main(String[] args)
{
    In in = new In(args[0]);
    EdgeWeightedGraph G = new EdgeWeightedGraph(in);
    MST mst = new MST(G);
    for (Edge e : mst.edges())
        StdOut.println(e);
    StdOut.printf("%.2f\n", mst.weight());
}
```

![image](https://s1.ax1x.com/2018/12/20/FDjjYj.png)

## 4. Kruskal's algorithm

### 4.1 Kruskal's algorithm demo

Consider edges in ascending order of weight.
- Add next edge to tree T unless doing so would create a cycle.

### 4.2 Kruskal's algorithm: correctness proof

**Proposition**. [Kruskal 1956] Kruskal's algorithm computes the MST.

**Pf**. Kruskal's algorithm is a special case of the greedy MST algorithm.
- Suppose Kruskal's algorithm colors the edge e = v–w black.
- Cut = set of vertices connected to v in tree T.
- No crossing edge is black.
- No crossing edge has lower weight. Why?

![image](https://s1.ax1x.com/2018/12/20/FDxPgI.png)

### 4.3 Kruskal's algorithm: implementation challenge

**Challenge**. Would adding edge v–w to tree T create a cycle? If not, add it.

##### How difficult?
-  E + V
-  V
    > run DFS from v, check if w is reachable (T has at most V – 1 edges)
-  log V
-  log* V
    > use the union-find data structure !
-  1

![image](https://s1.ax1x.com/2018/12/20/FDxZVS.png)

**Efficient solution**. Use the **union-find** data structure.
- Maintain a set for each connected component in T.
- If v and w are in same set, then adding v–w would create a cycle.
- To add v–w to T, merge sets containing v and w.

![image](https://s1.ax1x.com/2018/12/20/FDxeUg.png)

### 4.4 Kruskal's algorithm: Java implementation

```java
public class KruskalMST
{
    private Queue<Edge> mst = new Queue<Edge>();
    
    public KruskalMST(EdgeWeightedGraph G)
    {
        // build priority queue
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges())
            pq.insert(e);
        
        UF uf = new UF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V()-1)
        {
            Edge e = pq.delMin();   // greedily add edges to MST
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w))    // edge v–w does not create cycle
            {
                uf.union(v, w); // merge sets
                mst.enqueue(e); // add edge to MST
            }
        }
    }
    
    public Iterable<Edge> edges()
    { return mst; }
}
```

### 4.5 Kruskal's algorithm: running time

**Proposition**. Kruskal's algorithm computes MST in time proportional to E log E (in the worst case).

**Pf**.

![image](https://s1.ax1x.com/2018/12/20/FDxcIe.png)

**Remark**. If edges are already sorted, order of growth is E log* V.
> recall: log* V ≤ 5 in this universe

## 5. Prim's algorithm

### 5.1 Prim's algorithm demo

- Start with vertex 0 and greedily grow tree T.
- Add to T the min weight edge with exactly one endpoint in T.
- Repeat until V - 1 edges.

### 5.2 Prim's algorithm: proof of correctness

##### Proposition. [Jarník 1930, Dijkstra 1957, Prim 1959]
Prim's algorithm computes the MST.

**Pf**. Prim's algorithm is a special case of the greedy MST algorithm.
- Suppose edge e = min weight edge connecting a vertex on the tree to a vertex not on the tree.
- Cut = set of vertices connected on tree.
- No crossing edge is black.
- No crossing edge has lower weight.

![image](https://s1.ax1x.com/2018/12/20/FrrJg0.png)

### 5.3 Prim's algorithm: implementation challenge

**Challenge**. Find the min weight edge with exactly one endpoint in T.

##### How difficult?
-  E
    > try all edges
-  V
-  log E
    > use a priority queue!
-  log* E
-  l

### 5.4 Prim's algorithm: lazy implementation

**Challenge**. Find the min weight edge with exactly one endpoint in T.

**Lazy solution**. Maintain a PQ of edges with (at least) one endpoint in T.
- Key = edge; priority = weight of edge.
- Delete-min to determine next edge e = v–w to add to T.
- Disregard if both endpoints v and w are marked (both in T).
- Otherwise, let w be the unmarked vertex (not in T ):
    - add to PQ any edge incident to w (assuming other endpoint not in T)
    - add e to T and mark w

![image](https://s1.ax1x.com/2018/12/20/Fry9FH.png)

### 5.5 Prim's algorithm (lazy) demo

- Start with vertex 0 and greedily grow tree T.
- Add to T the min weight edge with exactly one endpoint in T.
- Repeat until V - 1 edges.

### 5.6 Prim's algorithm: lazy implementation

```java
public class LazyPrimMST
{
    private boolean[] marked; // MST vertices
    private Queue<Edge> mst; // MST edges
    private MinPQ<Edge> pq; // PQ of edges
    
    public LazyPrimMST(WeightedGraph G)
    {
        pq = new MinPQ<Edge>();
        mst = new Queue<Edge>();
        marked = new boolean[G.V()];    // assume G is connected
        visit(G, 0);
        
        while (!pq.isEmpty() && mst.size() < G.V() - 1)
        {
            // repeatedly delete the min weight edge e = v–w from PQ
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            if (marked[v] && marked[w]) continue;   // ignore if both endpoints in T
            mst.enqueue(e); // add edge e to tree
            // add v or w to tree
            if (!marked[v]) visit(G, v);
            if (!marked[w]) visit(G, w);
        }
    }
    
    private void visit(WeightedGraph G, int v)
    {
        marked[v] = true;   // add v to T
        // for each edge e = v–w, add to PQ if w not already in T
        for (Edge e : G.adj(v))
            if (!marked[e.other(v)])
                pq.insert(e);
    }
    
    public Iterable<Edge> mst()
    { return mst; }
}
```

### 5.7 Lazy Prim's algorithm: running time

**Proposition**. Lazy Prim's algorithm computes the MST in time proportional
to E log E and extra space proportional to E (in the worst case).

**Pf**.

![image](https://s1.ax1x.com/2018/12/20/Fr6k4J.png)

### 5.8 Prim's algorithm: eager implementation

**Challenge**. Find min weight edge with exactly one endpoint in T.

**Eager solution**. Maintain a PQ of vertices connected by an edge to T, where priority of vertex v = weight of shortest edge connecting v to T.
> pq has at most one entry per vertex

- Delete min vertex v and add its associated edge e = v–w to T.
- Update PQ by considering all edges e = v–x incident to v
    - ignore if x is already in T
    - add x to PQ if not already on it
    - **decrease priority** of x if v–x becomes shortest edge connecting x to T
![image](https://s1.ax1x.com/2018/12/24/Fc9vcD.png)
### 5.9 Prim's algorithm (eager) demo

- Start with vertex 0 and greedily grow tree T.
- Add to T the min weight edge with exactly one endpoint in T.
- Repeat until V - 1 edges.

### 5.10 Indexed priority queue

Associate an index between 0 and N - 1 with each key in a priority queue.
- Client can insert and delete-the-minimum.
- Client can change the key by specifying the index.

![image](https://s1.ax1x.com/2018/12/24/FcC9HA.png)

### 5.11 Indexed priority queue implementation

##### Implementation.
- Start with same code as `MinPQ`.
- Maintain parallel arrays `keys[]`, `pq[]`, and `qp[]` so that:
    - `keys[i]` is the priority of i
    - `pq[i]` is the index of the key in heap position i
    - `qp[i]` is the heap position of the key with index i
- Use `swim(qp[i])` implement `decreaseKey(i, key)`.

![image](https://s1.ax1x.com/2018/12/24/FcCE38.png)

### 5.12 Prim's algorithm: running time

Depends on PQ implementation: V insert, V delete-min, E decrease-key.

![image](https://s1.ax1x.com/2018/12/24/FcCnBj.png)

##### Bottom line.
- Array implementation optimal for dense graphs.
- Binary heap much faster for sparse graphs.
- 4-way heap worth the trouble in performance-critical situations.
- Fibonacci heap best in theory, but not worth implementing.

## 6. context

### 6.1 Does a linear-time MST algorithm exist?

![image](https://s1.ax1x.com/2018/12/24/Fcih1f.png)

**Remark**. Linear-time randomized MST algorithm (Karger-Klein-Tarjan 1995).

### 6.2 Euclidean MST

Given N points in the plane, find MST connecting them, where the distances between point pairs are their Euclidean distances.

![image](https://s1.ax1x.com/2018/12/24/Fci5jS.png)

**Brute force**. Compute `$\sim N^2 / 2$` distances and run Prim's algorithm.

**Ingenuity**. Exploit geometry and do it in `$\sim c N \log N$`.

### 6.3 Scientific application: clustering

**k-clustering**. Divide a set of objects classify into k coherent groups.

**Distance function**. Numeric value specifying "closeness" of two objects.

**Goal**. Divide into clusters so that objects in different clusters are far apart.

![image](https://s1.ax1x.com/2018/12/24/Fci77j.png)

##### Applications.
- Routing in mobile ad hoc networks.
- Document categorization for web search.
- Similarity searching in medical image databases.
- Skycat: cluster `$10^9$` sky objects into stars, quasars, galaxies.

### 6.4 Single-link clustering

**k-clustering**. Divide a set of objects classify into k coherent groups.

**Distance function**. Numeric value specifying "closeness" of two objects.

**Single link**. Distance between two clusters equals the distance between the two closest objects (one in each cluster).

**Single-link clustering**. Given an integer k, find a k-clustering that maximizes the distance between two closest clusters.

![image](https://s1.ax1x.com/2018/12/24/FciX90.png)

### 6.5 Single-link clustering algorithm

##### “Well-known” algorithm in science literature for single-link clustering:
- Form V clusters of one object each.
- Find the closest pair of objects such that each object is in a different cluster, and merge the two clusters.
- Repeat until there are exactly k clusters.

**Observation**. This is Kruskal's algorithm (stop when k connected components).

![image](https://s1.ax1x.com/2018/12/24/FcixjU.png)

**Alternate solution**. Run Prim's algorithm and delete k–1 max weight edges.