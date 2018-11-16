# Balanced Search Trees

### Symbol table review

![image](https://s1.ax1x.com/2018/11/14/ijkkTI.png)

**Challenge**. Guarantee performance.

**This lecture**. 2-3 trees, left-leaning red-black BSTs, B-trees.

## 1. 2-3 search trees

### 1.1 2-3 tree

##### Allow 1 or 2 keys per node.
- 2-node: one key, two children.
- 3-node: two keys, three children.

![image](https://s1.ax1x.com/2018/11/14/ijAQgO.png)

**Symmetric order**. Inorder traversal yields keys in ascending order.

**Perfect balance**. Every path from root to null link has same length.

### 1.2 2-3 tree demo

##### Search.
- Compare search key against keys in node.
- Find interval containing search key.
- Follow associated link (recursively).

##### Insertion into a 3-node at bottom.
- Add new key to 3-node to create temporary 4-node.
- Move middle key in 4-node into parent.
- Repeat up the tree, as necessary.
- If you reach the root and it's a 4-node, split it into three 2-nodes.

### 1.3 Local transformations in a 2-3 tree

Splitting a 4-node is a local transformation: constant number of operations.

![image](https://s1.ax1x.com/2018/11/14/ijVCXF.png)

### 1.4 Global properties in a 2-3 tree

**Invariants**. Maintains symmetric order and perfect balance.

**Pf**. Each transformation maintains symmetric order and perfect balance.

![image](https://s1.ax1x.com/2018/11/14/ijVF0J.png)

### 1.5 2-3 tree: performance

**Perfect balance**. Every path from root to null link has same length.

##### Tree height.
- Worst case: `$\lg N$`. [all 2-nodes]
- Best case: `$\log_3 N ≈ .631 \lg N$`. [all 3-nodes]
- Between 12 and 20 for a million nodes.
- Between 18 and 30 for a billion nodes.

Guaranteed **logarithmic** performance for search and insert.

### 1.6 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/14/ijeBYn.png)

### 1.7 2-3 tree: implementation?

##### Direct implementation is complicated, because:
- Maintaining multiple node types is cumbersome.
- Need multiple compares to move down tree.
- Need to move back up the tree to split 4-nodes.
- Large number of cases for splitting.

**Bottom line**. Could do it, but there's a better way.

## 2. red-black BSTs

### 2.1 Left-leaning red-black BSTs (Guibas-Sedgewick 1979 and Sedgewick 2007)

1. Represent 2–3 tree as a BST.
2. Use "internal" left-leaning links as "glue" for 3–nodes.

![image](https://s1.ax1x.com/2018/11/14/ijMgIg.png)

### 2.2 An equivalent definition

A BST such that:
- No node has two red links connected to it.
- Every path from root to null link has the same number of black links.
    > "perfect black balance"
- Red links lean left.

### 2.3 Left-leaning red-black BSTs: 1-1 correspondence with 2-3 trees

**Key property**. 1–1 correspondence between 2–3 and LLRB.

![image](https://s1.ax1x.com/2018/11/14/ijMWGj.png)

### 2.4 Search implementation for red-black BSTs

**Observation**. Search is the same as for elementary BST (ignore color).
> but runs faster because of better balance

```java
public Val get(Key key)
{
    Node x = root;
    while (x != null)
    {
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x = x.left;
        else if (cmp > 0) x = x.right;
        else if (cmp == 0) return x.val;
    }
    return null;
}
```

**Remark**. Most other ops (e.g., floor, iteration, selection) are also identical.

### 2.5 Red-black BST representation

Each node is pointed to by precisely one link (from its parent) ⇒
can encode color of links in nodes.

![image](https://s1.ax1x.com/2018/11/14/ij8p1H.png)

```java
private static final boolean RED = true;
private static final boolean BLACK = false;

private class Node
{
    Key key;
    Value val;
    Node left, right;
    boolean color; // color of parent link
}

private boolean isRed(Node x)
{
    if (x == null) return false;    // null links are black
    return x.color == RED;
}
```

### 2.6 Elementary red-black BST operations

**Left rotation**. Orient a (temporarily) right-leaning red link to lean left.

![image](https://s1.ax1x.com/2018/11/14/ij8inI.png)
![image](https://s1.ax1x.com/2018/11/14/ij8CjA.png)

```java
private Node rotateLeft(Node h)
{
    assert isRed(h.right);
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;
    return x;
}
```

**Right rotation**. Orient a left-leaning red link to (temporarily) lean right.

![image](https://s1.ax1x.com/2018/11/14/ij8ucj.png)
![image](https://s1.ax1x.com/2018/11/14/ij8n3Q.png)

```java
private Node rotateRight(Node h)
{
    assert isRed(h.left);
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;
    return x;
}
```

**Color flip**. Recolor to split a (temporary) 4-node.

![image](https://s1.ax1x.com/2018/11/14/ij8rE6.png)
![image](https://s1.ax1x.com/2018/11/14/ij8BHx.png)

```java
private void flipColors(Node h)
{
    assert !isRed(h);
    assert isRed(h.left);
    assert isRed(h.right);
    h.color = RED;
    h.left.color = BLACK;
    h.right.color = BLACK;
}
```

**Invariants**. Maintains symmetric order and perfect black balance.

### 2.7 Insertion in a LLRB tree: overview

**Basic strategy**. Maintain 1-1 correspondence with 2-3 trees by applying elementary red-black BST operations.

![image](https://s1.ax1x.com/2018/11/14/ijUtC6.png)

### 2.8 Insertion in a LLRB tree

**Warmup 1**. Insert into a tree with exactly 1 node.

![image](https://s1.ax1x.com/2018/11/14/ijUN8K.png)

**Case 1**. Insert into a 2-node at the bottom.
- Do standard BST insert; color new link red.
- If new red link is a right link, rotate left.

![image](https://s1.ax1x.com/2018/11/14/ijUavD.png)

**Warmup 2**. Insert into a tree with exactly 2 nodes.

![image](https://s1.ax1x.com/2018/11/14/ijUBbd.png)

**Case 2**. Insert into a 3-node at the bottom.
- Do standard BST insert; color new link red.
- Rotate to balance the 4-node (if needed).
- Flip colors to pass red link up one level.
- Rotate to make lean left (if needed).

![image](https://s1.ax1x.com/2018/11/14/ijaeZd.png)

### 2.9 Insertion in a LLRB tree: passing red links up the tree

**Case 2**. Insert into a 3-node at the bottom.
- Do standard BST insert; color new link red.
- Rotate to balance the 4-node (if needed).
- Flip colors to pass red link up one level.
- Rotate to make lean left (if needed).
- **Repeat case 1 or case 2 up the tree (if needed)**.

![image](https://s1.ax1x.com/2018/11/14/ijamdA.png)

### 2.10 Insertion in a LLRB tree: Java implementation

##### Same code for all cases.
- Right child red, left child black: rotate left.
- Left child, left-left grandchild red: rotate right.
- Both children red: flip colors.

![image](https://s1.ax1x.com/2018/11/14/ijanII.png)

```java
private Node put(Node h, Key key, Value val)
{
    if (h == null) return new Node(key, val, RED);   // insert at bottom (and color it red)
    int cmp = key.compareTo(h.key);
    if (cmp < 0) h.left = put(h.left, key, val);
    else if (cmp > 0) h.right = put(h.right, key, val);
    else if (cmp == 0) h.val = val;
    
    // only a few extra lines of code provides near-perfect balance
    if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);  // lean left
    if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h); // balance 4-node
    if (isRed(h.left) && isRed(h.right)) flipColors(h);  // split 4-node
    
    return h;
}
```

### 2.11 Balance in LLRB trees

**Proposition**. Height of tree is ≤ 2 lg N in the worst case.
**Pf**.
- Every path from root to null link has same number of black links.
- Never two red links in-a-row.

**Property**. Height of tree is `$\sim 1.00 \lg N$` in typical applications.

### 2.12 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/14/ija4SK.png)

### 2.13 War story: why red-black?

##### Xerox PARC innovations. [1970s]
- Alto.
- GUI.
- Ethernet.
- Smalltalk.
- InterPress.
- **Laser printing**.
- Bitmapped display.
- WYSIWYG text editor.
- ...

### 2.14 War story: red-black BSTs

Telephone company contracted with database provider to build real-time database to store customer information.

##### Database implementation.
- Red-black BST search and insert; Hibbard deletion.
- Exceeding height limit of 80 triggered error-recovery process.
    > allows for up to 240 keys

##### Extended telephone service outage.
- Main cause = height bounded exceeded!
    > Hibbard deletion was the problem
- Telephone company sues database provider.
- Legal testimony:

## 3. B-trees

### 3.1 File system model

**Page**. Contiguous block of data (e.g., a file or 4,096-byte chunk).

**Probe**. First access to a page (e.g., from disk to memory).

**Property**. Time required for a probe is much larger than time to access data within a page.

**Cost model**. Number of probes.

**Goal**. Access data using minimum number of probes.

### 3.2 B-trees (Bayer-McCreight, 1972)

**B-tree**. Generalize 2-3 trees by allowing up to M - 1 key-link pairs per node.
- At least 2 key-link pairs at root.
- At least M / 2 key-link pairs in other nodes.
- External nodes contain client keys.
- Internal nodes contain copies of keys to guide search.

![image](https://s1.ax1x.com/2018/11/14/ijwQKS.png)

### 3.3 Searching in a B-tree

- Start at root.
- Find interval for search key and take corresponding link.
- Search terminates in external node.

![image](https://s1.ax1x.com/2018/11/14/ijw1bQ.png)

### 3.4 Insertion in a B-tree

- Search for new key.
- Insert at bottom.
- Split nodes with M key-link pairs on the way up the tree.

![image](https://s1.ax1x.com/2018/11/14/ijw8Ej.png)

### 3.5 Balance in B-tree

**Proposition**. A search or an insertion in a B-tree of order M with N keys requires between log M-1 N and log M/2 N probes.

**Pf**. All internal nodes (besides root) have between M / 2 and M - 1 links.

**In practice**. Number of probes is at most 4.

**Optimization**. Always keep root page in memory.

### 3.6 Balanced trees in the wild

##### Red-black trees are widely used as system symbol tables.
- Java: java.util.TreeMap, java.util.TreeSet.
- C++ STL: map, multimap, multiset.
- Linux kernel: completely fair scheduler, linux/rbtree.h.
- Emacs: conservative stack scanning.

**B-tree variants**. B+ tree, B*tree, B# tree, …

##### B-trees (and variants) are widely used for file systems and databases.
- Windows: NTFS.
- Mac: HFS, HFS+.
- Linux: ReiserFS, XFS, Ext3FS, JFS.
- Databases: ORACLE, DB2, INGRES, SQL, PostgreSQL.