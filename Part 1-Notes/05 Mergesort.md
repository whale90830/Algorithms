# Mergesort

### Two classic sorting algorithms

##### Critical components in the world’s computational infrastructure.
- Full scientific understanding of their properties has enabled us to develop them into practical system sorts.
- Quicksort honored as one of top 10 algorithms of 20th century in science and engineering.

##### Mergesort. [this lecture]
- Java sort for objects.
- Perl, C++ stable sort, Python stable sort, Firefox JavaScript, ...

##### Quicksort. [next lecture]
- Java sort for primitive types.
- C qsort, Unix, Visual C++, Python, Matlab, Chrome JavaScript, ...

## 1. mergesort

### 1.1 Mergesort

##### Basic plan.
- Divide array into two halves.
- **Recursively** sort each half.
- Merge two halves.

### 1.2 Abstract in-place merge demo

**Goal.** Given two sorted subarrays ```a[lo]``` to ```a[mid]``` and ```a[mid+1]``` to ```a[hi]```, replace with sorted subarray ```a[lo]``` to ```a[hi]```.

### 1.3 Merging: Java implementation

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
{
    assert isSorted(a, lo, mid); // precondition: a[lo..mid] sorted
    assert isSorted(a, mid+1, hi); // precondition: a[mid+1..hi] sorted
    
    for (int k = lo; k <= hi; k++)
        aux[k] = a[k];
    
    int i = lo, j = mid+1;
    for (int k = lo; k <= hi; k++)
    {
        if (i > mid) a[k] = aux[j++];
        else if (j > hi) a[k] = aux[i++];
        else if (less(aux[j], aux[i])) a[k] = aux[j++];
        else a[k] = aux[i++];
    }
    
    assert isSorted(a, lo, hi); // postcondition: a[lo..hi] sorted
}
```

### 1.4 Assertions

**Assertion.** Statement to test assumptions about your program.
- Helps detect logic bugs.
- Documents code.

**Java assert statement.** Throws exception unless boolean condition is true.
```java
assert isSorted(a, lo, hi);
```

**Can enable or disable at runtime.** ⇒ No cost in production code.
```java
java -ea MyProgram // enable assertions
java -da MyProgram // disable assertions (default)
```

**Best practices.** Use assertions to check internal invariants; assume assertions will be disabled in production code.
> do not use for external argument checking

### 1.5 Mergesort: Java implementation

```java
public class Merge
{
    private static void merge(...)
    { /* as before */ }
    
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid+1, hi);
        merge(a, aux, lo, mid, hi);
    }
    
    public static void sort(Comparable[] a)
    {
        aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }
}
```

### 1.6 Mergesort: empirical analysis

##### Running time estimates:
- Laptop executes `$10^8$` compares/second.
- Supercomputer executes `$10^{12}$` compares/second.

**Bottom line.** Good algorithms are better than supercomputers.

### 1.7 Mergesort: number of compares and array accesses

**Proposition.** Mergesort uses at most `$N\lg N$` compares and `$6N\lg N$` array accesses to sort any array of size `$N$`.

**Pf sketch.** The number of compares `$C(N)$` and array accesses `$A(N)$` to mergesort an array of size `$N$` satisfy the recurrences:

`$C(N)\le C(\lceil N/2 \rceil) + C(\lfloor N/2 \rfloor) + N$` for `$N>1$`, with `$C(1)=0$`

`$A(N)\le A(\lceil N/2 \rceil) + A(\lfloor N/2 \rfloor) + 6N$` for `$N>1$`, with `$A(1)=0$`

We solve the recurrence when `$N$` is a power of 2.
> result holds for all N

`$D (N) = 2 D (N / 2) + N$`, for `$N > 1$`, with `$D (1) = 0$`

### 1.8 Divide-and-conquer recurrence: proof by picture

**Proposition.** If `$D (N)$`satisfies `$D (N) = 2 D (N / 2) + N$`, for `$N > 1$`, with `$D (1) = 0$`, then `$D (N) = N \lg N$`.

**Pf 1.** [assuming N is a power of 2]

![image](https://s1.ax1x.com/2018/10/25/iyiE9I.png)

**Pf 2.** [assuming N is a power of 2]

![image](https://s1.ax1x.com/2018/10/25/iyik4A.png)

**Pf 3.** [assuming N is a power of 2]
- Base case: `$N = 1$`.
- Inductive hypothesis: `$D (N) = N \lg N$`.
- Goal: show that `$D (2N) = (2N) \lg (2N)$`.

![image](https://s1.ax1x.com/2018/10/25/iyiFNd.png)

### 1.9 Mergesort analysis: memory

**Proposition.** Mergesort uses extra space proportional to N.

**Pf.** The array ```aux[]``` needs to be of size ```N``` for the last merge.

**Def.** A sorting algorithm is in-place if it uses ≤ c log N extra memory.
> Ex. Insertion sort, selection sort, shellsort.

**Challenge for the bored.** In-place merge. [Kronrod, 1969]

### 1.10 Mergesort: practical improvements

##### Use insertion sort for small subarrays.
- Mergesort has too much overhead for tiny subarrays.
- Cutoff to insertion sort for ≈ 7 items.

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo + CUTOFF - 1)
    {
        Insertion.sort(a, lo, hi);
        return;
    }
    int mid = lo + (hi - lo) / 2;
    sort (a, aux, lo, mid);
    sort (a, aux, mid+1, hi);
    merge(a, aux, lo, mid, hi);
}
```

##### Stop if already sorted.
- Is biggest item in first half ≤ smallest item in second half?
- Helps for partially-ordered arrays.

```java
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort (a, aux, lo, mid);
    sort (a, aux, mid+1, hi);
    if (!less(a[mid+1], a[mid])) return;
    merge(a, aux, lo, mid, hi);
}
```

##### Eliminate the copy to the auxiliary array. 
Save time (but not space) by switching the role of the input and auxiliary array in each recursive call.

```java
private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
{
    int i = lo, j = mid+1;
    for (int k = lo; k <= hi; k++)
    {
        if (i > mid) aux[k] = a[j++];
        else if (j > hi) aux[k] = a[i++];
        else if (less(a[j], a[i])) aux[k] = a[j++];
        else aux[k] = a[i++];
    }
}
private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
{
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort (aux, a, lo, mid);
    sort (aux, a, mid+1, hi);
    merge(a, aux, lo, mid, hi);
}
```

## 2. bottom-up mergesort

### 2.1 Bottom-up mergesort

##### Basic plan.
- Pass through array, merging subarrays of size 1.
- Repeat for subarrays of size 2, 4, 8, 16, ....

### 2.2 Bottom-up mergesort: Java implementation

```java
public class MergeBU
{
    private static void merge(...)
    { /* as before */ }
    
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz+sz)
            for (int lo = 0; lo < N-sz; lo += sz+sz)
                merge(a, aux, lo, lo+sz-1, Math.min(lo+sz+sz-1, N-1));
    }
}
```

**Bottom line.** Simple and non-recursive version of mergesort.
> but about 10% slower than recursive, top-down mergesort on typical systems

## 3. sorting complexity

### 3.1 Complexity of sorting

**Computational complexity.** Framework to study efficiency of algorithms for solving a particular problem `$X$`.

- **Model of computation.** Allowable operations.
- **Cost model.** Operation count(s).
- **Upper bound.** Cost guarantee provided by some algorithm for `$X$`.
- **Lower bound.** Proven limit on cost guarantee of all algorithms for `$X$`.
- **Optimal algorithm.** Algorithm with best possible cost guarantee for `$X$`.
    > lower bound ~ upper bound

##### Example: sorting.
- Model of computation: decision tree.
- Cost model: # compares.
- Upper bound: `$\sim N \lg N$` from mergesort.
- Lower bound: ?
- Optimal algorithm: ?

### 3.2 Decision tree (for 3 distinct items a, b, and c)

![image](https://s1.ax1x.com/2018/10/25/iyAYvt.png)

### 3.3 Compare-based lower bound for sorting

**Proposition.** Any compare-based sorting algorithm must use at least `$\lg(N!) \sim N \lg N$` compares in the worst-case.

**Pf.**
- Assume array consists of `$N$` distinct values `$a_1$` through `$a_N$`.
- Worst case dictated by **height** `$h$` of decision tree.
- Binary tree of height `$h$` has at most `$2^h$` leaves.
- `$N !$` different orderings ⇒ at least `$N!$` leaves.

`$2^h \ge$` #leaves `$\ge N!$`

`$\Rightarrow h \ge \lg(N!) \sim N \lg N$`
> Stirling's formula

### 3.4 Complexity of sorting

##### Example: sorting.
- Model of computation: decision tree.
- Cost model: # compares.
- Upper bound: `$\sim N \lg N$` from mergesort.
- Lower bound: `$\sim N \lg N$`
- **Optimal algorithm = mergesort.**

**First goal of algorithm design:** optimal algorithms.

### 3.5 Complexity results in context

**Compares?** Mergesort **is** optimal with respect to number compares.

**Space?** Mergesort **is not** optimal with respect to space usage.

**Lessons.** Use theory as a guide.
- **Ex.** Design sorting algorithm that guarantees `$\frac{1}{2} N \lg N$` compares?
- **Ex.** Design sorting algorithm that is both time- and space-optimal?

Lower bound may not hold if the algorithm has information about:
- The initial order of the input.
- The distribution of key values.
- The representation of the keys.


- **Partially-ordered arrays.** Depending on the initial order of the input, we may not need `$N \lg N$` compares.
    > insertion sort requires only N-1 compares if input array is sorted
- **Duplicate keys.** Depending on the input distribution of duplicates, we may not need `$N \lg N$` compares.
    > stay tuned for 3-way quicksort
- **Digital properties of keys.** We can use digit/character compares instead of key compares for numbers and strings.
    > stay tuned for radix sorts

## 4. comparators

### 4.1 Comparable interface: review

**Comparable interface:** sort using a type's **natural order**.

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
    …
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

### 4.2 Comparator interface

**Comparator interface:** sort using an **alternate order**.

public interface Comparator<Key> | /
---|---
int compare(Key v, Key w) | *compare keys v and w*

**Required property.** Must be a **total order**.

> Ex. Sort strings by:
> - Natural order. Now is the time
> - Case insensitive. is Now the time
> - Spanish. café cafetero cuarto churro nube ñoño
> - British phone book. McKinley Mackintosh
> - . . .

### 4.3 Comparator interface: system sort

##### To use with Java system sort:
- Create Comparator object.
- Pass as second argument to Arrays.sort().

```java
String[] a;
...
Arrays.sort(a); // uses natural order
...
// uses alternate order defined by Comparator<String> object
Arrays.sort(a, String.CASE_INSENSITIVE_ORDER);
...
Arrays.sort(a, Collator.getInstance(new Locale("es")));
...
Arrays.sort(a, new BritishPhoneBookOrder());
...
```

**Bottom line.** Decouples the definition of the data type from the definition of what it means to compare two objects of that type.

### 4.4 Comparator interface: using with our sorting libraries

##### To support comparators in our sort implementations:
- Use ```Object``` instead of ```Comparable```.
- Pass ```Comparator``` to ```sort()``` and ```less()``` and use it in ```less()```.

```java
public static void sort(Object[] a, Comparator comparator)
{
    int N = a.length;
    for (int i = 0; i < N; i++)
        for (int j = i; j > 0 && less(comparator, a[j], a[j-1]); j--)
            exch(a, j, j-1);
}

private static boolean less(Comparator c, Object v, Object w)
{ return c.compare(v, w) < 0; }

private static void exch(Object[] a, int i, int j)
{ Object swap = a[i]; a[i] = a[j]; a[j] = swap; }
```

### 4.5 Comparator interface: implementing

##### To implement a comparator:
- Define a (nested) class that implements the ```Comparator``` interface.
- Implement the ```compare()``` method.

```java
public class Student
{
    public static final Comparator<Student> BY_NAME = new ByName();
    public static final Comparator<Student> BY_SECTION = new BySection();
    private final String name;
    private final int section;
    ...
    
    // one Comparator for the class
    private static class ByName implements Comparator<Student>
    {
        public int compare(Student v, Student w)
        { return v.name.compareTo(w.name); }
    }
    
    private static class BySection implements Comparator<Student>
    {
        public int compare(Student v, Student w)
        {
            // this technique works here since no danger of overflow
            return v.section - w.section;
        }
    }
}
```

### 4.6 Polar order

**Polar order**. Given a point `$p$`, order points by polar angle they make with `$p$`.

![image](https://s1.ax1x.com/2018/10/26/i6kmaF.png)

**Application.** Graham scan algorithm for convex hull. [see previous lecture]

**High-school trig solution.** Compute polar angle `$\theta$` w.r.t. `$p$` using ```atan2()```.

**Drawback.** Evaluating a trigonometric function is expensive.

##### A ccw-based solution.
- If `$q_1$` is above `$p$` and `$q_2$` is below `$p$`, then `$q_1$` makes smaller polar angle.
- If `$q_1$` is below `$p$` and `$q_2$` is above `$p$`, then `$q_1$` makes larger polar angle.
- Otherwise, `$ccw(p, q_1, q_2)$` identifies which of `$q_1$` or `$q_2$` makes larger angle.

### 4.7 Comparator interface: polar order

```java
public class Point2D
{
    public final Comparator<Point2D> POLAR_ORDER = new PolarOrder();
    private final double x, y;
    ...
    
    private static int ccw(Point2D a, Point2D b, Point2D c)
    { /* as in previous lecture */ }
    
    // one Comparator for each point (not static)
    private class PolarOrder implements Comparator<Point2D>
    {
        public int compare(Point2D q1, Point2D q2)
        {
            double dy1 = q1.y - y;
            double dy2 = q2.y - y;
            
            if (dy1 == 0 && dy2 == 0) { ... }   // p, q1, q2 horizontal
            else if (dy1 >= 0 && dy2 < 0) return -1;  //q1 above p; q2 below p
            else if (dy2 >= 0 && dy1 < 0) return +1;  // q1 below p; q2 above p
            else return -ccw(Point2D.this, q1, q2);    // both above or below p
            // to access invoking point from within inner class
        }
    }
}
```

## 5. stability

### 5.1 Stability

**A typical application.** First, sort by name; then sort by section.

A **stable** sort preserves the relative order of items with equal keys.

**Q.** Which sorts are stable?
**A.** Insertion sort and mergesort (but not selection sort or shellsort).

**Note.** Need to carefully check code ("less than" vs. "less than or equal to").

### 5.2 Stability: insertion sort

**Proposition.** Insertion sort is **stable**.

```java
public class Insertion
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for (int i = 0; i < N; i++)
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }
}
```

**Pf.** Equal items never move past each other.

### 5.3 Stability: selection sort

**Proposition.** Selection sort is **not** stable.

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
}
```

**Pf by counterexample.** Long-distance exchange might move an item past some equal item.

### 5.4 Stability: shellsort

**Proposition.** Shellsort sort is **not** stable.

```java
public class Shell
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        int h = 1;
        while (h < N/3) h = 3*h + 1;
        while (h >= 1)
        {
            for (int i = h; i < N; i++)
            {
                for (int j = i; j > h && less(a[j], a[j-h]); j -= h)
                    exch(a, j, j-h);
            }
            h = h/3;
        }
    }
}
```

**Pf by counterexample.** Long-distance exchanges.

### 5.5 Stability: mergesort

**Proposition.** Mergesort is **stable**.

```java
public class Merge
{
    private static Comparable[] aux;
    private static void merge(Comparable[] a, int lo, int mid, int hi)
    { /* as before */ }
    
    private static void sort(Comparable[] a, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);
        sort(a, mid+1, hi);
        merge(a, lo, mid, hi);
    }
    
    public static void sort(Comparable[] a)
    { /* as before */ }
}
```

**Pf.** Suffices to verify that merge operation is stable.

**Proposition.** Merge operation is stable.

```java
private static void merge(...)
{
    for (int k = lo; k <= hi; k++)
        aux[k] = a[k];
    
    int i = lo, j = mid+1;
    for (int k = lo; k <= hi; k++)
    {
        if (i > mid) a[k] = aux[j++];
        else if (j > hi) a[k] = aux[i++];
        else if (less(aux[j], aux[i])) a[k] = aux[j++];
        else a[k] = aux[i++];
    }
}
```

**Pf.** Takes from left subarray if equal keys.