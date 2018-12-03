# HASH TABLES

### 0.1 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/29/FeKFVf.png)

**Q**. Can we do better?
**A**. Yes, but with different access to the data.

### 0.2 Hashing: basic plan

Save items in a **key-indexed table** (index is a function of the key).

**Hash function.** Method for computing array index from key.

##### Issues.
- Computing the hash function.
- Equality test: Method for checking whether two keys are equal.
- Collision resolution: Algorithm and data structure to handle two keys that hash to the same array index.

##### Classic space-time tradeoff.
- No space limitation: trivial hash function with key as index.
- No time limitation: trivial collision resolution with sequential search.
- Space and time limitations: hashing (the real world).

## 1. hash functions

### 1.1 Computing the hash function

**Idealistic goal**. Scramble the keys uniformly to produce a table index.
- Efficiently computable.
- Each table index equally likely for each key.
    > thoroughly researched problem, still problematic in practical applications

##### Ex 1. Phone numbers.
- Bad: first three digits.
- Better: last three digits.

##### Ex 2. Social Security numbers.
- Bad: first three digits.
    > 573 = California, 574 = Alaska (assigned in chronological order within geographic region)
- Better: last three digits.

**Practical challenge**. Need different approach for each key type.

### 1.2 Java’s hash code conventions
All Java classes inherit a method `hashCode()`, which returns a 32-bit `int`.

**Requirement**. If `x.equals(y)`, then `(x.hashCode() == y.hashCode())`.

**Highly desirable**. If `!x.equals(y)`, then `(x.hashCode() != y.hashCode())`.

**Default implementation**. Memory address of x.

**Legal (but poor) implementation**. Always return 17.

**Customized implementations**. `Integer`, `Double`, `String`, `File`, `URL`, `Date`, …

**User-defined types**. Users are on their own.

### 1.3 Implementing hash code: integers, booleans, and doubles

##### Java library implementations

```java
public final class Integer
{
    private final int value;
    ...
    
    public int hashCode()
    { return value; }
}
```

```java
public final class Boolean
{
    private final boolean value;
    ...
    
    public int hashCode()
    {
        if (value) return 1231;
        else return 1237;
    }
}
```

```java
public final class Double
{
    private final double value;
    ...
    
    public int hashCode()
    {
        long bits = doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
        // convert to IEEE 64-bit representation;
        // xor most significant 32-bits with least significant 32-bits
    }
}
```


### 1.4 Implementing hash code: strings

##### Java library implementation
```java
public final class String
{
    private final char[] s;
    ...
    
    public int hashCode()
    {
        int hash = 0;
        for (int i = 0; i < length(); i++)
            hash = s[i] + (31 * hash);  // i^th character of s
        return hash;
    }
}
```

- Horner's method to hash string of length L: L multiplies/adds.
- Equivalent to `$h=s[0] \cdot 31^{L-1} + ... + s[L-1] \cdot 31^2 + s[L-2] \cdot 31^1 + s[L-1] \cdot 31^0$`

**Ex**.
```java
    String s = "call";
    int code = s.hashCode();
    //3045982
    // = 99·31^3 + 97·31^2 + 108·31^1 + 108·31^0
    // = 108 + 31· (108 + 31 · (97 + 31 · (99)))
    // (Horner's method)
```

##### Performance optimization.
- Cache the hash value in an instance variable.
- Return cached value.

```java
public final class String
{
    private int hash = 0;   // cache of hash code
    private final char[] s;
    ...
    
    public int hashCode()
    {
        int h = hash;
        if (h != 0) return h;   // return cached value
        for (int i = 0; i < length(); i++)
            h = s[i] + (31 * h);
        hash = h;   // store cache of hash code
        return h;
    }
}
```

### 1.5 Implementing hash code: user-defined types

```java
public final class Transaction implements Comparable<Transaction>
{
    private final String who;
    private final Date when;
    private final double amount;
    
    public Transaction(String who, Date when, double amount)
    { /* as before */ }
    
    ...
    
    public boolean equals(Object y)
    { /* as before */ }
    
    public int hashCode()
    {
        int hash = 17;  // nonzero constant
        hash = 31*hash + who.hashCode();    // for reference types, use hashCode()
        hash = 31*hash + when.hashCode();   // for primitive types, use hashCode() of wrapper type
        hash = 31*hash + ((Double) amount).hashCode(); // 31 : typically a small prime
        return hash;
    }
}
```

### 1.6 Hash code design

##### "Standard" recipe for user-defined types.
- Combine each significant field using the `$31x + y$` rule.
- If field is a primitive type, use wrapper type `hashCode()`.
- If field is null, return 0.
- If field is a reference type, use `hashCode()`.
    > applies rule recursively
- If field is an array, apply to each entry.
    > or use `Arrays.deepHashCode()`

**In practice**. Recipe works reasonably well; used in Java libraries.

**In theory**. Keys are bitstring; "universal" hash functions exist.

**Basic rule**. Need to use the whole key to compute hash code; consult an expert for state-of-the-art hash codes.

### 1.7 Modular hashing

**Hash code**. An `int` between `$-2^{31}$` and `$2^{31} - 1$`.

**Hash function**. An `int` between 0 and M - 1 (for use as array index).
> M: typically a prime or power of 2

##### bug
```java
private int hash(Key key)
{ return key.hashCode() % M; }
```

##### 1-in-a-billion bug
> `hashCode()` of "polygenelubricants" is `$-2^{31}$`

```java
private int hash(Key key)
{ return Math.abs(key.hashCode()) % M; }
```

##### correct
```java
private int hash(Key key)
{ return (key.hashCode() & 0x7fffffff) % M; }
```

### 1.8 Uniform hashing assumption

**Uniform hashing assumption**. Each key is equally likely to hash to an
integer between 0 and M - 1.

**Bins and balls**. Throw balls uniformly at random into M bins.

**Birthday problem**. Expect two balls in the same bin after `$\sim \sqrt{\pi M/2}$` tosses.

**Coupon collector**. Expect every bin has ≥ 1 ball after `$\sim M \ln M$` tosses.

**Load balancing**. After M tosses, expect most loaded bin has `$\theta (\log M / \log \log M)$` balls.

## 2. separate chaining

### 2.1 Collisions

**Collision**. Two distinct keys hashing to same index.
- Birthday problem ⇒ can't avoid collisions unless you have a ridiculous (quadratic) amount of memory.
- Coupon collector + load balancing ⇒ collisions are evenly distributed.

![image](https://s1.ax1x.com/2018/11/29/FeGbNt.png)

**Challenge**. Deal with collisions efficiently.

### 2.2 Separate chaining symbol table

**Use an array of M < N linked lists**. [H. P. Luhn, IBM 1953]
- Hash: map key to integer i between 0 and M - 1.
- Insert: put at front of `$i^{th}$` chain (if not already there).
- Search: need to search only `$i^{th}$` chain.

![image](https://s1.ax1x.com/2018/11/29/FeGq4P.png)

### 2.3 Separate chaining ST: Java implementation

```java
public class SeparateChainingHashST<Key, Value>
{
    private int M = 97; // number of chains
    private Node[] st = new Node[M]; // array of chains
    
    private static class Node
    {
        private Object key; // no generic array creation
        private Object val; // (declare key and value of type Object)
        private Node next;
        ...
    }
    
    private int hash(Key key)
    { return (key.hashCode() & 0x7fffffff) % M; }
    
    public Value get(Key key) {
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next)
            if (key.equals(x.key)) return (Value) x.val;
        return null;
    }
    
    public void put(Key key, Value val) {
        int i = hash(key);
        for (Node x = st[i]; x != null; x = x.next)
            if (key.equals(x.key)) { x.val = val; return; }
        st[i] = new Node(key, val, st[i]);
    }
}
```

### 2.4 Analysis of separate chaining

**Proposition**. Under uniform hashing assumption, prob. that the number of
keys in a list is within a constant factor of N / M is extremely close to 1.

**Pf sketch**. Distribution of list size obeys a binomial distribution.

**Consequence**. Number of probes (`equals()` and `hashCode()`) for search/insert is proportional to N / M.
> M times faster than sequential search

- M too large ⇒ too many empty chains.
- M too small ⇒ chains too long.
- Typical choice: M ~ N / 5 ⇒ constant-time ops.

### 2.5 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/29/FeJQ4x.png)

## 3. linear probing

### 3.1 Collision resolution: open addressing

**Open addressing.** [Amdahl-Boehme-Rocherster-Samuel, IBM 1953]

When a new key collides, find next empty slot, and put it there.

![image](https://s1.ax1x.com/2018/11/29/Fe624O.png)

### 3.2 Linear probing hash table demo

**Hash**. Map key to integer i between 0 and M-1.

**Insert**. Put at table index i if free; if not try i+1, i+2, etc.

**Search**. Search table index i; if occupied but no match, try i+1, i+2, etc.

**Note**. Array size M must be greater than number of key-value pairs N.

### 3.3 Linear probing ST implementation

```java
public class LinearProbingHashST<Key, Value>
{
    private int M = 30001;
    private Value[] vals = (Value[]) new Object[M];  // array doubling and halving code omitted
    private Key[] keys = (Key[]) new Object[M];
    
    private int hash(Key key) { /* as before */ }
    
    public void put(Key key, Value val)
    {
        int i;
        for (i = hash(key); keys[i] != null; i = (i+1) % M)
            if (keys[i].equals(key))
                break;
        keys[i] = key;
        vals[i] = val;
    }
    
    public Value get(Key key)
    {
        for (int i = hash(key); keys[i] != null; i = (i+1) % M)
            if (key.equals(keys[i]))
                return vals[i];
        return null;
    }
}
```

### 3.4 Clustering

**Cluster**. A contiguous block of items.

**Observation**. New keys likely to hash into middle of big clusters.

### 3.5 Knuth's parking problem

**Model**. Cars arrive at one-way street with M parking spaces.
Each desires a random space i : if space i is taken, try i + 1, i + 2, etc.

**Q**. What is mean displacement of a car?

![image](https://s1.ax1x.com/2018/12/02/Fuqcbq.png)

**Half-full**. With M / 2 cars, mean displacement is ~ 3 / 2.

**Full**. With M cars, mean displacement is `$\sim \sqrt{\pi M/8}$`.

### 3.6 Analysis of linear probing

**Proposition**. Under uniform hashing assumption, the average # of probes in a linear probing hash table of size M that contains N = α M keys is:
- search hit: `$\sim \frac{1}{2}(1+\frac{1}{1-\alpha})$`
- search miss/insert: `$\sim \frac{1}{2}(1+\frac{1}{(1-\alpha)^2})$`

**Pf**. omitted

**Parameters**.
- M too large ⇒ too many empty array entries.
- M too small ⇒ search time blows up.
- Typical choice: α = N / M ~ ½.
    > - \# probes for search hit is about 3/2
    > - \# probes for search miss is about 5/2

### 3.7 ST implementations: summary

![image](https://s1.ax1x.com/2018/12/02/Fuq524.png)

> ### 3.8 Delete
> 
> ##### The easiest way to implement delete
> 
> - find and remove the key–value pair and then to reinsert all of the key–value pairs in the same cluster that appear after the deleted key–value pair.
> - If the hash table doesn't get too full, the expected number of key–value pairs to reinsert will be a small constant.
> 
> ##### An alternative
> 
> - flag the deleted linear-probing table entry so that it is skipped over during a search but is used for an insertion.
> - If there are too many flagged entries, create a new hash table and rehash all key–value pairs.
## 4. context

### 4.1 War story: String hashing in Java

##### String `hashCode()` in Java 1.1.
- For long strings: only examine 8-9 evenly spaced characters.
- Benefit: saves time in performing arithmetic.
    ```java
    public int hashCode()
    {
        int hash = 0;
        int skip = Math.max(1, length() / 8);
        for (int i = 0; i < length(); i += skip)
            hash = s[i] + (37 * hash);
        return hash;
    }
    ```
- Downside: great potential for bad collision patterns.

    ![image](https://s1.ax1x.com/2018/12/02/Fuj9sJ.png)

### 4.2 War story: algorithmic complexity attacks

**Q**. Is the uniform hashing assumption important in practice?

**A**. Obvious situations: aircraft control, nuclear reactor, pacemaker.

**A**. Surprising situations: denial-of-service attacks.

![image](https://s1.ax1x.com/2018/12/02/FujVJK.png)

**Real-world exploits.** [Crosby-Wallach 2003]
- Bro server: send carefully chosen packets to DOS the server, using less bandwidth than a dial-up modem.
- Perl 5.8.0: insert carefully chosen strings into associative array.
- Linux 2.4.20 kernel: save files with carefully chosen names.

### 4.3 Algorithmic complexity attack on Java

**Goal**. Find family of strings with the same hash code.
**Solution**. The base 31 hash code is part of Java's string API.

![image](https://s1.ax1x.com/2018/12/02/FujnQe.png)

### 4.4 Diversion: one-way hash functions

**One-way hash function**. "Hard" to find a key that will hash to a desired value (or two keys that hash to same value).

**Ex**. MD4, MD5, SHA-0, SHA-1, SHA-2, WHIRLPOOL, RIPEMD-160, ….

```java
String password = args[0];
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
byte[] bytes = sha1.digest(password);

/* prints bytes as hex string */
```

**Applications**. Digital fingerprint, message digest, storing passwords.

**Caveat**. Too expensive for use in ST implementations.

### 4.5 Separate chaining vs. linear probing
##### Separate chaining.
- Easier to implement delete.
- Performance degrades gracefully.
- Clustering less sensitive to poorly-designed hash function.

##### Linear probing.
- Less wasted space.
- Better cache performance.

**Q**. How to delete?

**Q**. How to resize?

### 4.6 Hashing: variations on the theme

Many improved versions have been studied.

**Two-probe hashing**. (separate-chaining variant)
- Hash to two positions, insert key in shorter of the two chains.
- Reduces expected length of the longest chain to `$\log \log N$`.

**Double hashing**. (linear-probing variant)
- Use linear probing, but skip a variable amount, not just 1 each time.
- Effectively eliminates clustering.
- Can allow table to become nearly full.
- More difficult to implement delete.

**Cuckoo hashing**. (linear-probing variant)
- Hash key to two positions; insert key into either position; if occupied, reinsert displaced key into its alternative position (and recur).
- Constant worst case time for search.

### 4.7 Hash tables vs. balanced search trees

##### Hash tables.
- Simpler to code.
- No effective alternative for unordered keys.
- Faster for simple keys (a few arithmetic ops versus `$\log N$` compares).
- Better system support in Java for strings (e.g., cached hash code).

##### Balanced search trees.
- Stronger performance guarantee.
- Support for ordered ST operations.
- Easier to implement `compareTo()` correctly than `equals()` and `hashCode()`.

##### Java system includes both.
- Red-black BSTs: `java.util.TreeMap`, `java.util.TreeSet`.
- Hash tables: `java.util.HashMap`, `java.util.IdentityHashMap`.