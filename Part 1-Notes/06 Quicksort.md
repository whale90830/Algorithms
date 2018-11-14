# Quicksort

### Two classic sorting algorithms

##### Critical components in the world’s computational infrastructure.
- Full scientific understanding of their properties has enabled us to develop them into practical system sorts.
- Quicksort honored as one of top 10 algorithms of 20th century in science and engineering.

##### Mergesort. [last lecture]
- Java sort for objects.
- Perl, C++ stable sort, Python stable sort, Firefox JavaScript, ...

##### Quicksort. [this lecture]
- Java sort for primitive types.
- C qsort, Unix, Visual C++, Python, Matlab, Chrome JavaScript, ...

## 1. quicksort

### 1.1 Quicksort

##### Basic plan.
- **Shuffle** the array.
- **Partition** so that, for some ```j```
    - entry ```a[j]``` is in place
    - no larger entry to the left of ```j```
    - no smaller entry to the right of ```j```
- **Sort** each piece recursively.

### 1.2 Quicksort partitioning demo

##### Repeat until i and j pointers cross.
- Scan ```i``` from left to right so long as (```a[i] < a[lo]```).
- Scan ```j``` from right to left so long as (```a[j] > a[lo]```).
- Exchange ```a[i]``` with ```a[j]```.

##### When pointers cross.
- Exchange ```a[lo]``` with ```a[j]```.

### 1.3 Quicksort: Java code for partitioning

```java
private static int partition(Comparable[] a, int lo, int hi)
{
    int i = lo, j = hi+1;
    while (true)
    {
        // find item on left to swap
        while (less(a[++i], a[lo]))
            if (i == hi) break;
        // find item on right to swap
        while (less(a[lo], a[--j]))
            if (j == lo) break;
        if (i >= j) break;  // check if pointers cross
        exch(a, i, j);  // swap
    }
    exch(a, lo, j); // swap with partitioning item
    return j; // return index of item now known to be in place
}
```

### 1.4 Quicksort: Java implementation

```java
public class Quick
{
    private static int partition(Comparable[] a, int lo, int hi)
    { /* see previous slide */ }
    
    public static void sort(Comparable[] a)
    {
        // shuffle needed for performance guarantee (stay tuned)
        StdRandom.shuffle(a);   
        sort(a, 0, a.length - 1);
    }
    
    private static void sort(Comparable[] a, int lo, int hi)
    {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }
}
```

### 1.5 Quicksort: implementation details

**Partitioning in-place.** Using an extra array makes partitioning easier (and stable), but is not worth the cost.

**Terminating the loop.** Testing whether the pointers cross is a bit trickier than it might seem.

**Staying in bounds.** The (```j == lo```) test is redundant (why?), but the (```i == hi```) test is not.

**Preserving randomness.** Shuffling is needed for performance guarantee.

**Equal keys.** When duplicates are present, it is (counter-intuitively) better to stop on keys equal to the partitioning item's key.

### 1.6 Quicksort: empirical analysis

##### Running time estimates:
- Home PC executes `$10^8$` compares/second.
- Supercomputer executes `$10^{12}$` compares/second.

**Lesson 1.** Good algorithms are better than supercomputers.

**Lesson 2.** Great algorithms are better than good ones.

### 1.7 Quicksort: best-case analysis

**Best case.** Number of compares is `$\sim N \lg N$`

### 1.8 Quicksort: worst-case analysis

**Worst case.** Number of compares is `$\sim \frac{1}{2}N^2$`

### 1.9 Quicksort: average-case analysis

**Proposition.** The average number of compares `$C_N$` to quicksort an array of `$N$` distinct keys is `$\sim 2N \ln N$` (and the number of exchanges is `$\sim \frac{1}{3}N \ln N$`).

**Pf.** `$C_N$` satisfies the recurrence `$C_0 = C_1 = 0$` and for `$N \ge 2$`:
```math
C_N = (N+1)+(\frac{C_0+C_{N-1}}{N})+(\frac{C_1+C_{N-2}}{N})+...+(\frac{C_{N-1}+C_0}{N})
```
- Multiply both sides by N and collect terms:
```math
NC_N=N(N+1)+2(C_0+C_1+...+C_{N-1})
```
- Subtract this from the same equation for N - 1:
```math
NC_N-(N-1)C_{N-1}=2N+2C_{N-1}
```
- Rearrange terms and divide by N (N + 1):
```math
\frac{C_N}{N+1}=\frac{C_{N-1}}{N}+\frac{2}{N+1}
```
- Repeatedly apply above equation:
```math
\begin{aligned}
\frac{C_N}{N+1} &= \frac{C_{N-1}}{N}+\frac{2}{N+1}\\
&= \frac{C_{N-2}}{N-1} + \frac{2}{N} + \frac{2}{N+1}\\
&= \frac{C_{N-3}}{N-2} + \frac{2}{N-1} + \frac{2}{N} + \frac{2}{N+1}\\
&= \frac{2}{3}+\frac{2}{4}+\frac{2}{5}+...+\frac{2}{N+1}
\end{aligned}
```
- Approximate sum by an integral:
```math
\begin{aligned}
C_N &= 2(N+1)(\frac{1}{3}+\frac{1}{4}+\frac{1}{5}+...+\frac{1}{N+1})\\
&\sim 2(N+1)\int_3^{N+1} \frac{1}{x} dx
\end{aligned}
```
- Finally, the desired result:
```math
C_N \sim 2(N+1)\ln N \approx 1.39N\lg N
```

### 1.10 Quicksort: summary of performance characteristics

**Worst case.** Number of compares is quadratic.
- `$N + (N - 1) + (N - 2) + … + 1 \sim \frac{1}{2}N^2$`.
- More likely that your computer is struck by lightning bolt.

**Average case.** Number of compares is `$\sim 1.39N\lg N$`.
- 39% more compares than mergesort.
- But faster than mergesort in practice because of less data movement.

##### Random shuffle.
- Probabilistic guarantee against worst case.
- Basis for math model that can be validated with experiments.

**Caveat emptor.** Many textbook implementations go quadratic if array
- Is sorted or reverse sorted.
- Has many duplicates (even if randomized!)

### 1.11 Quicksort properties

**Proposition.** Quicksort is an **in-place** sorting algorithm.

**Pf.**
- Partitioning: constant extra space.
- Depth of recursion: logarithmic extra space (with high probability).
> can guarantee logarithmic depth by recurring on smaller subarray before larger subarray

**Proposition.** Quicksort is **not stable**.

**Pf.**

### 1.12 Quicksort: practical improvements

##### Insertion sort small subarrays.
- Even quicksort has too much overhead for tiny subarrays.
- Cutoff to insertion sort for ≈ 10 items.
- Note: could delay insertion sort until one pass at end.

```java
private static void sort(Comparable[] a, int lo, int hi)
{
    if (hi <= lo + CUTOFF - 1)
    {
        Insertion.sort(a, lo, hi);
        return;
    }
    int j = partition(a, lo, hi);
    sort(a, lo, j-1);
    sort(a, j+1, hi);
}
```

##### Median of sample.
- Best choice of pivot item = median.
- Estimate true median by taking median of sample.
- Median-of-3 (random) items.
    > - ~ 12/7 N ln N compares (slightly fewer)
    > - ~ 12/35 N ln N exchanges (slightly more)

```java
private static void sort(Comparable[] a, int lo, int hi)
{
    if (hi <= lo) return;
    
    int m = medianOf3(a, lo, lo + (hi - lo)/2, hi);
    swap(a, lo, m);
    
    int j = partition(a, lo, hi);
    sort(a, lo, j-1);
    sort(a, j+1, hi);
}
```

## 2. selection

### 2.1 Selection

**Goal.** Given an array of `$N$` items, find a `$k^{th}$` smallest item.
> Ex. Min (`$k = 0$`), max (`$k = N - 1$`), median (`$k = N / 2$`).

##### Applications.
- Order statistics.
- Find the "top k."

##### Use theory as a guide.
- Easy `$N log N$` upper bound. How?
- Easy `$N$` upper bound for `$k = 1, 2, 3$`. How?
- Easy `$N$` lower bound. Why?

##### Which is true?
- `$N log N$` lower bound?
    > is selection as hard as sorting?
- `$N$` upper bound?
    > is there a linear-time algorithm for each k?

### 2.2 Quick-select

##### Partition array so that:
- Entry ```a[j]``` is in place.
- No larger entry to the left of ```j```.
- No smaller entry to the right of ```j```.

Repeat in one subarray, depending on ```j```; finished when ```j``` equals ```k```.

```java
public static Comparable select(Comparable[] a, int k)
{
    StdRandom.shuffle(a);
    int lo = 0, hi = a.length - 1;
    while (hi > lo)
    {
        int j = partition(a, lo, hi);
        if (j < k) lo = j + 1;
        else if (j > k) hi = j - 1;
        else return a[k];
    }
    return a[k];
}
```

### 2.3 Quick-select: mathematical analysis

**Proposition.** Quick-select takes **linear** time on average.

**Pf sketch.**
- Intuitively, each partitioning step splits array approximately in half:`$N + N / 2 + N / 4 + … + 1 \sim 2N$`compares.
- Formal analysis similar to quicksort analysis yields:
```math
C_N = 2N + 2k\ln(N/k) + 2(N-k)\ln(N/(N-k))
```
> (2 + 2 ln 2) N to find the median

**Remark.** Quick-select uses `$\sim \frac{1}{2} N^2$` compares in the worst case, but (as with quicksort) the random shuffle provides a probabilistic guarantee.

### 2.4 Theoretical context for selection

**Proposition.** [Blum, Floyd, Pratt, Rivest, Tarjan, 1973] Compare-based selection algorithm whose worst-case running time is linear.

**Remark.** But, constants are too high ⇒ not used in practice.

##### Use theory as a guide.
- Still worthwhile to seek **practical** linear-time (worst-case) algorithm.
- Until one is discovered, use quick-select if you don’t need a full sort.

## 3. duplicate keys

### 3.1 Duplicate keys

##### Often, purpose of sort is to bring items with equal keys together.
- Sort population by age.
- Remove duplicates from mailing list.
- Sort job applicants by college attended.

##### Typical characteristics of such applications.
- Huge array.
- Small number of key values.

**Mergesort with duplicate keys.** Between `$\frac{1}{2}N\lg N$` and `$N\lg N$` compares.

##### Quicksort with duplicate keys.
- Algorithm goes quadratic unless partitioning stops on equal keys!
- 1990s C user found this defect in qsort().
> several textbook and system
implementation also have this defect

### 3.2 Duplicate keys: the problem

**Mistake.** Put all items equal to the partitioning item on one side.

**Consequence.** `$\sim \frac{1}{2}N^2$` compares when all keys equal.

**Recommended.** Stop scans on items equal to the partitioning item.

**Consequence.** `$\sim N\lg N$` compares when all keys equal.

**Desirable.** Put all items equal to the partitioning item in place.

### 3.3 3-way partitioning

**Goal.** Partition array into 3 parts so that:
- Entries between ```lt``` and ```gt``` equal to partition item ```v```.
- No larger entries to left of ```lt```.
- No smaller entries to right of ```gt```.

**Dutch national flag problem.** [Edsger Dijkstra]
- Conventional wisdom until mid 1990s: not worth doing.
- New approach discovered when fixing mistake in C library ```qsort()```.
- Now incorporated into ```qsort()``` and Java system sort.

### 3.4 Dijkstra 3-way partitioning demo

- Let ```v``` be partitioning item ```a[lo]```.
- Scan ```i``` from left to right.
    - (```a[i] < v```): exchange ```a[lt]``` with ```a[i]```; increment both ```lt``` and ```i```
    - (```a[i] > v```): exchange ```a[gt]``` with ```a[i]```; decrement ```gt```
    - (```a[i] == v```): increment ```i```

### 3.5 3-way quicksort: Java implementation

```java
private static void sort(Comparable[] a, int lo, int hi)
{
    if (hi <= lo) return;
    int lt = lo, gt = hi;
    Comparable v = a[lo];
    int i = lo;
    while (i <= gt)
    {
        int cmp = a[i].compareTo(v);
        if (cmp < 0) exch(a, lt++, i++);
        else if (cmp > 0) exch(a, i, gt--);
        else i++;
    }
    sort(a, lo, lt - 1);
    sort(a, gt + 1, hi);
}
```

### 3.6 Duplicate keys: lower bound

**Sorting lower bound.** If there are `$n$` distinct keys and the `$i^{th}$` one occurs `$x_i$` times, any compare-based sorting algorithm must use at least 
```math
\lg (\frac{N!}{x_1!x_2!...x_n!})\sim - \sum_{i=1}^n x_i \lg \frac{x_i}{N}
```
compares in the worst case.
> - `$N\lg N$` when all distinct;
> - linear when only a constant number of distinct keys

**Proposition.** [Sedgewick-Bentley, 1997]
Quicksort with 3-way partitioning is entropy-optimal.

**Pf.** [beyond scope of course]

**Bottom line.** Randomized quicksort with 3-way partitioning reduces running time from linearithmic to linear in broad class of applications.

## 4. system sorts

### 4.1 Sorting applications

##### Sorting algorithms are essential in a broad variety of applications:

obvious applications
- Sort a list of names.
- Organize an MP3 library.
- Display Google PageRank results.
- List RSS feed in reverse chronological order.

problems become easy once items are in sorted order
- Find the median.
- Identify statistical outliers.
- Binary search in a database.
- Find duplicates in a mailing list.

non-obvious applications
- Data compression.
- Computer graphics.
- Computational biology.
- Load balancing on a parallel computer.
. . .

### 4.2 Java system sorts

##### Arrays.sort().
- Has different method for each primitive type.
- Has a method for data types that implement Comparable.
- Has a method that uses a Comparator.
- Uses tuned quicksort for primitive types; tuned mergesort for objects.

```java
import java.util.Arrays;
public class StringSort
{
    public static void main(String[] args)
    {
        String[] a = StdIn.readStrings());
        Arrays.sort(a);
        for (int i = 0; i < N; i++)
        StdOut.println(a[i]);
    }
}
```

**Q.** Why use different algorithms for primitive and reference types?

### 4.3 War story (C qsort function)

**AT&T Bell Labs (1991).** Allan Wilks and Rick Becker discovered that a ```qsort()``` call that should have taken seconds was taking minutes.

##### At the time, almost all ```qsort()``` implementations based on those in:
- Version 7 Unix (1979): quadratic time to sort organ-pipe arrays.
- BSD Unix (1983): quadratic time to sort random arrays of 0s and 1s.

### 4.4 Engineering a system sort

##### Basic algorithm = quicksort.
- Cutoff to insertion sort for small subarrays.
- Partitioning scheme: Bentley-McIlroy 3-way partitioning.
- Partitioning item.
    - small arrays: middle entry
    - medium arrays: median of 3
    - large arrays: Tukey's ninther [next slide]

**Now widely used.** C, C++, Java 6, ….

### 4.5 Tukey's ninther

**Tukey's ninther.** Median of the median of 3 samples, each of 3 entries.
- Approximates the median of 9.
- Uses at most 12 compares.

**Q.** Why use Tukey's ninther?
**A.** Better partitioning than random shuffle and less costly.

### 4.6 Achilles heel in Bentley-McIlroy implementation (Java system sort)

**Q.** Based on all this research, Java’s system sort is solid, right?

**A.** No: a killer input.
- Overflows function call stack in Java and crashes program.
- Would take quadratic time if it didn’t crash first.

### 4.7 System sort: Which algorithm to use?

Many sorting algorithms to choose from:

##### Internal sorts.
- Insertion sort, selection sort, bubblesort, shaker sort.
- Quicksort, mergesort, heapsort, samplesort, shellsort.
- Solitaire sort, red-black sort, splaysort, Yaroslavskiy sort, psort, ...

**External sorts.** Poly-phase mergesort, cascade-merge, oscillating sort.

**String/radix sorts.** Distribution, MSD, LSD, 3-way string quicksort.

##### Parallel sorts.
- Bitonic sort, Batcher even-odd sort.
- Smooth sort, cube sort, column sort.
- GPUsort.

##### Applications have diverse attributes.
- Stable?
- Parallel?
- Deterministic?
- Keys all distinct?
- Multiple key types?
- Linked list or arrays?
- Large or small items?
- Is your array randomly ordered?
- Need guaranteed performance?

Elementary sort may be method of choice for some combination.
Cannot cover all combinations of attributes.

**Q.** Is the system sort good enough?
**A.** Usually.

### 4.8 Sorting summary

![image](https://s1.ax1x.com/2018/10/31/iWQCwD.png)