# Symbol Tables

## 1. API

### 1.1 Symbol tables

##### Key-value pair abstraction.
- Insert a value with specified key.
- Given a key, search for the corresponding value.
> Ex. DNS lookup.
> - Insert domain name with specified IP address.
> - Given domain name, find corresponding IP address.

### 1.2 Symbol table applications

![image](https://s1.ax1x.com/2018/11/02/i4SeH0.png)

### 1.3 Basic symbol table API

**Associative array abstraction**. Associate one value with each key.

![image](https://s1.ax1x.com/2018/11/02/i4SuNT.png)

### 1.4 Conventions

- Values are not null.
- Method ```get()``` returns ```null``` if key not present.
- Method ```put()``` overwrites old value with new value.

##### Intended consequences.

- Easy to implement ```contains()```.
```java
public boolean contains(Key key)
{ return get(key) != null; }
```
- Can implement lazy version of ```delete()```.
```java
public void delete(Key key)
{ put(key, null); }
```

### 1.5 Keys and values

**Value type**. Any generic type.

##### Key type: several natural assumptions.
- Assume keys are ```Comparable```, use ```compareTo()```.
    > specify Comparable in API.
- Assume keys are any generic type, use ```equals()``` to test equality.
- Assume keys are any generic type, use ```equals()``` to test equality; use ```hashCode()``` to scramble key.
    > built-in to Java
(stay tuned)

**Best practices**. Use immutable types for symbol table keys.
- Immutable in Java: ```Integer```, ```Double```, ```String```, ```java.io.File```, …
- Mutable in Java: ```StringBuilder```, ```java.net.URL```, ```arrays```, ...

### 1.6 Equality test

All Java classes inherit a method ```equals()```.

**Java requirements**. For any references ```x```, ```y``` and ```z```:
- Reflexive: ```x.equals(x)``` is true.
- Symmetric: ```x.equals(y)``` iff ```y.equals(x)```.
- Transitive: if ```x.equals(y)``` and ```y.equals(z)```, then ```x.equals(z)```.
> equivalence
relation
- Non-null: ```x.equals(null)``` is false.

**Default implementation**. ```(x == y)```
> do x and y refer to the same object?

**Customized implementations**. ```Integer```, ```Double```, ```String```, ```java.io.File```, …

**User-defined implementations**. Some care needed.

### 1.7 Implementing equals for user-defined types

Seems easy, but requires some care.

```java
// typically unsafe to use equals() with inheritance (would violate symmetry)
public final class Date implements Comparable<Date>
{
    private final int month;
    private final int day;
    private final int year;
    ...
    
    // must be Object. Why? Experts still debate.
    public boolean equals(Object y)
    {
        // optimize for true object equality
        if (y == this) return true;
        
        // check for null
        if (y == null) return false;
        
        // objects must be in the same class (religion: getClass() vs. instanceof)
        if (y.getClass() != this.getClass())
            return false;
        
        Date that = (Date) y;   // cast is guaranteed to succeed
        // check that all significant fields are the same
        if (this.day != that.day ) return false;
        if (this.month != that.month) return false;
        if (this.year != that.year ) return false;
        return true;
    }
}
```

### 1.8 Equals design

##### "Standard" recipe for user-defined types.
- Optimization for reference equality.
- Check against ```null```.
- Check that two objects are of the same type and cast.
- Compare each significant field:
    - if field is a primitive type, use ```==```
    - if field is an object, use ```equals()```
        > apply rule recursively
    
    - if field is an array, apply to each entry
        > alternatively, use ```Arrays.equals(a, b)``` or ```Arrays.deepEquals(a, b)```, but not ```a.equals(b)```

##### Best practices.
- No need to use calculated fields that depend on other fields.
- Compare fields mostly likely to differ first.
- Make ```compareTo()``` consistent with ```equals()```.
    > ```x.equals(y)``` if and only if ```(x.compareTo(y) == 0)```

### 1.9 ST test client for traces

Build ST by associating value `$i$` with `$i^{th}$` string from standard input.

```java
public static void main(String[] args)
{
    ST<String, Integer> st = new ST<String, Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++)
    {
        String key = StdIn.readString();
        st.put(key, i);
    }
    for (String s : st.keys())
        StdOut.println(s + " " + st.get(s));
}
```

### 1.10 ST test client for analysis

**Frequency counter**. Read a sequence of strings from standard input and print out one that occurs with highest frequency.

### 1.11 Frequency counter implementation

```java
public class FrequencyCounter
{
    public static void main(String[] args)
    {
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();  // create ST
        while (!StdIn.isEmpty())
        {
            String word = StdIn.readString();
            if (word.length() < minlen) continue;   // ignore short strings
            // read string and update frequency
            if (!st.contains(word)) st.put(word, 1);
            else st.put(word, st.get(word) + 1);
        }
        // print a string with max freq
        String max = "";
        st.put(max, 0);
        for (String word : st.keys())
            if (st.get(word) > st.get(max))
                max = word;
        StdOut.println(max + " " + st.get(max));
    }
}
```

## 2. elementary implementations

### 2.1 Sequential search in a linked list

**Data structure**. Maintain an (unordered) linked list of key-value pairs.

**Search**. Scan through all keys until find a match.

**Insert**. Scan through all keys until find a match; if no match add to front.

![image](https://s1.ax1x.com/2018/11/03/i4Jwi6.png)

### 2.2 Elementary ST implementations: summary

![image](https://s1.ax1x.com/2018/11/03/i4J0JK.png)

**Challenge**. Efficient implementations of both search and insert.

### 2.3 Binary search in an ordered array

**Data structure**. Maintain an ordered array of key-value pairs.

**Rank helper function**. How many keys `$< k$` ?

![image](https://s1.ax1x.com/2018/11/03/i4JDzD.png)

### 2.4 Binary search: Java implementation

```java
public Value get(Key key)
{
    if (isEmpty()) return null;
    int i = rank(key);
    if (i < N && keys[i].compareTo(key) == 0) return vals[i];
    else return null;
}
private int rank(Key key)   // number of keys < key
{
    int lo = 0, hi = N-1;
    while (lo <= hi)
    {
        int mid = lo + (hi - lo) / 2;
        int cmp = key.compareTo(keys[mid]);
        if (cmp < 0) hi = mid - 1;
        else if (cmp > 0) lo = mid + 1;
        else if (cmp == 0) return mid;
    }
    return lo;
}
```

### 2.5 Binary search: trace of standard indexing client

**Problem**. To insert, need to shift all greater keys over.

![image](https://s1.ax1x.com/2018/11/03/i4J4W8.png)

### 2.6 Elementary ST implementations: summary

![image](https://s1.ax1x.com/2018/11/03/i4JoQg.png)

**Challenge**. Efficient implementations of both search and insert.

## 3. ordered operations

### 3.1 Examples of ordered symbol table API

![image](https://s1.ax1x.com/2018/11/03/i4UuXF.png)

### 3.2 Ordered symbol table API

![image](https://s1.ax1x.com/2018/11/03/i4UGfx.png)

### 3.3 Binary search: ordered symbol table operations summary

![image](https://s1.ax1x.com/2018/11/03/i4Ut1K.png)
