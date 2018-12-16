# Undirected Graphs

## 1. introduction

### 1.1 Undirected graphs

**Graph**. Set of vertices connected pairwise by edges.

##### Why study graph algorithms?
- Thousands of practical applications.
- Hundreds of graph algorithms known.
- Interesting and broadly useful abstraction.
- Challenging branch of computer science and discrete math.

### 1.2 Graph applications

![image](https://s1.ax1x.com/2018/12/06/F1PrLt.png)

### 1.3 Graph terminology

**Path**. Sequence of vertices connected by edges.

**Cycle**. Path whose first and last vertices are the same.

Two vertices are **connected** if there is a path between them.

![image](https://s1.ax1x.com/2018/12/06/F1lELT.png)

### 1.4 Some graph-processing problems

**Path**. Is there a path between s and t ?

**Shortest path**. What is the shortest path between s and t ?

**Cycle**. Is there a cycle in the graph?

**Euler tour**. Is there a cycle that uses each edge exactly once?

**Hamilton tour**. Is there a cycle that uses each vertex exactly once.

**Connectivity**. Is there a way to connect all of the vertices?

**MST**. What is the best way to connect all of the vertices?

**Biconnectivity**. Is there a vertex whose removal disconnects the graph?

**Planarity**. Can you draw the graph in the plane with no crossing edges

**Graph isomorphism**. Do two adjacency lists represent the same graph?

**Challenge**. Which of these problems are easy? difficult? intractable?

## 2. graph API

### 2.1 Graph representation

**Graph drawing**. Provides intuition about the structure of the graph.

**Caveat**. Intuition can be misleading.

##### Vertex representation.
- This lecture: use integers between 0 and V – 1.
- Applications: convert between names and integers with symbol table.

![image](https://s1.ax1x.com/2018/12/06/F1lhpn.png)

##### Anomalies.

![image](https://s1.ax1x.com/2018/12/06/F1lH7F.png)

### 2.2 Graph API

![image](https://s1.ax1x.com/2018/12/06/F1ljpR.png)

```java
In in = new In(args[0]);
Graph G = new Graph(in);    // read graph from input stream

for (int v = 0; v < G.V(); v++)
    for (int w : G.adj(v))  // print out each edge (twice)
        StdOut.println(v + "-" + w);
```

### 2.3 Graph API: sample client

Graph input format.

![image](https://s1.ax1x.com/2018/12/06/F11o8A.png)

### 2.4 Typical graph-processing code

```java
// compute the degree of v
public static int degree(Graph G, int v)
{
    int degree = 0;
    for (int w : G.adj(v)) degree++;
    return degree;
}

// compute maximum degree
public static int maxDegree(Graph G)
{
    int max = 0;
    for (int v = 0; v < G.V(); v++)
        if (degree(G, v) > max)
            max = degree(G, v);
    return max;
}

// compute average degree
public static double averageDegree(Graph G)
{ return 2.0 * G.E() / G.V(); }

// count self-loops
public static int numberOfSelfLoops(Graph G)
{
    int count = 0;
    for (int v = 0; v < G.V(); v++)
        for (int w : G.adj(v))
            if (v == w) count++;
    return count/2; // each edge counted twice
}
```

### 2.5 Set-of-edges graph representation

Maintain a list of the edges (linked list or array).

![image](https://s1.ax1x.com/2018/12/06/F13DZ8.png)

### 2.6 Adjacency-matrix graph representation

Maintain a two-dimensional V-by-V boolean array;
for each edge `v–w` in graph: `adj[v][w] = adj[w][v] = true`.

![image](https://s1.ax1x.com/2018/12/06/F13hLV.png)

### 2.7 Adjacency-list graph representation

Maintain vertex-indexed array of lists.

![image](https://s1.ax1x.com/2018/12/06/F13HJJ.png)

### 2.8 Adjacency-list graph representation: Java implementation

```java
public class Graph
{
    private final int V;
    private Bag<Integer>[] adj; // adjacency lists ( using Bag data type )
    
    public Graph(int V)
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];  // create empty graph with V vertices
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }
    
    public void addEdge(int v, int w)
    {
        // add edge v-w (parallel edges and self-loops allowed)
        adj[v].add(w);
        adj[w].add(v);
    }
    
    // iterator for vertices adjacent to v
    public Iterable<Integer> adj(int v)
    { return adj[v]; }
}
```

### 2.9 Graph representations

**In practice**. Use adjacency-lists representation.
- Algorithms based on iterating over vertices adjacent to v.
- Real-world graphs tend to be **sparse**.
    > sparse: huge number of vertices, small average vertex degree

![image](https://s1.ax1x.com/2018/12/06/F18Zef.png)

![image](https://s1.ax1x.com/2018/12/06/F18uFg.png)

## 3. depth-first search

### 3.1 Maze exploration

##### Maze graph.
- Vertex = intersection.
- Edge = passage.

![image](https://s1.ax1x.com/2018/12/06/F1BtJ0.png)

**Goal**. Explore every intersection in the maze.

### 3.2 Trémaux maze exploration

##### Algorithm.
- Unroll a ball of string behind you.
- Mark each visited intersection and each visited passage.
- Retrace steps when no unvisited options.

![image](https://s1.ax1x.com/2018/12/06/F1DFpV.png)

**First use?** Theseus entered Labyrinth to kill the monstrous Minotaur;
Ariadne instructed Theseus to use a ball of string to find his way back out.

### 3.3 Depth-first search

**Goal**. Systematically search through a graph.

**Idea**. Mimic maze exploration.

![image](https://s1.ax1x.com/2018/12/06/F1D7B4.png)

##### Typical applications.
- Find all vertices connected to a given source vertex.
- Find a path between two vertices.

**Design challenge**. How to implement?

### 3.4 Design pattern for graph processing

**Design pattern**. Decouple graph data type from graph processing.
- Create a Graph object.
- Pass the Graph to a graph-processing routine.
- Query the graph-processing routine for information.

![image](https://s1.ax1x.com/2018/12/06/F1DIjU.png)

```java
Paths paths = new Paths(G, s);
for (int v = 0; v < G.V(); v++)
    if (paths.hasPathTo(v))
        StdOut.println(v);  // print all vertices connected to s
```

### 3.5 Depth-first search demo

To visit a vertex v :
- Mark vertex v as visited.
- Recursively visit all unmarked vertices adjacent to v.

### 3.6 Depth-first search

**Goal**. Find all vertices connected to s (and a corresponding path).

**Idea**. Mimic maze exploration.

##### Algorithm.
- Use recursion (ball of string).
- Mark each visited vertex (and keep track of edge taken to visit it).
- Return (retrace steps) when no unvisited options.

##### Data structures.
-  `boolean[] marked` to mark visited vertices.
-  `int[] edgeTo` to keep tree of paths.
    > (`edgeTo[w] == v`) means that edge v-w taken to visit w for first time

```java
public class DepthFirstPaths
{
    // marked[v] = true if v connected to s
    private boolean[] marked;
    // edgeTo[v] = previous vertex on path from s to v
    private int[] edgeTo;
    private int s;
    
    public DepthFirstPaths(Graph G, int s)
    {
        ... // initialize data structures
        dfs(G, s);  // find vertices connected to s
    }
    
    // recursive DFS does the work
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w])
            {
                dfs(G, w);
                edgeTo[w] = v;
            }
    }
}
```

### 3.7 Depth-first search properties

**Proposition**. DFS marks all vertices connected to s in time proportional to the sum of their degrees.

![image](https://s1.ax1x.com/2018/12/06/F1rQEj.png)

**Pf**. [correctness]
- If w marked, then w connected to s (why?)
- If w connected to s, then w marked.
(if w unmarked, then consider last edge
on a path from s to w that goes from a
marked vertex to an unmarked one).

**Pf**. [running time]
Each vertex connected to s is visited once.

**Proposition**. After DFS, can find vertices connected to s in constant time and can find a path to s (if one exists) in time proportional to its length.

**Pf**. `edgeTo[]` is parent-link representation of a tree rooted at s.

```java
public boolean hasPathTo(int v)
{ return marked[v]; }

public Iterable<Integer> pathTo(int v)
{
    if (!hasPathTo(v)) return null;
    Stack<Integer> path = new Stack<Integer>();
    for (int x = v; x != s; x = edgeTo[x])
        path.push(x);
    path.push(s);
    return path;
}
```

### 3.8 Depth-first search application: flood fill

**Challenge**. Flood fill (Photoshop magic wand).

**Assumptions**. Picture has millions to billions of pixels.

**Solution**. Build a **grid graph**.
- Vertex: pixel.
- Edge: between two adjacent gray pixels.
- Blob: all pixels connected to given pixel.

![image](https://s1.ax1x.com/2018/12/06/F1rs8x.png)

## 4. breadth-first search

### 4.1 Breadth-first search demo

Repeat until queue is empty:
- Remove vertex v from queue.
- Add to queue all unmarked vertices adjacent to v and mark them.

### 4.2 Breadth-first search

**Depth-first search**. Put unvisited vertices on a **stack**.

**Breadth-first search**. Put unvisited vertices on a **queue**.

**Shortest path**. Find path from s to t that uses fewest number of edges.

![image](https://s1.ax1x.com/2018/12/06/F1ry26.png)

**Intuition**. BFS examines vertices in increasing distance from s.

### 4.3 Breadth-first search properties

**Proposition**. BFS computes shortest paths (fewest number of edges) from s to all other vertices in a graph in time proportional to E + V.

**Pf**. [correctness] Queue always consists of zero or more vertices of distance k from s, followed by zero or more vertices of distance k + 1.

**Pf**. [running time] Each vertex connected to s is visited once.

![image](https://s1.ax1x.com/2018/12/06/F1r6xK.png)

### 4.4 Breadth-first search

```java
public class BreadthFirstPaths
{
    private boolean[] marked;
    private int[] edgeTo;
    …
    
    private void bfs(Graph G, int s)
    {
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(s);
        marked[s] = true;
        while (!q.isEmpty())
        {
            int v = q.dequeue();
            for (int w : G.adj(v))
            {
                if (!marked[w])
                {
                    q.enqueue(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                }
            }
        }
    }
}
```

## 5. connected components

### 5.1 Connectivity queries

**Def**. Vertices v and w are **connected** if there is a path between them.

**Goal**. Preprocess graph to answer queries of the form is v connected to w? in **constant** time.

![image](https://s1.ax1x.com/2018/12/06/F1rhad.png)

**Union-Find?** Not quite.

**Depth-first search**. Yes. [next few slides]

### 5.2 Connected components

The relation "is connected to" is an equivalence relation:
- Reflexive: v is connected to v.
- Symmetric: if v is connected to w, then w is connected to v.
- Transitive: if v connected to w and w connected to x, then v connected to x.

**Def**. A connected component is a maximal set of connected vertices.

**Remark**. Given connected components, can answer queries in constant time.

![image](https://s1.ax1x.com/2018/12/06/F1sYFA.png)

**Def**. A connected component is a maximal set of connected vertices.

**Goal**. Partition vertices into connected components.

![image](https://s1.ax1x.com/2018/12/16/FdZIaV.png)

### 5.3 Connected components demo

To visit a vertex v :
- Mark vertex v as visited.
- Recursively visit all unmarked vertices adjacent to v.

### 5.4 Finding connected components with DFS

```java
public class CC
{
    private boolean[] marked;
    // id[v] = id of component containing v
    private int[] id;
    // number of components
    private int count;
    
    public CC(Graph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
        {
            if (!marked[v])
            {
                // run DFS from one vertex in each component
                dfs(G, v);
                count++;
            }
        }
    }
    
    // number of components
    public int count()
    { return count; }
    
    // id of component containing v
    public int id(int v)
    { return id[v]; }
    
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        // all vertices discovered in same call of dfs have same id
        id[v] = count;
        for (int w : G.adj(v))
        if (!marked[w])
        dfs(G, w);
    }
}
```

### 5.5 Connected components application: particle detection

**Particle detection**. Given grayscale image of particles, identify "blobs."
- Vertex: pixel.
- Edge: between two adjacent pixels with grayscale value ≥ 70.
    > black = 0, white = 255
- Blob: connected component of 20-30 pixels.

![image](https://s1.ax1x.com/2018/12/16/FdeZZt.png)

**Particle tracking**. Track moving particles over time.

## 6. challenges

### 6.1 Graph-processing challenge 1

**Problem**. Is a graph bipartite?

![image](https://s1.ax1x.com/2018/12/16/Fdnlbn.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.  ✔
    > simple DFS-based solution
- Hire an expert.
- Intractable.
- No one knows.
- Impossible.

### 6.2 Graph-processing challenge 2

**Problem**. Find a cycle.

![image](https://s1.ax1x.com/2018/12/16/FdnG5V.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.  ✔
    > simple DFS-based solution
- Hire an expert.
- Intractable.
- No one knows.
- Impossible.

**The Seven Bridges of Königsberg.** [Leonhard Euler 1736]

![image](https://s1.ax1x.com/2018/12/16/Fdn2xe.png)

**Euler tour**. Is there a (general) cycle that uses each edge exactly once?

**Answer**. A connected graph is Eulerian iff all vertices have even degree.

### 6.3 Graph-processing challenge 3

**Problem**. Find a (general) cycle that uses every **edge** exactly once.

![image](https://s1.ax1x.com/2018/12/16/FdnoIP.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.  ✔
    > Eulerian tour (classic graph-processing problem)
- Hire an expert.
- Intractable.
- No one knows.
- Impossible..

### 6.4 Graph-processing challenge 4

**Problem**. Find a cycle that visits every **vertex** exactly once.

![image](https://s1.ax1x.com/2018/12/16/FduSI0.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.
- Hire an expert.
- Intractable.  ✔
    > Hamiltonian cycle (classical NP-complete problem)
- No one knows.
- Impossible.

### 6.5 Graph-processing challenge 5

**Problem**. Are two graphs identical except for vertex names?

![image](https://s1.ax1x.com/2018/12/16/FduEL9.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.
- Hire an expert.
- Intractable.
- No one knows. ✔
    > graph isomorphism is longstanding open problem
- Impossible.

### 6.6 Graph-processing challenge 6

**Problem**. Lay out a graph in the plane without crossing edges?

![image](https://s1.ax1x.com/2018/12/16/FduKJK.png)

##### How difficult?
- Any programmer could do it.
- Typical diligent algorithms student could do it.
- Hire an expert.   ✔
    > - linear-time DFS-based planarity algorithm discovered by Tarjan in 1970s
    > - (too complicated for most practitioners)
- Intractable.
- No one knows.
- Impossible.