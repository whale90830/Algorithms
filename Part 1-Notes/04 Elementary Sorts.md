# Elementary Sorts

## 1. rules of the game

### 1.1 Sorting problem

**Ex.** Student records in a university.

**Sort.** Rearrange array of N items into ascending order.

### 1.2 Sample sort client

**Goal.** Sort any type of data.

**Ex 1.** Sort random ++real numbers++ in ascending order.
> seems artificial, but stay tuned for an application

```java
public class Experiment
{
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++)
            a[i] = StdRandom.uniform();
        Insertion.sort(a);
        for (int i = 0; i < N; i++)
            StdOut.println(a[i]);
    }
}
```

**Ex 2.** Sort ++strings++ from file in alphabetical order.

```java
public class StringSorter
{
    public static void main(String[] args)
    {
        String[] a = In.readStrings(args[0]);
        Insertion.sort(a);
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
    }
}
```

**Ex 3.** Sort the ++files++ in a given directory by filename.

```java
import java.io.File;
public class FileSorter
{
    public static void main(String[] args)
    {
        File directory = new File(args[0]);
        File[] files = directory.listFiles();
        Insertion.sort(files);
        for (int i = 0; i < files.length; i++)
            StdOut.println(files[i].getName());
    }
}
```

### 1.3 Callbacks

**Goal.** Sort any type of data.

**Q.** How can ```sort()``` know how to compare data of type ```Double```, ```String```, and ```java.io.File``` without any information about the type of an item's key?

##### Callback = reference to executable code.
- Client passes array of objects to ```sort() ```function.
- The ```sort()``` function calls back object's ```compareTo()``` method as needed.

##### Implementing callbacks.
- Java: interfaces.
- C: function pointers.
- C++: class-type functors.
- C#: delegates.
- Python, Perl, ML, Javascript: first-class functions.

### 1.4 Callbacks: roadmap

##### Comparable interface (built in to Java)
```java
public interface Comparable<Item>
{
    public int compareTo(Item that);
}
```

##### object implementation
```java
public class File implements Comparable<File>
{
    ...
    public int compareTo(File b)
    {
        ...
        return -1;
        ...
        return +1;
        ...
        return 0;
    }
}
```

##### sort implementation
```java
public static void sort(Comparable[] a)
{
    int N = a.length;
    for (int i = 0; i < N; i++)
        for (int j = i; j > 0; j--)
            // key point: no dependence on File data type
            if (a[j].compareTo(a[j-1]) < 0)
                exch(a, j, j-1);
            else break;
}
```

### 1.5 Total order

A **total order** is a binary relation ≤ that satisfies:
- Antisymmetry（反对称性）: if v ≤ w and w ≤ v, then v = w.
- Transitivity（传递性）: if v ≤ w and w ≤ x, then v ≤ x.
- Totality: either v ≤ w or w ≤ v or both.

**Ex.**
- Standard order for natural and real numbers.
- Chronological order for dates or times.
- Alphabetical order for strings.
- ……

**Surprising but true.** The <= operator for double is not a total order. (!)

### 1.6 Comparable API

##### Implement ```compareTo()``` so that ```v.compareTo(w)```
- Is a total order.
- Returns a negative integer, zero, or positive integer if v is less than, equal to, or greater than w, respectively.
- Throws an exception if incompatible types (or either is null).

**Built-in comparable types.** Integer, Double, String, Date, File, ...

**User-defined comparable types.** Implement the Comparable interface.

### 1.7 Implementing the Comparable interface

**Date data type.** Simplified version of ```java.util.Date```.

```java
public class Date implements Comparable<Date>
{
    private final int month, day, year;
    
    public Date(int m, int d, int y)
    {
        month = m;
        day = d;
        year = y;
    }
    
    public int compareTo(Date that)
    {
        if (this.year < that.year ) return -1;
        if (this.year > that.year ) return +1;
        if (this.month < that.month) return -1;
        if (this.month > that.month) return +1;
        if (this.day < that.day ) return -1;
        if (this.day > that.day ) return +1;
        return 0;
    }
}
```

### 1.8 Two useful sorting abstractions

**Helper functions.** Refer to data through compares and exchanges.

**Less.** Is item v less than w ?
```java
private static boolean less(Comparable v, Comparable w)
{ return v.compareTo(w) < 0; }
```

**Exchange.** Swap item in array ```a[]``` at index ```i``` with the one at index ```j```.
```java
private static void exch(Comparable[] a, int i, int j)
{
    Comparable swap = a[i];
    a[i] = a[j];
    a[j] = swap;
}
```

### 1.9 Testing

**Goal.** Test if an array is sorted.
```java
private static boolean isSorted(Comparable[] a)
{
    for (int i = 1; i < a.length; i++)
        if (less(a[i], a[i-1])) return false;
    return true;
}
```

**Q.** If the sorting algorithm passes the test, did it correctly sort the array?

**A.**

## 2. selection sort

### 2.1 Selection sort demo

- In iteration ```i```, find index ```min``` of smallest remaining entry.
- Swap ```a[i]``` and ```a[min]```.

### 2.2 Selection sort

**Algorithm.** ↑ scans from left to right.

##### Invariants.
- Entries the left of ↑ (including ↑) fixed and in ascending order.
- No entry to right of ↑ is smaller than any entry to the left of ↑.

### 2.3 Selection sort inner loop

To maintain algorithm invariants:

- Move the pointer to the right.
```java
i++;
```
- Identify index of minimum entry on right.
```java
int min = i;
for (int j = i+1; j < N; j++)
    if (less(a[j], a[min]))
        min = j;
```
- Exchange into position.
```java
exch(a, i, min);
```

### 2.4 Selection sort: Java implementation

```java
public class Selection
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++)
        {
            int min = i;
            for (int j = i+1; j < N; j++)
                if (less(a[j], a[min]))
                    min = j;
            exch(a, i, min);
        }
    }
    
    private static boolean less(Comparable v, Comparable w)
    { /* as before */ }
    
    private static void exch(Comparable[] a, int i, int j)
    { /* as before */ }
}
```

### 2.5 Selection sort: mathematical analysis

**Proposition.** Selection sort uses `$(N-1)+(N-2)+...+1+0 \sim N^2/2$` compares
and `$N$` exchanges.

**Running time insensitive to input.** Quadratic time, even if input is sorted.

**Data movement is minimal.** Linear number of exchanges.

## 3. insertion sort

### 3.1 Insertion sort demo

- In iteration ```i```, swap ```a[i]``` with each larger entry to its left.

### 3.2 Insertion sort

**Algorithm.** ↑ scans from left to right.

##### Invariants.
- Entries to the left of ↑ (including ↑) are in ascending order.
- Entries to the right of ↑ have not yet been seen.

### 3.3 Insertion sort inner loop

##### To maintain algorithm invariants:
- Move the pointer to the right.
```java
i++;
```
- Moving from right to left, exchange ```a[i]``` with each larger entry to its left.
```java
for (int j = i; j > 0; j--)
    if (less(a[j], a[j-1]))
        exch(a, j, j-1);
    else break;
```

### 3.4 Insertion sort: Java implementation

```java
public class Insertion
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++)
            for (int j = i; j > 0; j--)
                if (less(a[j], a[j-1]))
                    exch(a, j, j-1);
                else break;
    }
    
    private static boolean less(Comparable v, Comparable w)
    { /* as before */ }
    
    private static void exch(Comparable[] a, int i, int j)
    { /* as before */ }
}
```

### 3.5 Insertion sort: mathematical analysis

**Proposition.** To sort a randomly-ordered array with distinct keys, insertion sort uses `$\sim \frac{1}{4}N^2$` compares and `$\sim \frac{1}{4}N^2$` exchanges on average.

**Pf.** Expect each entry to move halfway back.

### 3.6 Insertion sort: best and worst case

**Best case.** If the array is in ascending order, insertion sort makes `$N - 1$` compares and `$0$` exchanges.

**Worst case.** If the array is in descending order (and no duplicates), insertion sort makes `$\sim \frac{1}{2} N^2$` compares and `$\sim \frac{1}{2} N^2$` exchanges.

### 3.7 Insertion sort: partially-sorted arrays

**Def.** An **inversion** is a pair of keys that are out of order.
> - A E E L M O T R X P S
> - T-R T-P T-S R-P X-P X-S (6 inversions)

**Def.** An array is **partially sorted** if the number of inversions is `$\le cN$`
> - Ex 1. A subarray of size 10 appended to a sorted subarray of size N.
> - Ex 2. An array of size N with only 10 entries out of place.

**Proposition.** For partially-sorted arrays, insertion sort runs in linear time.

**Pf.** Number of exchanges equals the number of inversions.
> number of compares = exchanges + (N – 1)

## 4. shellsort

### 4.1 Shellsort overview

**Idea.** Move entries more than one position at a time by h-sorting the array.

**Shellsort.** [Shell 1959] h-sort array for decreasing sequence of values of h.

### 4.2 h-sorting

**How to h-sort an array?** Insertion sort, with stride length h.

##### Why insertion sort?
- Big increments ⇒ small subarray.
- Small increments ⇒ nearly in order. [stay tuned]

### 4.3 Shellsort: intuition

**Proposition.** A g-sorted array remains g-sorted after h-sorting it.

**Challenge.** Prove this fact—it's more subtle than you'd think!

### 4.4 Shellsort: which increment sequence to use?

**Powers of two.** 1, 2, 4, 8, 16, 32, ...
**No.**

**Powers of two minus one.** 1, 3, 7, 15, 31, 63, …
**Maybe.**

**3x + 1.** 1, 4, 13, 40, 121, 364, …
**OK.** Easy to compute.

**Sedgewick.** 1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905, …
Good. Tough to beat in empirical studies.

### 4.5 Shellsort: Java implementation

```java
public class Shell
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        
        int h = 1;
        while (h < N/3) h = 3*h + 1; // 1, 4, 13, 40, 121, 364, ...
        
        while (h >= 1)
        {   // h-sort the array.
            for (int i = h; i < N; i++)
            {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                    exch(a, j, j-h);
            }
            
            h = h/3;
        }
    }
    
    private static boolean less(Comparable v, Comparable w)
    { /* as before */ }
    
    private static void exch(Comparable[] a, int i, int j)
    { /* as before */ }
}
```

### 4.6 Shellsort: analysis

**Proposition.** The worst-case number of compares used by shellsort with the 3x+1 increments is `$O(N^\frac{3}{2})$`

**Property.** Number of compares used by shellsort with the 3x+1 increments is at most by a small multiple of N times the # of increments used.

**Remark.** Accurate model has not yet been discovered (!)

### 4.7 Why are we interested in shellsort?

##### Example of simple idea leading to substantial performance gains.

##### Useful in practice.
- Fast unless array size is huge (used for small subarrays).
- Tiny, fixed footprint for code (used in some embedded systems).
- Hardware sort prototype.

##### Simple algorithm, nontrivial performance, interesting questions.
- Asymptotic growth rate?
- Best sequence of increments?
- Average-case performance?

**Lesson.** Some good algorithms are still waiting discovery.

## 5. shuffling

### 5.1 How to shuffle an array

**Goal.** Rearrange array so that result is a uniformly random permutation.

### 5.2 Shuffle sort

- Generate a random real number for each array entry.
    > useful for shuffling
columns in a spreadsheet
- Sort the array.

**Proposition.** Shuffle sort produces a uniformly random permutation of the input array, provided no duplicate values.

### 5.3 War story (Microsoft)

**Microsoft antitrust probe by EU.** Microsoft agreed to provide a randomized ballot screen for users to select browser in Windows 7.

**Solution?** Implement shuffle sort by making comparator always return a random answer.

```java
public int compareTo(Browser that)
{
    double r = Math.random();
    if (r < 0.5) return -1;
    if (r > 0.5) return +1;
    return 0;
}
```

### 5.4 Knuth shuffle

- In iteration ```i```, pick integer ```r``` between ```0``` and ```i``` uniformly at random.
    > - common bug: between 0 and N – 1
    > - correct variant: between i and N – 1
- Swap ```a[i]``` and ```a[r]```.

**Proposition.** [Fisher-Yates 1938] Knuth shuffling algorithm produces a uniformly random permutation of the input array in linear time.

```java
public class StdRandom
{
    ...
    public static void shuffle(Object[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++)
        {
            int r = StdRandom.uniform(i + 1);
            exch(a, i, r);
        }
    }
}
```

### 5.5 War story (online poker)

**Texas hold'em poker.** Software must shuffle electronic cards.

```java
for i := 1 to 52 do begin
    r := random(51) + 1;
    swap := card[r];
    card[r] := card[i];
    card[i] := swap;
end;
```

- **Bug 1.** Random number r never 52 ⇒ 52nd card can't end up in 52nd place.
- **Bug 2.** Shuffle not uniform (should be between 1 and i).
- **Bug 3.** ```random()``` uses 32-bit seed ⇒ `$2^{32}$` possible shuffles.
- **Bug 4.** Seed = milliseconds since midnight ⇒ 86.4 million shuffles.

##### Best practices for shuffling (if your business depends on it).
- Use a hardware random-number generator that has passed both the FIPS 140-2 and the NIST statistical test suites.
- Continuously monitor statistic properties: hardware random-number generators are fragile and fail silently.
- Use an unbiased shuffling algorithm.

**Bottom line.** Shuffling a deck of cards is hard!

## 6. convex hull

### 6.1 Convex hull

The **convex hull** of a set of N points is the smallest perimeter fence enclosing the points.

##### Equivalent definitions.
- Smallest convex set containing all the points.
- Smallest area convex polygon enclosing the points.
- Convex polygon enclosing the points, whose vertices are points in set.

**Convex hull output.** Sequence of vertices in counterclockwise order.

![image](https://s1.ax1x.com/2018/10/23/iDqlW9.png)

### 6.2 Convex hull: mechanical algorithm

**Mechanical algorithm.** Hammer nails perpendicular to plane; stretch elastic rubber band around points.

### 6.3 Convex hull application: motion planning

**Robot motion planning.** Find shortest path in the plane from s to t that avoids a polygonal obstacle.

**Fact.** Shortest path is either straight line from s to t or it is one of two polygonal chains of convex hull.

![image](https://s1.ax1x.com/2018/10/23/iDq1zR.png)

### 6.4 Convex hull application: farthest pair

**Farthest pair problem.** Given N points in the plane, find a pair of points with the largest Euclidean distance between them.

**Fact.** Farthest pair of points are extreme points on convex hull.

![image](https://s1.ax1x.com/2018/10/23/iDq8Q1.png)

### 6.5 Convex hull: geometric properties

**Fact.** Can traverse the convex hull by making only counterclockwise turns.

**Fact.** The vertices of convex hull appear in increasing order of polar angle with respect to point p with lowest y-coordinate.

![image](https://s1.ax1x.com/2018/10/23/iDqGsx.png)

### 6.6 Graham scan

- Choose point p with smallest y-coordinate.
- Sort points by polar angle with p.
- Consider points in order; discard unless it create a ccw turn.

### 6.7 Graham scan: implementation challenges

**Q.** How to find point p with smallest y-coordinate?
**A.** Define a total order, comparing by y-coordinate. [next lecture]

**Q.** How to sort points by polar angle with respect to p ?
**A.** Define a total order for each point p. [next lecture]

**Q.** How to determine whether p1 → p2 → p3 is a counterclockwise turn?
**A.** Computational geometry. [next two slides]

**Q.** How to sort efficiently?
**A.** Mergesort sorts in N log N time. [next lecture]

**Q.** How to handle degeneracies (three or more points on a line)?
**A.** Requires some care, but not hard. [see booksite]

### 6.8 Implementing ccw

**CCW.** Given three points a, b, and c, is a → b → c a counterclockwise turn?

**Lesson.** Geometric primitives are tricky to implement.
- Dealing with degenerate cases.
- Coping with floating-point precision.

### 6.9 Immutable point data type

```java
public class Point2D
{
    private final double x;
    private final double y;
    
    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    ...
    
    public static int ccw(Point2D a, Point2D b, Point2D c)
    {
        double area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if (area2 < 0) return -1; // clockwise
        else if (area2 > 0) return +1; // counter-clockwise
        else return 0; // collinear
    }
}
```