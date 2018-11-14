# Analysis of Algorithms

## 1. introduction

### 1.1 Reasons to analyze algorithms
- Predict performance.
- Compare algorithms.
- Provide guarantees.
- Understand theoretical basis.
- **Primary practical reason**: avoid performance bugs.

### 1.2 Some algorithmic successes

- Discrete Fourier transform
    - Break down waveform of N samples into periodic components.
    - Applications: DVD, JPEG, MRI, astrophysics, ….
    - Brute force: N 2 steps.
    - FFT algorithm: N log N steps, **enables new technology**.
- N-body simulation.
    - Simulate gravitational interactions among N bodies.
    - Brute force: N 2 steps.
    - Barnes-Hut algorithm: N log N steps, **enables new research**.

### 1.3 Scientific method applied to analysis of algorithms

A framework for predicting performance and comparing algorithms.

##### Scientific method.
- **Observe** some feature of the natural world.
- **Hypothesize** a model that is consistent with the observations.
- **Predict** events using the hypothesis.
- **Verify** the predictions by making further observations.
- **Validate** by repeating until the hypothesis and observations agree.

##### Principles.
- Experiments must be **reproducible**（可复现）.
- Hypotheses must be **falsifiable**（可证伪）.

## 2. observations

### 2.1 Example: 3-SUM

Given N distinct integers, how many triples sum to exactly zero?

brute-force algorithm：
```java
public class ThreeSum
{
    public static int count(int[] a)
    {
        int N = a.length;
        int count = 0;
        
        //check each triple
        for (int i = 0; i < N; i++)
            for (int j = i+1; j < N; j++)
                for (int k = j+1; k < N; k++)   //for simplicity, ignore integer overflow
                    if (a[i] + a[j] + a[k] == 0)
                        count++;
        return count;
    }
    public static void main(String[] args)
    {
        int[] a = In.readInts(args[0]);
        StdOut.println(count(a));
    }
}
```

### 2.2 Measuring the running time

public class Stopwatch | /
---|---
Stopwatch() | *create a new stopwatch*
double elapsedTime() | *time since creation (in seconds)*

```java
public static void main(String[] args)
{
    int[] a = In.readInts(args[0]);
    Stopwatch stopwatch = new Stopwatch();
    StdOut.println(ThreeSum.count(a));
    double time = stopwatch.elapsedTime();
}
```

### 2.3 Empirical analysis

Run the program for various input sizes and measure running time.

N | time (seconds) †
---|---
250 | 0.0
500 | 0.0
1,000 | 0.1
2,000 | 0.8
4,000 | 6.4
8,000 | 51.1
16,000 | ?

### 2.4 Data analysis & Prediction and validation

#### Standard plot
![image](https://s1.ax2x.com/2018/09/27/5Ha25G.png)

#### Log-log plot
![image](https://s1.ax2x.com/2018/09/27/5Ha7Mn.png)

**Regression**. Fit straight line through data points: `$aN^b$`.

**Hypothesis**. The running time is about `$1.006 \times 10^{-10} \times N^{2.999}$` seconds.
> "order of growth" of running time is about `$N^3$` [stay tuned]

**Predictions**.
- 51.0 seconds for N = 8,000.
- 408.1 seconds for N = 16,000.

**Observations**
……

### 2.6 Doubling hypothesis

Quick way to estimate b in a power-law relationship.

Run program, doubling the size of the input. 

N | time (seconds) † | ratio | lg ratio
---|---|---|---
250 | 0.0 | 
500 | 0.0 | 4.8 | 2.3
1,000 | 0.1 | 6.9 | 2.8
2,000 | 0.8 | 7.7 | 2.9
4,000 | 6.4 | 8.0 | 3.0
8,000 | 51.1 | 8.0 | 3.0
> lg ratio seems to converge to a constant b ≈ 3

**Hypothesis**
- Running time is about `$aN^b$` with b = lg ratio.
- Run the program (for a sufficient large value of N) and solve for a.

### 2.7 Experimental algorithmics

##### System independent effects.
- Algorithm.
- Input data.
> determines exponent b in power law

##### System dependent effects.
- Hardware: CPU, memory, cache, …
- Software: compiler, interpreter, garbage collector, …
- System: operating system, network, other apps, …

> all above determines constant a in power law

**Bad news.** Difficult to get precise measurements.

**Good news.** Much easier and cheaper than other sciences.
> e.g., can run huge number of experiments

## 3. mathematical models

### 3.1 Mathematical models for running time

**Total running time**: sum of cost × frequency for all operations.
- Need to analyze program to determine set of operations.
- Cost depends on machine, compiler.
- Frequency depends on algorithm, input data.

**In principle**, accurate mathematical models are available.

### 3.2 Example

#### 3.2.1 Example: 1-SUM

How many instructions as a function of input size N ?
```java
int count = 0;
for (int i = 0; i < N; i++)
    if (a[i] == 0)
        count++;
```
operation | frequency
---|---
variable declaration | 2
assignment statement | 2
less than compare | N + 1
equal to compare | N
array access | N
increment | N to 2 N

#### 3.2.2 Example: 2-SUM

How many instructions as a function of input size N ?
```java
int count = 0;
for (int i = 0; i < N; i++)
    for (int j = i+1; j < N; j++)
        if (a[i] + a[j] == 0)
            count++;
```
operation | frequency
---|---
variable declaration | N + 2
assignment statement | N + 2
less than compare | ½ (N + 1) (N + 2)
equal to compare | ½ N (N − 1)
array access | N (N − 1)
increment | ½ N (N − 1) to N (N − 1)
> tedious to count exactly

### 3.3 Simplification

#### 3.3.1 Simplification 1: cost model

Use some basic operation as a proxy for running time.
```
if (a[i] + a[j] == 0)
```
cost model = array accesses
> we assume compiler/JVM do not optimize array accesses away!

#### 3.3.2 Simplification 2: tilde notation

- Estimate running time (or memory) as a function of input size N.
- Ignore lower order terms.
    - when N is large, terms are negligible
    - when N is small, we don't care

 **Technical definition.** `$f(N) \sim g(N)$` means `$ \lim\limits_{N\to\infty}\frac{f(N)}{g(N)}=1 $`

operation | frequency | tilde notation
---|---|---
variable declaration | N + 2 | ~ N
assignment statement | N + 2 | ~ N
less than compare | ½ (N + 1) (N + 2) | ~ `$\frac{1}{2}N^2$`
equal to compar |e ½ N (N − 1) | ~ `$\frac{1}{2}N^2$`
array access | N (N − 1) | ~ `$N^2$`
increment | ½ N (N − 1) to N (N − 1) | ~ `$\frac{1}{2}N^2$` to ~`$N^2$`

### 3.4 Mathematical models for running time

**In principle**, accurate mathematical models are available.

**In practice**,
- Formulas can be complicated.
- Advanced mathematics might be required.
- Exact models best left for experts.
```math
T_N = c_1 A + c_2 B + c_3 C + c_4 D + c_5 E
```
> - costs (depend on machine, compiler)
>     - `$c_1, c_2, c_3, c_4, c_5$`
> - frequencies (depend on algorithm, input)
>     - A = array access
>     - B = integer add
>     - C = integer compare
>     - D = increment
>     - E = variable assignment

**Bottom line.** We use approximate models in this course: `$T(N) \sim c N^3$`.

## 4. order-of-growth classifications

### 4.1 Common order-of-growth classifications

**Good news.** the small set of functions
```math
1, log N, N, N log N, N^2, N^3, and 2^N
```
suffices to describe order-of-growth of typical algorithms.
> order of growth discards leading coefficient

![image](https://s1.ax1x.com/2018/09/29/ilZpSP.png)

**Bottom line.** Need linear or linearithmic alg to keep pace with Moore's law.

### 4.2 Binary search demo

**Goal.** Given a sorted array and a key, find index of the key in the array?

**Binary search.** Compare key against middle entry.
- Too small, go left.
- Too big, go right.
- Equal, found.

##### Java implementation

```java
public static int binarySearch(int[] a, int key)
{
    int lo = 0, hi = a.length-1;
    while (lo <= hi)
    {
        int mid = lo + (hi - lo) / 2;
        if (key < a[mid]) hi = mid - 1;
        else if (key > a[mid]) lo = mid + 1;
        else return mid;
    }
    return -1;
}
```

**Invariant.** If key appears in the array a[], then a[lo] ≤ key ≤ a[hi].

### 4.3 Binary search: mathematical analysis

**Proposition.** Binary search uses at most 1 + lg N key compares to search in a sorted array of size N.

**Def.** T (N) ≡ # key compares to binary search a sorted subarray of size ≤ N.

**Binary search recurrence.** `$T (N) ≤ T (N / 2) + 1$` for `$N > 1$`, with `$T (1) = 1$`.
> `$T (N / 2)$` -> left or right half

**Pf sketch.**
```math
\begin{aligned}	 
T (N) &≤ T (N / 2) + 1\\
&≤ T (N / 4) + 1 + 1\\
&≤ T (N / 8) + 1 + 1 + 1\\
&...\\
&≤ T (N / N) + 1 + 1 + … + 1\\
&= 1 + lg N
\end{aligned}	 
```

### 4.4 An `$N^2 log N$` algorithm for 3-SUM

**Sorting-based algorithm.**
- Step 1: Sort the N (distinct) numbers.
- Step 2: For each pair of numbers a[i] and a[j], binary search for -(a[i] + a[j]).

**Analysis.** Order of growth is `$N^2 log N$`.
- Step 1: `$N^2$` with insertion sort.
- Step 2: `$N^2 log N$` with binary search.

## 5. theory of algorithms

### 5.1 Types of analyses

**Best case.** Lower bound on cost.
- Determined by “easiest” input.
- Provides a goal for all inputs.

**Worst case.** Upper bound on cost.
- Determined by “most difficult” input.
- Provides a guarantee for all inputs.

**Average case.** Expected cost for random input.
- Need a model for “random” input.
- Provides a way to predict performance.

> Ex. Compares for binary search.
> - Best: ~ 1
> - Average: ~ lg N
> - Worst: ~ lg N

##### Actual data might not match input model?
- Need to understand input to effectively process it.
- Approach 1: design for the worst case.
- Approach 2: randomize, depend on probabilistic guarantee.

### 5.2 Theory of algorithms

##### Goals.
- Establish “difficulty” of a problem.
- Develop “optimal” algorithms.

##### Approach.
- Suppress details in analysis: analyze “to within a constant factor”.
- Eliminate variability in input model by focusing on the worst case.

##### Optimal algorithm.
- Performance guarantee (to within a constant factor) for any input.
- No algorithm can provide a better performance guarantee.

### 5.3 Commonly-used notations in the theory of algorithms

![image](https://s1.ax1x.com/2018/09/29/ilmNqO.md.png)

### 5.4 Theory of algorithms: example

#### 5.4.1 example 1

##### Goals.
- Establish “difficulty” of a problem and develop “optimal” algorithms.
- Ex. 1-SUM = “Is there a 0 in the array? ”

**Upper bound**. A specific algorithm.
- Ex. Brute-force algorithm for 1-SUM: Look at every array entry.
- Running time of the optimal algorithm for 1-SUM is O(N ).

**Lower bound**. Proof that no algorithm can do better.
- Ex. Have to examine all N entries (any unexamined one might be 0).
- Running time of the optimal algorithm for 1-SUM is Ω(N ).

##### Optimal algorithm.
- Lower bound equals upper bound (to within a constant factor).
- Ex. Brute-force algorithm for 1-SUM is optimal: its running time is Θ(N ).

#### 5.4.2 example 2

##### Goals.
- Establish “difficulty” of a problem and develop “optimal” algorithms.
- Ex. 3-SUM.

**Upper bound.** A specific algorithm.
- Ex. Improved algorithm for 3-SUM.
- Running time of the optimal algorithm for 3-SUM is O(`$N^2 logN$` ).

**Lower bound.** Proof that no algorithm can do better.
- Ex. Have to examine all N entries to solve 3-SUM.
- Running time of the optimal algorithm for solving 3-SUM is Ω(N ).

##### Open problems.
- Optimal algorithm for 3-SUM?
- Subquadratic algorithm for 3-SUM?
- Quadratic lower bound for 3-SUM?

### 5.5 Algorithm design approach

##### Start.
- Develop an algorithm.
- Prove a lower bound.

##### Gap?
- Lower the upper bound (discover a new algorithm).
- Raise the lower bound (more difficult).

##### Golden Age of Algorithm Design.
- 1970s-.
- Steadily decreasing upper bounds for many important problems.
- Many known optimal algorithms.

##### Caveats.
- Overly pessimistic to focus on worst case?
- Need better than “to within a constant factor” to predict performance.

**Common mistake.** Interpreting big-Oh as an approximate model.

**This course.** Focus on approximate models: use Tilde-notation

## 6. memory

### 6.1 Basics

- **Bit.** 0 or 1.
- **Byte.** 8 bits.
- **Megabyte (MB).** 1 million or `$2^{20}$` bytes.
- **Gigabyte (GB).** 1 billion or `$2^{30}$` bytes.

**64-bit machine**. We assume a 64-bit machine with 8 byte pointers.
- Can address more memory.
- Pointers use more space.
    > some JVMs "compress" ordinary object pointers to 4 bytes to avoid this cost

### 6.2 Typical memory usage for primitive types and arrays

##### for primitive types
type | bytes
---|---
boolean | 1
byte | 1
char | 2
int | 4
float | 4
long | 8
double | 8

##### for one-dimensional arrays
type | bytes
---|---
char[] | 2N + 24
int[] | 4N + 24
double[] | 8N + 24

##### for two-dimensional arrays
type | bytes
---|---
char[][] | ~ 2 M N
int[][] | ~ 4 M N
double[][] | ~ 8 M N

### 6.3 Typical memory usage for objects in Java

- **Object overhead.** 16 bytes.
- **Reference.** 8 bytes.
- **Padding.** Each object uses a multiple of 8 bytes.

Ex 1. A Date object uses 32 bytes of memory.

![image](https://s1.ax1x.com/2018/09/29/ilnvB8.png)

Ex 2. A virgin String of length N uses ~ 2N bytes of memory.

![image](https://s1.ax1x.com/2018/09/29/ilnjnf.md.png)

### 6.4 Typical memory usage summary

##### Total memory usage for a data type value:
- Primitive type: 4 bytes for int, 8 bytes for double, …
- Object reference: 8 bytes.
- Array: 24 bytes + memory for each array entry.
- Object: 16 bytes + memory for each instance variable + 8 bytes if inner class (for pointer to enclosing class).
- Padding: round up to multiple of 8 bytes.

**Shallow memory usage**: Don't count referenced objects.

**Deep memory usage**: If array entry or instance variable is a reference,
add memory (recursively) for referenced object.

### 6.5 Example

How much memory does WeightedQuickUnionUF use as a function of N ?
> Use tilde notation to simplify your answer.

```
public class WeightedQuickUnionUF   //16 bytes (object overhead)
{
    private int[] id;   // 8 + (4N + 24) each reference + int[] array
    private int[] sz;
    private int count;  // 4 bytes (int)
    // 4 bytes (padding)
    
    public WeightedQuickUnionUF(int N)
    {
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++) id[i] = i;
        for (int i = 0; i < N; i++) sz[i] = 1;
    }
    ...
}
```
**A.** 8 N + 88 ~ 8 N bytes.

## 7. Turning the crank: summary

#### Empirical analysis.
- Execute program to perform experiments.
- Assume power law and formulate a hypothesis for running time.
- Model enables us to make predictions.

#### Mathematical analysis.
- Analyze algorithm to count frequency of operations.
- Use tilde notation to simplify analysis.
- Model enables us to explain behavior.

#### Scientific method.
- Mathematical model is independent of a particular system; applies to machines not yet built.
- Empirical analysis is necessary to validate mathematical models and to make predictions.