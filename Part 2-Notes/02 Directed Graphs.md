# Directed Graphs

## 1. introduction

### 1.1 Directed graphs

**Digraph**. Set of vertices connected pairwise by **directed** edges.

![image](https://s1.ax1x.com/2018/12/16/FdoIiD.png)

### 1.2 Digraph applications

![image](https://s1.ax1x.com/2018/12/16/FdoTRH.png)

### 1.3 Some digraph problems

**Path**. Is there a directed path from s to t ?

**Shortest path**. What is the shortest directed path from s to t ?

**Topological sort**. Can you draw a digraph so that all edges point upwards?

**Strong connectivity**. Is there a directed path between all pairs of vertices?

**Transitive closure**. For which vertices v and w is there a path from v to w ?

**PageRank**. What is the importance of a web page?

## 2. digraph API

### 2.1 Digraph API

![image](https://s1.ax1x.com/2018/12/16/Fd7KN8.png)

```java
// read digraph from input stream
In in = new In(args[0]);
Digraph G = new Digraph(in);

// print out each edge (once)
for (int v = 0; v < G.V(); v++)
    for (int w : G.adj(v))
        StdOut.println(v + "->" + w);
```

![image](https://s1.ax1x.com/2018/12/16/Fd713Q.png)

### 2.2 Adjacency-lists digraph representation

Maintain vertex-indexed array of lists.

![image](https://s1.ax1x.com/2018/12/16/FdHpKs.png)

### 2.3 Adjacency-lists graph representation (review): Java implementation

```java
public class Graph
{
    private final int V;
    private final Bag<Integer>[] adj;   // adjacency lists
    
    // create empty graph with V vertices
    public Graph(int V)
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }
    
    // add edge v–w
    public void addEdge(int v, int w)
    {
        adj[v].add(w);
        adj[w].add(v);
    }
    
    // iterator for vertices adjacent to v
    public Iterable<Integer> adj(int v)
    { return adj[v]; }
}
```

### 2.4 Adjacency-lists digraph representation: Java implementation

```java
public class Digraph
{
    private final int V;
    private final Bag<Integer>[] adj;   // adjacency lists
    
    // create empty digraph with V vertices
    public Digraph(int V)
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }
    
    // add edge v→w
    public void addEdge(int v, int w)
    {
        adj[v].add(w);
    }
    
    // iterator for vertices pointing from v
    public Iterable<Integer> adj(int v)
    { return adj[v]; }
}
```

### 2.5 Digraph representations

**In practice**. Use adjacency-lists representation.
- Algorithms based on iterating over vertices pointing from v.
- Real-world digraphs tend to be sparse.
    > huge number of vertices, small average vertex degree

![image](https://s1.ax1x.com/2018/12/16/Fdq3ge.png)

## 3. digraph search

### 3.1 Reachability

**Problem**. Find all vertices reachable from s along a directed path.

![image](https://s1.ax1x.com/2018/12/16/FdO80A.png)

### 3.2 Depth-first search in digraphs

##### Same method as for undirected graphs.
- Every undirected graph is a digraph (with edges in both directions).
- DFS is a **digraph** algorithm.

![image](https://s1.ax1x.com/2018/12/16/FdOttP.png)

### 3.3 Depth-first search demo

To visit a vertex v :
- Mark vertex v as visited.
- Recursively visit all unmarked vertices pointing from v.

### 3.4 Depth-first search (in undirected graphs)

Recall code for **undirected** graphs.

```java
public class DepthFirstSearch
{
    private boolean[] marked;   // true if path to s
    
    // constructor marks vertices connected to s
    public DepthFirstSearch(Graph G, int s)
    {
        marked = new boolean[G.V()];
        dfs(G, s);
    }
    
    // recursive DFS does the work
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
    }
    
    // client can ask whether any vertex is connected to s
    public boolean visited(int v)
    { return marked[v]; }
}
```

### 3.5 Depth-first search (in directed graphs)

Code for **directed** graphs identical to undirected one.
[substitute `Digraph` for `Graph`]

```java
public class DirectedDFS
{
    private boolean[] marked;   // true if path from s
    
    // constructor marks vertices reachable from s
    public DirectedDFS(Digraph G, int s)
    {
        marked = new boolean[G.V()];
        dfs(G, s);
    }
    
    // recursive DFS does the work
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
    }
    
    // client can ask whether any vertex is reachable from s
    public boolean visited(int v)
    { return marked[v]; }
}
```

### 3.6 Reachability application: program control-flow analysis

##### Every program is a digraph.
- Vertex = basic block of instructions (straight-line program).
- Edge = jump.

##### Dead-code elimination.
Find (and remove) unreachable code.

##### Infinite-loop detection.
Determine whether exit is unreachable.

![image](https://s1.ax1x.com/2018/12/16/Fdvr0H.png)

### 3.7 Reachability application: mark-sweep garbage collector

##### Every data structure is a digraph.
- Vertex = object.
- Edge = reference.

**Roots**. Objects known to be directly accessible by program (e.g., stack).

**Reachable objects**. Objects indirectly accessible by program
(starting at a root and following a chain of pointers).

![image](https://s1.ax1x.com/2018/12/16/FdxaUs.png)

##### Mark-sweep algorithm. [McCarthy, 1960]
- Mark: mark all reachable objects.
- Sweep: if object is unmarked, it is garbage (so add to free list).

**Memory cost.** Uses 1 extra mark bit per object (plus DFS stack).

### 3.8 Depth-first search in digraphs summary

##### DFS enables direct solution of simple digraph problems.
- Reachability. ✔
- Path finding.
- Topological sort.
- Directed cycle detection.

##### Basis for solving difficult digraph problems.
- 2-satisfiability.
- Directed Euler path.
- Strongly-connected components.

### 3.9 Breadth-first search in digraphs

##### Same method as for undirected graphs.
- Every undirected graph is a digraph (with edges in both directions).
- BFS is a **digraph** algorithm.

![image](https://s1.ax1x.com/2018/12/16/FdxqVH.png)

**Proposition**. BFS computes shortest paths (fewest number of edges) from s to all other vertices in a digraph in time proportional to E + V.

### 3.10 Directed breadth-first search demo

Repeat until queue is empty:
- Remove vertex v from queue.
- Add to queue all unmarked vertices pointing from v and mark them.

### 3.11 Multiple-source shortest paths

**Multiple-source shortest paths.** Given a digraph and a **set** of source vertices, find shortest path from any vertex in the set to each other vertex.

**Ex**. S = { 1, 7, 10 }.
- Shortest path to 4 is 7→6→4.
- Shortest path to 5 is 7→6→0→5.
- Shortest path to 12 is 10→12.
- …

![image](https://s1.ax1x.com/2018/12/16/Fdz9sS.png)

**Q**. How to implement multi-source shortest paths algorithm?

**A**. Use BFS, but initialize by ++enqueuing all source vertices++.

### 3.12 Breadth-first search in digraphs application: web crawler

**Goal**. Crawl web, starting from some root web page, say www.princeton.edu.

**Solution**. [BFS with implicit digraph]
- Choose root web page as source s.
- Maintain a `Queue` of websites to explore.
- Maintain a `SET` of discovered websites.
- Dequeue the next website and enqueue websites to which it links (provided you haven't done so before).

![image](https://s1.ax1x.com/2018/12/16/Fw9weU.png)

**Q**. Why not use DFS?

### 3.13 Bare-bones web crawler: Java implementation

```java
// queue of websites to crawl
Queue<String> queue = new Queue<String>();
// set of marked websites
SET<String> marked = new SET<String>();

// start crawling from root website
String root = "http://www.princeton.edu";
queue.enqueue(root);
marked.add(root);

while (!queue.isEmpty())
{
    // read in raw html from next website in queue
    String v = queue.dequeue();
    StdOut.println(v);
    In in = new In(v);
    String input = in.readAll();
    
    // use regular expression to find all URLs in website of form http://xxx.yyy.zzz
    // [crude pattern misses relative URLs]
    String regexp = "http://(\\w+\\.)*(\\w+)";
    Pattern pattern = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(input);
    while (matcher.find())
    {
        String w = matcher.group();
        if (!marked.contains(w))
        {
            // if unmarked, mark it and put on the queue
            marked.add(w);
            queue.enqueue(w);
        }
    }
}
```

## 4. topological sort

### 4.1 Precedence scheduling

**Goal**. Given a set of tasks to be completed with precedence constraints, in which order should we schedule the tasks?

**Digraph model**. vertex = task; edge = precedence constraint.

![image](https://s1.ax1x.com/2018/12/16/FwCM11.png)

### 4.2 Topological sort

**DAG**. Directed **acyclic** graph.

**Topological sort**. Redraw DAG so all edges point upwards.

![image](https://s1.ax1x.com/2018/12/16/FwCYAe.png)

**Solution**. DFS. What else?

### 4.3 Topological sort demo

- Run depth-first search.
- Return vertices in reverse postorder.

### 4.4 Depth-first search order

```java
public class DepthFirstOrder
{
    private boolean[] marked;
    private Stack<Integer> reversePost;
    
    public DepthFirstOrder(Digraph G)
    {
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }
    
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        for (int w : G.adj(v))
            if (!marked[w]) dfs(G, w);
        reversePost.push(v);
    }
    
    // returns all vertices in “reverse DFS postorder”
    public Iterable<Integer> reversePost()
    { return reversePost; }
}
```

### 4.5 Topological sort in a DAG: correctness proof

**Proposition**. Reverse DFS postorder of a DAG is a topological order.

**Pf**. Consider any edge `v→w`. When `dfs(v)` is called:

- Case 1: `dfs(w)` has already been called and returned. Thus, w was done before v.
- Case 2: `dfs(w)` has not yet been called. `dfs(w)` will get called directly or indirectly by `dfs(v)` and will finish before `dfs(v)`. Thus, w will be done before v.
- Case 3: `dfs(w)` has already been called, but has not yet returned. Can’t happen in a DAG: function call stack contains path from w to v, so v→w would complete a cycle.

![image](https://s1.ax1x.com/2018/12/16/FwMGYq.png)
> v = 3: all vertices pointing from 3 are done before 3 is done, so they appear after 3 in topological order

### 4.6 Directed cycle detection

**Proposition**. A digraph has a topological order iff no directed cycle.

**Pf**.
- If directed cycle, topological order impossible.
- If no directed cycle, DFS-based algorithm finds a topological order.

![image](https://s1.ax1x.com/2018/12/16/FwM00J.png)

**Goal**. Given a digraph, find a directed cycle.

**Solution**. DFS. What else? See textbook.

### 4.7 Directed cycle detection application: precedence scheduling

**Scheduling**. Given a set of tasks to be completed with precedence constraints, in what order should we schedule the tasks?

**Remark**. A directed cycle implies scheduling problem is infeasible.

### 4.8 Directed cycle detection application: cyclic inheritance

The Java compiler does cycle detection.

![image](https://s1.ax1x.com/2018/12/16/FwQd8P.png)

### 4.9 Directed cycle detection application: spreadsheet recalculation

Microsoft Excel does cycle detection (and has a circular reference toolbar!)

![image](https://s1.ax1x.com/2018/12/16/FwlhFA.png)

## 5. strong components

### 5.1 Strongly-connected components

**Def**. Vertices v and w are **strongly connected** if there is both a directed path from v to w and a directed path from w to v.

**Key property**. Strong connectivity is an **equivalence relation**:
- v is strongly connected to v.
- If v is strongly connected to w, then w is strongly connected to v.
- If v is strongly connected to w and w to x, then v is strongly connected to x.

**Def**. A **strong component** is a maximal subset of strongly-connected vertices.

![image](https://s1.ax1x.com/2018/12/16/Fw49mV.png)

### 5.2 Connected components vs. strongly-connected components

![image](https://s1.ax1x.com/2018/12/17/F0silT.png)

### 5.3 Strong component application: ecological food webs

**Food web graph**. Vertex = species; edge = from producer to consumer.

**Strong component**. Subset of species with common energy flow.

### 5.4 Strong component application: software modules

##### Software module dependency graph.
- Vertex = software module.
- Edge: from module to dependency.

**Strong component**. Subset of mutually interacting modules.

**Approach 1**. Package strong components together.

**Approach 2**. Use to improve design!

### 5.5 Strong components algorithms: brief history

##### 1960s: Core OR problem.
- Widely studied; some practical algorithms.
- Complexity not understood.

##### 1972: linear-time DFS algorithm (Tarjan).
- Classic algorithm.
- Level of difficulty: Algs4++.
- Demonstrated broad applicability and importance of DFS.

##### 1980s: easy two-pass linear-time algorithm (Kosaraju-Sharir).
- Forgot notes for lecture; developed algorithm in order to teach it!
- Later found in Russian scientific literature (1972).

##### 1990s: more easy linear-time algorithms.
- Gabow: fixed old OR algorithm.
- Cheriyan-Mehlhorn: needed one-pass algorithm for LEDA.

### 5.6 Kosaraju-Sharir algorithm: intuition

**Reverse graph**. Strong components in G are same as in `$G^R$`.

**Kernel DAG**. Contract each strong component into a single vertex.

**Idea**.
- Compute topological order (reverse postorder) in kernel DAG.
- Run DFS, considering vertices in reverse topological order.

### 5.7 Kosaraju-Sharir algorithm demo

**Phase 1**. Compute reverse postorder in `$G^R$`.

**Phase 2**. Run DFS in G, visiting unmarked vertices in reverse postorder of `$G^R$`.

### 5.8 Kosaraju-Sharir algorithm

##### Simple (but mysterious) algorithm for computing strong components.
- Phase 1: run DFS on `$G^R$` to compute reverse postorder.

![image](https://s1.ax1x.com/2018/12/17/F0Wtr6.md.png)

- Phase 2: run DFS on G, considering vertices in order given by first DFS.

![image](https://s1.ax1x.com/2018/12/17/F0WNqK.png)

> The DFS in the first phase (to compute the reverse postorder) is crucial; in the second phase, any algorithm that marks the set of vertices reachable from a given vertex will do.

### 5.9 Kosaraju-Sharir algorithm

**Proposition**. Kosaraju-Sharir algorithm computes the strong components of a digraph in time proportional to E + V.

**Pf**.
- Running time: bottleneck is running DFS twice (and computing `$G^R$`).
- Correctness: tricky, see textbook (2nd printing).
- Implementation: easy!

### 5.10 Connected components in an undirected graph (with DFS)

```java
public class CC
{
    private boolean marked[];
    private int[] id;
    private int count;
    
    public CC(Graph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        
        for (int v = 0; v < G.V(); v++)
        {
            if (!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }
    
    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }
    
    public boolean connected(int v, int w)
    { return id[v] == id[w]; }
}
```

### 5.11 Strong components in a digraph (with two DFSs)

```java
public class KosarajuSharirSCC
{
    private boolean marked[];
    private int[] id;
    private int count;
    
    public KosarajuSharirSCC(Digraph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        for (int v : dfs.reversePost())
        {
            if (!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }
    
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v))
            if (!marked[w])
                dfs(G, w);
    }
    
    public boolean stronglyConnected(int v, int w)
    { return id[v] == id[w]; }
}
```

### 5.12 Digraph-processing summary: algorithms of the day

![image](https://s1.ax1x.com/2018/12/17/F0WwIe.png)