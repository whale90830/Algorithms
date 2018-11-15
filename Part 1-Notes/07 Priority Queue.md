# Priority Queue

## 1. API and elementary implementations

### 1.1 Priority queue

- **Collections.** Insert and delete items. Which item to delete?
- **Stack.** Remove the item most recently added.
- **Queue.** Remove the item least recently added.
- **Randomized queue.** Remove a random item.
- **Priority queue.** Remove the largest (or smallest) item.

### 1.2 Priority queue API

**Requirement.** Generic items are Comparable.

![image](https://s1.ax1x.com/2018/11/01/iW5X4K.png)

### 1.3 Priority queue applications

- Event-driven simulation. [customers in a line, colliding particles]
- Numerical computation. [reducing roundoff error]
- Data compression. [Huffman codes]
- Graph searching. [Dijkstra's algorithm, Prim's algorithm]
- Number theory. [sum of powers]
- Artificial intelligence. [A* search]
- Statistics. [maintain largest M values in a sequence]
- Operating systems. [load balancing, interrupt handling]
- Discrete optimization. [bin packing, scheduling]
- Spam filtering. [Bayesian spam filter]

**Generalizes**: stack, queue, randomized queue.

### 1.4 Priority queue client example

**Challenge**. Find the largest `$M$` items in a stream of `$N$` items.
> N huge, M large
- Fraud detection: isolate $$ transactions.
- File maintenance: find biggest files or directories.

**Constraint**. Not enough memory to store `$N$` items.

```java
// Transaction data type is Comparable(ordered by $$)
MinPQ<Transaction> pq = new MinPQ<Transaction>();

while (StdIn.hasNextLine())
{
    String line = StdIn.readLine();
    Transaction item = new Transaction(line);
    pq.insert(item);
    if (pq.size() > M)  // pq contains largest M items
        pq.delMin();
}
```

![image](https://s1.ax1x.com/2018/11/01/iWICDA.png)

### 1.5 Priority queue: unordered array implementation

```java
public class UnorderedMaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq; // pq[i] = ith element on pq
    private int N; // number of elements on pq
    
    public UnorderedMaxPQ(int capacity) // no generic array creation
    { pq = (Key[]) new Comparable[capacity]; }
    
    public boolean isEmpty()
    { return N == 0; }
    
    public void insert(Key x)
    { pq[N++] = x; }
    
    public Key delMax()
    {
        int max = 0;
        for (int i = 1; i < N; i++)
            if (less(max, i)) max = i;
        exch(max, N-1);
        // less() and exch() similar to sorting methods
        return pq[--N];
    }
}
```

### 1.6 Priority queue elementary implementations

**Challenge**. Implement all operations efficiently.

![image](https://s1.ax1x.com/2018/11/01/iWIegg.png)

## 2. binary heaps

### 2.1 Complete binary tree

**Binary tree**. Empty or node with links to left and right binary trees.

**Complete tree**. Perfectly balanced, except for bottom level.

**Property**. Height of complete tree with`$ N$` nodes is `$\lfloor \lg N \rfloor$`

**Pf**. Height only increases when `$N$` is a power of 2.

### 2.2 Binary heap representations

**Binary heap**. Array representation of a heap-ordered complete binary tree.

##### Heap-ordered binary tree.
- Keys in nodes.
- Parent's key no smaller than children's keys.

##### Array representation.
- Indices start at 1.
- Take nodes in level order.
- No explicit links needed!

![image](https://s1.ax1x.com/2018/11/01/iWoEZR.png)

### 2.3 Binary heap properties

**Proposition**. Largest key is ```a[1]```, which is root of binary tree.

**Proposition**. Can use array indices to move through tree.
- Parent of node at ```k``` is at ```k/2```.
- Children of node at ```k``` are at ```2k``` and ```2k+1```.

### 2.4 Promotion in a heap

**Scenario**. Child's key becomes larger key than its parent's key.

##### To eliminate the violation:
- Exchange key in child with key in parent.
- Repeat until heap order restored.

```java
private void swim(int k)
{
    while (k > 1 && less(k/2, k))
    {
        exch(k, k/2);
        k = k/2;
    }
}
```

![image](https://s1.ax1x.com/2018/11/01/iWocF0.png)

**Peter principl**e. Node promoted to level of incompetence.

### 2.5 Insertion in a heap

**Insert**. Add node at end, then swim it up.

**Cost**. At most `$1 + \lg N$` compares.

```java
public void insert(Key x)
{
    pq[++N] = x;
    swim(N);
}
```

![image](https://s1.ax1x.com/2018/11/01/iWoowR.png)

### 2.6 Demotion in a heap

**Scenario**. Parent's key becomes smaller than one (or both) of its children's.

##### To eliminate the violation:
- Exchange key in parent with key in larger child.
- Repeat until heap order restored.

```java
private void sink(int k)
{
    while (2*k <= N)
    {
        int j = 2*k;
        if (j < N && less(j, j+1)) j++;
        if (!less(k, j)) break;
        exch(k, j);
        k = j;
    }
}
```

![image](https://s1.ax1x.com/2018/11/01/iWozmd.png)

**Power struggle**. Better subordinate promoted.

### 2.7 Delete the maximum in a heap

**Delete max**. Exchange root with node at end, then sink it down.

**Cost**. At most `$2 \lg N$` compares.

```java
public Key delMax()
{
    Key max = pq[1];
    exch(1, N--);
    sink(1);
    pq[N+1] = null; // prevent loitering
    return max;
}
```

![image](https://s1.ax1x.com/2018/11/01/iWTE6g.png)

### 2.8 Binary heap demo

**Insert**. Add node at end, then swim it up.

**Remove the maximum**. Exchange root with node at end, then sink it down.

### 2.9 Binary heap: Java implementation

```java
public class MaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq;
    private int N;
    
    // fixed capacity (for simplicity)
    public MaxPQ(int capacity)
    { pq = (Key[]) new Comparable[capacity+1]; }
    
    // PQ ops
    public boolean isEmpty()
    { return N == 0; }
    public void insert(Key key)
    public Key delMax()
    { /* see previous code */ }
    
    // heap helper functions
    private void swim(int k)
    private void sink(int k)
    { /* see previous code */ }
    
    // array helper functions
    private boolean less(int i, int j)
    { return pq[i].compareTo(pq[j]) < 0; }
    private void exch(int i, int j)
    { Key t = pq[i]; pq[i] = pq[j]; pq[j] = t; }
}
```

### 2.10 Priority queues implementation cost summary

![image](https://s1.ax1x.com/2018/11/01/ifI4kq.png)

### 2.11 Binary heap considerations

##### Immutability of keys.
- Assumption: client does not change keys while they're on the PQ.
- Best practice: use immutable keys.

##### Underflow and overflow.
- Underflow: throw exception if deleting from empty PQ.
- Overflow: add no-arg constructor and use resizing array.
> leads to `$\log N$` amortized time per op (how to make worst case?)

##### Minimum-oriented priority queue.
- Replace ```less()``` with ```greater()```.
- Implement ```greater()```.

##### Other operations.
- Remove an arbitrary item.
- Change the priority of an item.
> can implement with ```sink()``` and ```swim()``` [stay tuned]

### 2.12 Immutability: implementing in Java

**Data type**. Set of values and operations on those values.

**Immutable data type**. Can't change the data type value once created.

```java
// can't override instance methods
public final class Vector {
    // all instance variables private and final
    private final int N;
    private final double[] data;
    
    public Vector(double[] data) {
        this.N = data.length;
        this.data = new double[N];
        // defensive copy of mutable instance variables
        for (int i = 0; i < N; i++)
            this.data[i] = data[i];
    }
    
    …   // instance methods don't change instance variables
}
```

**Immutable**. String, Integer, Double, Color, Vector, Transaction, Point2D.

**Mutable**. StringBuilder, Stack, Counter, Java array.

### 2.13 Immutability: properties

##### Advantages.
- Simplifies debugging.
- Safer in presence of hostile code.
- Simplifies concurrent programming.
- Safe to use as key in priority queue or symbol table.

**Disadvantage**. Must create new object for each data type value.

## 3. heapsort

### 3.1 Heapsort

##### Basic plan for in-place sort.
- Create max-heap with all N keys.
- Repeatedly remove the maximum key.

### 3.2 Heapsort demo

**Heap construction**. Build max heap using bottom-up method.
> we assume array entries are indexed 1 to N

**Sortdown**. Repeatedly delete the largest remaining item.

### 3.3 Heapsort: heap construction

**First pass**. Build heap using bottom-up method.

```java
for (int k = N/2; k >= 1; k--)
    sink(a, k, N);
```

![image](https://s1.ax1x.com/2018/11/01/if7Pwd.png)

### 3.4 Heapsort: sortdown

##### Second pass.
- Remove the maximum, one at a time.
- Leave in array, instead of nulling out.

```java
while (N > 1)
{
    exch(a, 1, N--);
    sink(a, 1, N);
}
```

![image](https://s1.ax1x.com/2018/11/01/if7AYt.png)

### 3.5 Heapsort: Java implementation

```java
public class Heap
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int k = N/2; k >= 1; k--)
            sink(a, k, N);
        while (N > 1)
        {
            exch(a, 1, N);
            sink(a, 1, --N);
        }
    }
    
    private static void sink(Comparable[] a, int k, int N)
    { /* as before */ }
    
    // but convert from 1-based indexing to 0-base indexing
    private static boolean less(Comparable[] a, int i, int j)
    { /* as before */ }
    private static void exch(Comparable[] a, int i, int j)
    { /* as before */ }
}
```

### 3.6 Heapsort: mathematical analysis

**Proposition**. Heap construction uses `$\leq 2N$` compares and exchanges.

**Proposition**. Heapsort uses `$\leq 2N\lg N$` compares and exchanges.

**Significance**. In-place sorting algorithm with `$N \log N$` worst-case.
- Mergesort: no, linear extra space.
- Quicksort: no, quadratic time in worst case.
- Heapsort: yes!

**Bottom line**. Heapsort is optimal for both time and space, but:
- Inner loop longer than quicksort’s.
- Makes poor use of cache memory.
- Not stable.

### 3.7 Sorting algorithms: summary

![image](https://s1.ax1x.com/2018/11/01/if75tI.png)

## 4. event-driven simulation

### 4.1 Molecular dynamics simulation of hard discs

**Goal**. Simulate the motion of `$N$` moving particles that behave according to the laws of elastic collision.

##### Hard disc model.
- Moving particles interact via elastic collisions with each other and walls.
- Each particle is a disc with known position, velocity, mass, and radius.
- No other forces.

**Significance**. Relates macroscopic observables to microscopic dynamics.
- Maxwell-Boltzmann: distribution of speeds as a function of temperature.
- Einstein: explain Brownian motion of pollen grains.

### 4.2 Warmup: bouncing balls

**Time-driven simulation**. `$N$` bouncing balls in the unit square.

```java
public class BouncingBalls
{
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        Ball[] balls = new Ball[N];
        for (int i = 0; i < N; i++)
            balls[i] = new Ball();
        while(true)
        {
            // main simulation loop
            StdDraw.clear();
            for (int i = 0; i < N; i++)
            {
                balls[i].move(0.5);
                balls[i].draw();
            }
            StdDraw.show(50);
        }
    }
}
```

```java
public class Ball
{
    private double rx, ry; // position
    private double vx, vy; // velocity
    private final double radius; // radius
    public Ball(...)
    { /* initialize position and velocity */ }
    
    public void move(double dt)
    {
        // check for collision with walls
        if ((rx + vx*dt < radius) || (rx + vx*dt > 1.0 - radius)) { vx = -vx; }
        if ((ry + vy*dt < radius) || (ry + vy*dt > 1.0 - radius)) { vy = -vy; }
        rx = rx + vx*dt;
        ry = ry + vy*dt;
    }
    public void draw()
    { StdDraw.filledCircle(rx, ry, radius); }
}
```

**Missing**. Check for balls colliding with each other.
- Physics problems: when? what effect?
- CS problems: which object does the check? too many checks?

### 4.3 Time-driven simulation

- Discretize time in quanta of size dt.
- Update the position of each particle after every dt units of time, and check for overlaps.
- If overlap, roll back the clock to the time of the collision, update the velocities of the colliding particles, and continue the simulation.

![image](https://s1.ax1x.com/2018/11/02/ihOsu6.png)

##### Main drawbacks.
- `$\sim N^2/2$` overlap checks per time quantum.
- Simulation is too slow if ```dt``` is very small.
- May miss collisions if ```dt``` is too large. (if colliding particles fail to overlap when we are looking)

![image](https://s1.ax1x.com/2018/11/02/ihO6HO.png)

### 4.4 Event-driven simulation

##### Change state only when something happens.
- Between collisions, particles move in straight-line trajectories.
- Focus only on times when collisions occur.
- Maintain PQ of collision events, prioritized by time.
- Remove the min = get next collision.

**Collision prediction**. Given position, velocity, and radius of a particle, when will it collide next with a wall or another particle?

**Collision resolution**. If collision occurs, update colliding particle(s) according to laws of elastic collisions.

![image](https://s1.ax1x.com/2018/11/02/ihOR4H.png)

### 4.5 Particle-wall collision

##### Collision prediction and resolution.
- Particle of radius s at position (rx, ry).
- Particle moving in unit box with velocity (vx, vy).
- Will it collide with a vertical wall? If so, when?

![image](https://s1.ax1x.com/2018/11/02/ihOTDf.png)

### 4.6 Particle-particle collision prediction

##### Collision prediction.
- Particle `$i$`: radius `$s_i$`, position `$(rx_i, ry_i)$`, velocity `$(vx_i, vy_i)$`.
- Particle `$j$`: radius `$s_j$`, position `$(rx_j, ry_j)$`, velocity `$(vx_j, vy_j)$`.
- Will particles `$i$` and `$j$` collide? If so, when?

![image](https://s1.ax1x.com/2018/11/02/ihObVS.png)

![image](https://s1.ax1x.com/2018/11/02/ihXuqK.png)

### 4.7 Particle-particle collision resolution

**Collision resolution**. When two particles collide, how does velocity change?

![image](https://s1.ax1x.com/2018/11/02/ihXV2R.png)

### 4.8 Particle data type skeleton

```java
public class Particle
{
    private double rx, ry; // position
    private double vx, vy; // velocity
    private final double radius; // radius
    private final double mass; // mass
    private int count; // number of collisions
    
    public Particle(...) { }
    
    public void move(double dt) { }
    public void draw() { }
    
    // predict collision with particle or wall
    public double timeToHit(Particle that) { }
    public double timeToHitVerticalWall() { }
    public double timeToHitHorizontalWall() { }
    
    // resolve collision with particle or wall
    public void bounceOff(Particle that) { }
    public void bounceOffVerticalWall() { }
    public void bounceOffHorizontalWall() { }
}
```

### 4.9 Particle-particle collision and resolution implementation

```java
public double timeToHit(Particle that)
{
    if (this == that) return INFINITY;
    double dx = that.rx - this.rx, dy = that.ry - this.ry;
    double dvx = that.vx - this.vx; dvy = that.vy - this.vy;
    double dvdr = dx*dvx + dy*dvy;
    if( dvdr > 0) return INFINITY;
    double dvdv = dvx*dvx + dvy*dvy;
    double drdr = dx*dx + dy*dy;
    double sigma = this.radius + that.radius;
    double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
    if (d < 0) return INFINITY;
    return -(dvdr + Math.sqrt(d)) / dvdv;
}
public void bounceOff(Particle that)
{
    double dx = that.rx - this.rx, dy = that.ry - this.ry;
    double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
    double dvdr = dx*dvx + dy*dvy;
    double dist = this.radius + that.radius;
    double J = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
    double Jx = J * dx / dist;
    double Jy = J * dy / dist;
    this.vx += Jx / this.mass;
    this.vy += Jy / this.mass;
    that.vx -= Jx / that.mass;
    that.vy -= Jy / that.mass;
    this.count++;
    that.count++;
}
```

### 4.10 Collision system: event-driven simulation main loop

##### Initialization.
- Fill PQ with all potential particle-wall collisions.
- Fill PQ with all potential particle-particle collisions.
> “potential” since collision may not happen if
some other collision intervenes

##### Main loop.
- Delete the impending event from PQ (min priority = t).
- If the event has been invalidated, ignore it.
- Advance all particles to time t, on a straight-line trajectory.
- Update the velocities of the colliding particle(s).
- Predict future particle-wall and particle-particle collisions involving the colliding particle(s) and insert events onto PQ.

### 4.11 Event data type

##### Conventions.
- Neither particle null ⇒ particle-particle collision.
- One particle null ⇒ particle-wall collision.
- Both particles null ⇒ redraw event.

```java
private class Event implements Comparable<Event>
{
    private double time; // time of event
    private Particle a, b; // particles involved in event
    private int countA, countB; // collision counts for a and b
    
    // create event
    public Event(double t, Particle a, Particle b) { }
    
    // ordered by time
    public int compareTo(Event that)
    { return this.time - that.time; }
    
    // invalid if intervening collision
    public boolean isValid()
    { }
}
```

### 4.12 Collision system implementation: skeleton

```java
public class CollisionSystem
{
    private MinPQ<Event> pq; // the priority queue
    private double t = 0.0; // simulation clock time
    private Particle[] particles; // the array of particles
    
    public CollisionSystem(Particle[] particles) { }
    
    // add to PQ all particle-wall and particleparticle collisions involving this particle
    private void predict(Particle a)
    {
        if (a == null) return;
        for (int i = 0; i < N; i++)
        {
            double dt = a.timeToHit(particles[i]);
            pq.insert(new Event(t + dt, a, particles[i]));
        }
        pq.insert(new Event(t + a.timeToHitVerticalWall() , a, null));
        pq.insert(new Event(t + a.timeToHitHorizontalWall(), null, a));
    }
    
    private void redraw() { }
    
    public void simulate() { /* see next slide */ }
}
```

### 4.13 Collision system implementation: main event-driven simulation loop

```java
public void simulate()
{
    // initialize PQ with collision events and redraw event
    pq = new MinPQ<Event>();
    for(int i = 0; i < N; i++) predict(particles[i]);
    pq.insert(new Event(0, null, null));
    
    while(!pq.isEmpty())
    {
        // get next event
        Event event = pq.delMin();
        if(!event.isValid()) continue;
        Particle a = event.a;
        Particle b = event.b;
        
        // update positions and time
        for(int i = 0; i < N; i++)
            particles[i].move(event.time - t);
        t = event.time;
        
        // process event
        if (a != null && b != null) a.bounceOff(b);
        else if (a != null && b == null) a.bounceOffVerticalWall()
        else if (a == null && b != null) b.bounceOffHorizontalWall();
        else if (a == null && b == null) redraw();
        
        // predict new events based on changes
        predict(a);
        predict(b);
    }
}
```