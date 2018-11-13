# Union-Find

Steps to developing a usable algorithm.
- Model the problem.
- Find an algorithm to solve it.
- Fast enough? Fits in memory?
- If not, figure out why.
- Find a way to address the problem.
- Iterate until satisfied.

## 1. dynamic connectivity

#### 1.1 Dynamic connectivity
- Union command: connect two objects.
- Find/connected query: is there a path connecting the two objects?

#### 1.2 Modeling the connections

- Applications involve manipulating objects of all types.
- When programming, convenient to name objects 0 to N –1.（简化模型）
- We assume "is connected to" is an equivalence relation（等价关系）:
    - Reflexive（自反性）: p is connected to p.
    - Symmetric（反身性）: if p is connected to q, then q is connected to p.
    - Transitive（传递性）: if p is connected to q and q is connected to r, then p is connected to r.
- Connected components（连通分量）. Maximal set of objects that are mutually connected.

#### 1.3 Implementing the operations

- Find query. Check if two objects are in the same component.
- Union command. Replace components containing two objects with their union.

#### 1.4 Union-find data type (API)

**Goal**. Design efficient data structure for union-find.
- Number of objects N can be huge.
- Number of operations M can be huge.
- Find queries and union commands may be intermixed.

public class UF | /
---|---
**UF(int N)** | initialize union-find data structure with N objects (0 to N – 1)
**void union(int p, int q)** | add connection between p and q
**boolean connected(int p, int q)** | are p and q in the same component?
int find(int p) | component identifier for p (0 to N – 1)
int count() | number of components

## 2. quick find

#### 2.1 Quick-find *[eager approach]*

**Data structure**
- Integer array id[] of length N.
- Interpretation: p and q are connected iff they have the same id.

**Find**. Check if p and q have the same id.

**Union**. To merge components containing p and q, change all entries whose id equals id[p] to id[q].

#### 2.2 Quick-find: Java implementation

```java
public class QuickFindUF
{
    private int[] id;
    
    //set id of each object to itself
    //(N array accesses)
    public QuickFindUF(int N)
    {
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }
    
    //check whether p and q are in the same component
    //(2 array accesses)
    public boolean connected(int p, int q)
    { return id[p] == id[q]; }
    
    //change all entries with id[p] to id[q]
    //(at most 2N + 2 array accesses)
    public void union(int p, int q)
    {
        int pid = id[p];
        int qid = id[q];
        for (int i = 0; i < id.length; i++)
            if (id[i] == pid)
                id[i] = qid;
    }
}
```

#### 2.3 Quick-find is too slow

**Cost model**. Number of array accesses (for read or write).

algorithm | initialize | union | find
---|---|---|---
quick-find | N | N | 1

**Union is too expensive**. It takes `$N^2$` array accesses to process a sequence of N union commands on N objects.

## 3. quick union

#### 3.1 Quick-union *[lazy approach]*

**Data structure.**
- Integer array id[] of length N.
- Interpretation: id[i] is parent of i.
- **Root** of i is id[id[id[...id[i]...]]].
    > keep going until it doesn’t change (algorithm ensures no cycles)

**Find.** Check if p and q have the same root.

**Union**. To merge components containing p and q,
set the id of p's root to the id of q's root.

#### 3.2 Quick-union: Java implementation

```java
public class QuickUnionUF
{
    private int[] id;
    
    //set id of each object to itself
    //(N array accesses)
    public QuickUnionUF(int N)
    {
        id = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
    }
    
    //chase parent pointers until reach root
    //(depth of i array accesses)
    private int root(int i)
    {
        while (i != id[i])
            i = id[i];
        return i;
    }
    
    //check if p and q have same root
    //(depth of p and q array accesses)
    public boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }
    
    //change root of p to point to root of q
    //(depth of p and q array accesses)
    public void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);
        id[i] = j;
    }
}
```

#### 3.3 Quick-union is also too slow

**Cost model**. Number of array accesses (for read or write).

algorithm | initialize | union | find
---|---|---|---
quick-find | N | N | 1
quick-union | N | N+ *(includes cost of finding roots)* | N *(worst case)*

**Quick-find defect.**
- Union too expensive (N array accesses).
- Trees are flat, but too expensive to keep them flat.

**Quick-union defect.**
- Trees can get tall.
- Find too expensive (could be N array accesses).

## 4. improvements

### 4.1 Improvement 1: weighting

#### 4.1.1 Weighted quick-union.
- Modify quick-union to avoid tall trees.
- Keep track of size of each tree (number of objects).
- Balance by linking root of smaller tree to root of larger tree.

#### 4.1.2 Weighted quick-union: Java implementation

**Data structure.** Same as quick-union, but maintain extra array sz[i] to count number of objects in the tree rooted at i.

**Find.** Identical to quick-union.

**Union.** Modify quick-union to:
- Link root of smaller tree to root of larger tree.
- Update the sz[] array.

```java
    int i = root(p);
    int j = root(q);
    
    if (i == j) return;
    if (sz[i] < sz[j]) 
    {
        id[i] = j; sz[j] += sz[i];
    }
    else 
    {
        id[j] = i; sz[i] += sz[j];
    }
```

#### 4.1.3 Weighted quick-union analysis

**Running time.**
- Find: takes time proportional to depth of p and q.
- Union: takes constant time, given roots.

**Proposition**. Depth of any node x is at most lg N.

**Pf.** When does depth of x increase?

Increases by 1 when tree T1 containing x is merged into another tree T2.
- The size of the tree containing x at least doubles since | T 2 | ≥ | T 1 |.
- Size of tree containing x can double at most lg N times.

algorithm | initialize | union | find
---|---|---|---
quick-find | N | N | 1
quick-union | N | N+ *(includes cost of finding roots)* | N *(worst case)*
weighted QU | N | lg N+ *(includes cost of finding roots)* | lg N

### 4.2 Improvement 2: path compression

#### 4.2.1 Quick union with path compression.

Just after computing the root of p, set the id of each examined node to point to that root.

#### 4.2.2 Path compression: Java implementation

**Two-pass implementation**: add second loop to root() to set the id[] of each examined node to the root.

**Simpler one-pass variant**: Make every other node in path point to its grandparent (thereby halving path length).

```
private int root(int i)
{
    while (i != id[i])
    {
        id[i] = id[id[i]];  //only one extra line of code!
        i = id[i];
    }
    return i;
}
```

**In practice.** No reason not to! Keeps tree almost completely flat.

#### 4.2.3 Weighted quick-union with path compression: amortized analysis

**Proposition**. *[Hopcroft-Ulman, Tarjan]* Starting from an empty data structure, any sequence of M union-find ops on N objects makes ≤ c ( N + M lg* N ) array accesses.
- Analysis can be improved to N + M α(M, N).
- Simple algorithm with fascinating mathematics.

N | lg* N
---|---
1 | 0
2 | 1
4 | 2
16 | 3
65536 | 4
265536 | 5

**Linear-time algorithm for M union-find ops on N objects?**
- Cost within constant factor of reading in the data.
- In theory, WQUPC is not quite linear.
- In practice, WQUPC is linear.

**Amazing fact**. *[Fredman-Saks]* No linear-time algorithm exists.

### 4.3 Summary

**Bottom line.** Weighted quick union (with path compression) makes it possible to solve problems that could not otherwise be addressed.

algorithm | worst-case time
---|---
quick-find | M N
quick-union | M N
weighted QU | N + M log N
QU + path compression | N + M log N
weighted QU + path compression | N + M lg* N

## 5. applications

#### 5.1 Percolation

**A model for many physical systems:**
- N-by-N grid of sites.
- Each site is open with probability p (or blocked with probability 1 – p).
- System percolates iff top and bottom are connected by open sites.

#### 5.2 Likelihood of percolation

Depends on site vacancy probability p.

![image](https://s1.ax1x.com/2018/09/11/iFOsoD.png)

#### 5.3 Percolation phase transition

When N is large, theory guarantees a sharp threshold p*.
- p > p*: almost certainly percolates.
- p < p*: almost certainly does not percolate.

![image](https://s1.ax1x.com/2018/09/11/iFOcJH.png)

#### 5.4 Monte Carlo simulation

- Initialize N-by-N whole grid to be blocked.
- Declare random sites open until top connected to bottom.
- Vacancy percentage estimates p*.

#### 5.5 Dynamic connectivity solution to estimate percolation threshold

How to check whether an N-by-N system percolates?
- Create an object for each site and name them 0 to`$ N^2-1$`.
- Sites are in same component if connected by open sites.
- Percolates iff any site on bottom row is connected to site on top row.

**Clever trick**. Introduce 2 virtual sites (and connections to top and bottom).
- Percolates iff virtual top site is connected to virtual bottom site.

![image](https://s1.ax1x.com/2018/09/11/iFO6Fe.png)

How to model opening a new site?
- Mark new site as open; connect it to all of its adjacent open sites.
    > up to 4 calls to union()

#### 5.6 Percolation threshold

About 0.592746 for large square lattices.
> constant known only via simulation

![image](https://s1.ax1x.com/2018/09/11/iFOcJH.png)

## 6. Interview Questions

#### 6.1 Union-find with specific canonical element. 

Add a method `$find()$` to the union-find data type so that `$find(i)$` returns the largest element in the connected component containing `$i$`. The operations, `$union()$`, `$connected()$`, and `$find()$` should all take logarithmic time or better.

For example, if one of the connected components is `$\{1, 2, 6, 9\}$`, then the `$find()$` method should return 9 for each of the four elements in the connected components.

**Hint:** maintain an extra array to the weighted quick-union data structure that stores for each root `$i$` the large element in the connected component containing `i`.

#### 6.2 Successor with delete.

Given a set of n integers `S={0,1,...,n-1}` and a sequence of requests of the following form:
- Remove `x` from `S`
- Find the successor of `x`: the smallest `y` in `S` such that `y≥x`.

design a data type so that all operations (except construction) take logarithmic time or better in the worst case.

**Hint:** use the modification of the union−find data discussed in the previous question.
