# Geometric Applications of BSTs

### Overview

**This lecture**. Intersections among geometric objects.

![image](https://s1.ax1x.com/2018/11/24/FFyBSP.png)

**Applications**. CAD, games, movies, virtual reality, databases, GIS, .…

**Efficient solutions**. Binary search trees (and extensions).

## 1. 1d range search

### 1.1 1d range search

##### Extension of ordered symbol table.
- Insert key-value pair.
- Search for key k.
- Delete key k.
- **Range search**: find all keys between k1 and k2.
- **Range count**: number of keys between k1 and k2.

**Application**. Database queries.

##### Geometric interpretation.
- Keys are point on a line.
- Find/count points in a given 1d interval.

### 1.2 1d range search: elementary implementations

**Unordered list**. Fast insert, slow range search.

**Ordered array**. Slow insert, binary search for `$k_1$` and `$k_2$` to do range search.

![image](https://s1.ax1x.com/2018/11/24/FFywWt.png)

### 1.3 1d range count: BST implementation

**1d range count**. How many keys between ```lo``` and ```hi``` ?

![image](https://s1.ax1x.com/2018/11/24/FFysOS.png)

```java
public int size(Key lo, Key hi)
{
    if (contains(hi)) return rank(hi) - rank(lo) + 1;
    else return rank(hi) - rank(lo);
}
```

**Proposition**. Running time proportional to `$\log N$`.

**Pf**. Nodes examined = search path to ```lo``` + search path to ```hi```.

**1d range search**. Find all keys between ```lo``` and ```hi```.
- Recursively find all keys in left subtree (if any could fall in range).
- Check key in current node.
- Recursively find all keys in right subtree (if any could fall in range).

![image](https://s1.ax1x.com/2018/11/24/FFycwQ.png)

**Proposition**. Running time proportional to `$R + \log N$`.

**Pf**. Nodes examined = search path to ```lo``` + search path to ```hi``` + matches.

## 2. line segment intersection

### 2.1 Orthogonal line segment intersection

Given `$N$` horizontal and vertical line segments, find all intersections.

![image](https://s1.ax1x.com/2018/11/24/FFy4S0.png)

**Quadratic algorithm**. Check all pairs of line segments for intersection.

**Nondegeneracy assumption**. All x- and y-coordinates are distinct.

### 2.2 Orthogonal line segment intersection: sweep-line algorithm

##### Sweep vertical line from left to right.
- x-coordinates define events.
- h-segment (left endpoint): insert y-coordinate into BST.
- h-segment (right endpoint): remove y-coordinate from BST.
- v-segment: range search for interval of y-endpoints.

![image](https://s1.ax1x.com/2018/11/24/FF6Fkd.png)

### 2.3 Orthogonal line segment intersection: sweep-line analysis

**Proposition**. The sweep-line algorithm takes time proportional to `$N \log N + R$` to find all R intersections among N orthogonal line segments.

**Pf**.
- Put x-coordinates on a PQ (or sort).
- Insert y-coordinates into BST.
- Delete y-coordinates from BST.
- Range searches in BST.

**Bottom line**. Sweep line reduces 2d orthogonal line segment intersection search to 1d range search.

## 3. kd trees

### 3.1 2-d orthogonal range search

##### Extension of ordered symbol-table to 2d keys.
- Insert a 2d key.
- Delete a 2d key.
- Search for a 2d key.
- **Range search**: find all keys that lie in a 2d range.
- **Range count**: number of keys that lie in a 2d range.

**Applications**. Networking, circuit design, databases, ...

##### Geometric interpretation.
- Keys are point in the plane.
- Find/count points in a given h-v rectangle
    > rectangle is axis-aligned

### 3.2 2d orthogonal range search: grid implementation

##### Grid implementation.
- Divide space into M-by-M grid of squares.
- Create list of points contained in each square.
- Use 2d array to directly index relevant square.
- Insert: add (x, y) to list for corresponding square.
- Range search: examine only squares that intersect 2d range query.

![image](https://s1.ax1x.com/2018/11/24/FFh929.png)

### 3.3 2d orthogonal range search: grid implementation analysis

##### Space-time tradeoff.
- Space: `$M^2 + N$`.
- Time: `$1 + N / M^2$` per square examined, on average.

##### Choose grid square size to tune performance.
- Too small: wastes space.
- Too large: too many points per square.
- Rule of thumb: √N-by-√N grid.

**Running time**. [if points are evenly distributed]
> choose M ~ √N

- Initialize data structure: N.
- Insert point: 1.
- Range search: 1 per point in range.

### 3.4 Clustering

**Grid implementation**. Fast, simple solution for evenly-distributed points.

**Problem**. Clustering a well-known phenomenon in geometric data.
- Lists are too long, even though average length is short.
- Need data structure that adapts gracefully to data.

![image](https://s1.ax1x.com/2018/11/24/FFhFDx.png)

### 3.5 Space-partitioning trees

Use a tree to represent a recursive subdivision of 2d space.

**Grid**. Divide space uniformly into squares.

**2d tree**. Recursively divide space into two halfplanes.

**Quadtree**. Recursively divide space into four quadrants.

**BSP tree**. Recursively divide space into two regions.

![image](https://s1.ax1x.com/2018/11/24/FFhVUO.png)

### 3.6 Space-partitioning trees: applications

##### Applications.
- Ray tracing.
- 2d range search.
- Flight simulators.
- N-body simulation.
- Collision detection.
- Astronomical databases.
- Nearest neighbor search.
- Adaptive mesh generation.
- Accelerate rendering in Doom.
- Hidden surface removal and shadow casting.

### 3.7 2d tree construction

Recursively partition plane into two halfplanes.

![image](https://s1.ax1x.com/2018/11/24/FFhu2d.png)

### 3.8 2d tree implementation

**Data structure**. BST, but alternate using x- and y-coordinates as key.
- Search gives rectangle containing point.
- Insert further subdivides the plane.

![image](https://s1.ax1x.com/2018/11/25/Fklf1I.png)

### 3.9 Range search in a 2d tree demo

**Goal**. Find all points in a query axis-aligned rectangle.
- Check if point in node lies in given rectangle.
- Recursively search left/bottom (if any could fall in rectangle).
- Recursively search right/top (if any could fall in rectangle).

![image](https://s1.ax1x.com/2018/11/25/FklInf.png)

### 3.10 Range search in a 2d tree analysis

**Typical case**. `$R + \log N$`.

**Worst case (assuming tree is balanced)**.`$ R + \sqrt N$`.

### 3.11 Nearest neighbor search in a 2d tree demo

**Goal**. Find closest point to query point.
- Check distance from point in node to query point.
- Recursively search left/bottom (if it could contain a closer point).
- Recursively search right/top (if it could contain a closer point).
- Organize method so that it begins by searching for query point.

![image](https://s1.ax1x.com/2018/11/25/FklzuV.png)

### 3.12 Nearest neighbor search in a 2d tree analysis

**Typical case**.   `$\log N$`.

**Worst case (even if tree is balanced)**. `$N$`.

### 3.13 Flocking birds
**Q**. What "natural algorithm" do starlings, migrating geese, starlings,
cranes, bait balls of fish, and flashing fireflies use to flock?

### 3.14 Flocking boids [Craig Reynolds, 1986]
**Boids**. Three simple rules lead to complex emergent flocking behavior:
- Collision avoidance: point away from k nearest boids.
- Flock centering: point towards the center of mass of k nearest boids.
- Velocity matching: update velocity to the average of k nearest boids.

### 3.15 Kd tree

**Kd tree**. Recursively partition k-dimensional space into 2 halfspaces.

**Implementation**. BST, but cycle through dimensions ala 2d trees.

![image](https://s1.ax1x.com/2018/11/25/Fk1pHU.png)

##### Efficient, simple data structure for processing k-dimensional data.
- Widely used.
- Adapts well to high-dimensional and clustered data.
- Discovered by an undergrad in an algorithms class!

### 3.16 N-body simulation

**Goal**. Simulate the motion of N particles, mutually affected by gravity.

**Brute force**. For each pair of particles, compute force:`$F=\frac{Gm_1m_2}{r^2}$`

**Running time**. Time per step is `$N^2$`.

### 3.17 Appel's algorithm for N-body simulation

**Key idea**. Suppose particle is far, far away from cluster of particles.
- Treat cluster of particles as a single aggregate particle.
- Compute force between particle and center of mass of aggregate.

##### Appel's algorithm for N-body simulation
- Build 3d-tree with N particles as nodes.
- Store center-of-mass of subtree in each node.
- To compute total force acting on a particle, traverse tree, but stop as soon as distance from particle to subdivision is sufficiently large.

**Impact**. Running time per step is `$N \log N$` ⇒ enables new research.

## 4. interval search trees

### 4.1 1d interval search

**1d interval search**. Data structure to hold set of (overlapping) intervals.
- Insert an interval ( `lo`,` hi` ).
- Search for an interval ( `lo`,` hi` ).
- Delete an interval ( `lo`,` hi` ).
- Interval intersection query: given an interval ( `lo`,` hi` ), find all intervals (or one interval) in data structure that intersects ( `lo`,` hi` ).

**Q**. Which intervals intersect ( 9, 16 ) ?
**A**. ( 7, 10 ) and ( 15, 18 ).

![image](https://s1.ax1x.com/2018/11/25/Fk1mDK.png)

### 4.2 1d interval search API

![image](https://s1.ax1x.com/2018/11/25/Fk1nHO.png)

**Nondegeneracy assumption**. No two intervals have the same left endpoint.

### 4.3 Interval search trees

Create BST, where each node stores an interval ( `lo`, `hi` ).
- Use left endpoint as BST key.
- Store max endpoint in subtree rooted at node.

![image](https://s1.ax1x.com/2018/11/25/Fk1Gvt.png)

### 4.4 Interval search tree demo

To insert an interval ( `lo`,` hi` ) :
- Insert into BST, using lo as the key.
- Update max in each node on search path.

To search for any one interval that intersects query interval ( `lo`,` hi` ) :
- If interval in node intersects query interval, return it.
- Else if left subtree is null, go right.
- Else if max endpoint in left subtree is less than lo, go right.
- Else go left.

### 4.5 Search for an intersecting interval implementation

```java
Node x = root;
while (x != null)
{
    if (x.interval.intersects(lo, hi)) return x.interval;
    else if (x.left == null) x = x.right;
    else if (x.left.max < lo) x = x.right;
    else x = x.left;
}
return null;
```

### 4.6 Search for an intersecting interval analysis

**Case 1**. If search goes right, then no intersection in left.

**Pf**. Suppose search goes right and left subtree is non empty.
- Max endpoint max in left subtree is less than lo.
- For any interval (a, b) in left subtree of x, we have b ≤ max < lo.
    > - b ≤ max: definition of max
    > - max < lo: reason for going right

![image](https://s1.ax1x.com/2018/11/25/FkzxSA.png)

**Case 2**. If search goes left, then there is either an intersection in left subtree or no intersections in either.

**Pf**. Suppose no intersection in left.
- Since went left, we have lo ≤ max.
- Then for any interval (a, b) in right subtree of x, hi < c ≤ a ⇒ no intersection in right.
    > - hi < c: no intersections in left subtree
    > - c ≤ a: intervals sorted by left endpoint

![image](https://s1.ax1x.com/2018/11/25/FASpOP.png)

### 4.7 Interval search tree: analysis

**Implementation**. Use a red-black BST to guarantee performance.
> easy to maintain auxiliary information using log N extra work per op

![image](https://s1.ax1x.com/2018/11/25/FASCef.png)

## 5. rectangle intersection

### 5.1 Orthogonal rectangle intersection

**Goal**. Find all intersections among a set of N orthogonal rectangles.

**Quadratic algorithm**. Check all pairs of rectangles for intersection.

![image](https://s1.ax1x.com/2018/11/25/FApBD0.png)

**Non-degeneracy assumption**. All x- and y-coordinates are distinct.

### 5.2 Microprocessors and geometry

**Early 1970s**. microprocessor design became a geometric problem.
- Very Large Scale Integration (VLSI).
- Computer-Aided Design (CAD).

##### Design-rule checking.
- Certain wires cannot intersect.
- Certain spacing needed between different types of wires.
- Debugging = orthogonal rectangle intersection search.

### 5.3 Algorithms and Moore's law

"**Moore’s law.**" Processing power doubles every 18 months.
- 197x: check N rectangles.
- 197(x+1.5): check 2 N rectangles on a 2x-faster computer.

**Bootstrapping**. We get to use the faster computer for bigger circuits.

But bootstrapping is not enough if using a quadratic algorithm:
- 197x: takes M days.
- 197(x+1.5): takes (4 M) / 2 = 2 M days. (!)
    > - 4 M: quadratic algorithm
    > - / 2: 2x-faster computer

**Bottom line**. Linearithmic algorithm is necessary to sustain Moore’s Law.

### 5.4 Orthogonal rectangle intersection: sweep-line algorithm

##### Sweep vertical line from left to right.
- x-coordinates of left and right endpoints define events.
- Maintain set of rectangles that intersect the sweep line in an interval search tree (using y-intervals of rectangle).
- Left endpoint: interval search for y-interval of rectangle; insert y-interval.
- Right endpoint: remove y-interval.

![image](https://s1.ax1x.com/2018/11/25/FApyUU.png)

### 5.5 Orthogonal rectangle intersection: sweep-line analysis

**Proposition**. Sweep line algorithm takes time proportional to `$N \log N + R \log N$` to find R intersections among a set of N rectangles.

**Pf**.
- Put x-coordinates on a PQ (or sort).  `$N \log N$`
- Insert y-intervals into ST.   `$N \log N$`
- Delete y-intervals from ST.   `$N \log N$`
- Interval searches for y-intervals.    `$N \log N + R \log N$`

**Bottom line**. Sweep line reduces 2d orthogonal rectangle intersection
search to 1d interval search.

### 5.6 Geometric applications of BSTs

![image](https://s1.ax1x.com/2018/11/25/FApR29.png)