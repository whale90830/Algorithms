# Symbol Table Applications

## 1. sets

### 1.1 Set API

**Mathematical set**. A collection of distinct keys.

![image](https://s1.ax1x.com/2018/12/02/FKAotg.png)

**Q**. How to implement?

### 1.2 Exception filter

- Read in a list of words from one file.
- Print out all words from standard input that are { in, not in } the list.

![image](https://s1.ax1x.com/2018/12/02/FKAIAS.png)

### 1.3 Exception filter applications

![image](https://s1.ax1x.com/2018/12/02/FKA478.png)

### 1.4 Exception filter: Java implementation

- Read in a list of words from one file.
- Print out all words from standard input that are in the list.

```java
public class WhiteList
{
    public static void main(String[] args)
    {
        SET<String> set = new SET<String>();  // create empty set of strings
        
        In in = new In(args[0]);
        while (!in.isEmpty())   // read in whitelist
            set.add(in.readString());
        
        while (!StdIn.isEmpty())
        {
            String word = StdIn.readString();
            if (set.contains(word)) // print words in list
                StdOut.println(word);
        }
    }
}
```

- Print out all words from standard input that are **not** in the list.

```java
public class BlackList
{
    public static void main(String[] args)
    {
        SET<String> set = new SET<String>();  // create empty set of strings
        
        In in = new In(args[0]);
        while (!in.isEmpty())   // read in whitelist
            set.add(in.readString());
        
        while (!StdIn.isEmpty())
        {
            String word = StdIn.readString();
            if (!set.contains(word)) // print words not in list
                StdOut.println(word);
        }
    }
}
```

## 2. dictionary clients

### 2.1 Dictionary lookup

##### Command-line arguments.
- A comma-separated value (CSV) file.
- Key field.
- Value field.

**Ex 1**. DNS lookup.

![image](https://s1.ax1x.com/2018/12/03/FK7x1J.png)
![image](https://s1.ax1x.com/2018/12/03/FK7zc9.png)

**Ex 2**. Amino acids.

![image](https://s1.ax1x.com/2018/12/03/FK7XhF.png)
![image](https://s1.ax1x.com/2018/12/03/FK7vp4.png)

**Ex 3**. Class list.

![image](https://s1.ax1x.com/2018/12/03/FK7OtU.png)
![image](https://s1.ax1x.com/2018/12/03/FKHSXR.png)

### 2.2 Dictionary lookup: Java implementation

```java
public class LookupCSV
{
    public static void main(String[] args)
    {
        // process input file
        In in = new In(args[0]);
        int keyField = Integer.parseInt(args[1]);
        int valField = Integer.parseInt(args[2]);
        
        // build symbol table
        ST<String, String> st = new ST<String, String>();
        while (!in.isEmpty())
        {
            String line = in.readLine();
            String[] tokens = line.split(",");
            String key = tokens[keyField];
            String val = tokens[valField];
            st.put(key, val);
        }
        
        // process lookups with standard I/O
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            if (!st.contains(s)) StdOut.println("Not found");
            else StdOut.println(st.get(s));
        }
    }
}
```

## 3. indexing clients

### 3.1 File indexing

**Goal**. Index a PC (or the web).

**Goal**. Given a list of files specified, create an index so that you can efficiently find all files containing a given query string.

![image](https://s1.ax1x.com/2018/12/03/FMDsxg.png)

**Solution**. Key = query string; value = set of files containing that string.

```java
import java.io.File;
public class FileIndex
{
    public static void main(String[] args)
    {
        ST<String, SET<File>> st = new ST<String, SET<File>>();    // symbol table
        
        // list of file names from command line
        for (String filename : args) {
            File file = new File(filename);
            In in = new In(file);
            // for each word in file, add file to corresponding set
            while (!in.isEmpty())
            {
                String key = in.readString();
                if (!st.contains(key))
                    st.put(key, new SET<File>());
                SET<File> set = st.get(key);
                set.add(file);
            }
        }
        
        while (!StdIn.isEmpty())
        {
            String query = StdIn.readString(); // process queries
            StdOut.println(st.get(query));
        }
    }
}
```

### 3.2 Book index

**Goal**. Index for an e-book.

### 3.3 Concordance

**Goal**. Preprocess a text corpus to support concordance queries: given a word, find all occurrences with their immediate contexts.

![image](https://s1.ax1x.com/2018/12/03/FMDrRS.png)

### 3.4 Concordance

```java
public class Concordance
{
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] words = in.readAllStrings();
        ST<String, SET<Integer>> st = new ST<String, SET<Integer>>();
        // read text and build index
        for (int i = 0; i < words.length; i++)
        {
            String s = words[i];
            if (!st.contains(s))
                st.put(s, new SET<Integer>());
            SET<Integer> set = st.get(s);
            set.add(i);
        }
        
        // process queries and print concordances
        while (!StdIn.isEmpty())
        {
            String query = StdIn.readString();
            SET<Integer> set = st.get(query);
            for (int k : set)
                // print words[k-4] to words[k+4]
        }
    }
}
```

## 4. sparse vectors

### 4.1 Matrix-vector multiplication (standard implementation)

![image](https://s1.ax1x.com/2018/12/03/FMrDT1.png)

```java
...
double[][] a = new double[N][N];
double[] x = new double[N];
double[] b = new double[N];
...
// initialize a[][] and x[]
...
// nested loops (N^2 running time)
for (int i = 0; i < N; i++)
{
    sum = 0.0;
    for (int j = 0; j < N; j++)
        sum += a[i][j]*x[j];
    b[i] = sum;
}
```

### 4.2 Sparse matrix-vector multiplication

**Problem**. Sparse matrix-vector multiplication.

**Assumptions**. Matrix dimension is 10,000; average nonzeros per row ~ 10.

![image](https://s1.ax1x.com/2018/12/03/FMrBwR.png)

### 4.3 Vector representations

##### 1d array (standard) representation.
- Constant time access to elements.
- Space proportional to N.

![image](https://s1.ax1x.com/2018/12/03/FMrdOJ.png)

##### Symbol table representation.
- Key = index, value = entry.
- Efficient iterator.
- Space proportional to number of nonzeros.

![image](https://s1.ax1x.com/2018/12/03/FMray4.png)

### 4.4 Sparse vector data type

```java
public class SparseVector
{
    // HashST because order not important
    private HashST<Integer, Double> v;
    
    // empty ST represents all 0s vector
    public SparseVector()
    { v = new HashST<Integer, Double>(); }
    
    // a[i] = value
    public void put(int i, double x)
    { v.put(i, x); }
    
    // return a[i]
    public double get(int i)
    {
        if (!v.contains(i)) return 0.0;
        else return v.get(i);
    }
    
    public Iterable<Integer> indices()
    { return v.keys(); }
    
    // dot product is constant time for sparse vectors
    public double dot(double[] that)
    {
        double sum = 0.0;
        for (int i : indices())
            sum += that[i]*this.get(i);
        return sum;
    }
}
```

### 4.5 Matrix representations

**2D array (standard) matrix representation**: Each row of matrix is an **array**.
- Constant time access to elements.
- Space proportional to `$N^2$`.

**Sparse matrix representation**: Each row of matrix is a **sparse vector**.
- Efficient access to elements.
- Space proportional to number of nonzeros (plus N).

![image](https://s1.ax1x.com/2018/12/03/FMr0m9.png)

### 4.6 Sparse matrix-vector multiplication

```java
...
SparseVector[] a = new SparseVector[N];
double[] x = new double[N];
double[] b = new double[N];
...
// Initialize a[] and x[]
...
// linear running time for sparse matrix
for (int i = 0; i < N; i++)
    b[i] = a[i].dot(x);
```