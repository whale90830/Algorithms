# Binary Search Trees

## 1. BSTs

### 1.1 Binary search trees

**Definition**. A BST is a **binary tree** in **symmetric order**.

##### A binary tree is either:
- Empty.
- Two disjoint binary trees (left and right).

**Symmetric order**. Each node has a key,
and every node’s key is:
- Larger than all keys in its left subtree.
- Smaller than all keys in its right subtree.

![image](https://s1.ax1x.com/2018/11/03/i4R6X9.png)

### 1.2 BST representation in Java

**Java definition**. A BST is a reference to a root ```Node```.

A ```Node``` is comprised of four fields:
- A ```Key``` and a ```Value```.
- A reference to the left and right subtree.
    > left: smaller keys; right: larger keys

```java
private class Node
{
    private Key key;
    private Value val;
    private Node left, right;
    public Node(Key key, Value val)
    {
        this.key = key;
        this.val = val;
    }
}
```

![image](https://s1.ax1x.com/2018/11/03/i4Rq0I.png)

### 1.3 BST implementation (skeleton)

```java
public class BST<Key extends Comparable<Key>, Value>
{
    private Node root;  // root of BST
    
    private class Node
    { /* see previous slide */ }
    
    public void put(Key key, Value val)
    { /* see next slides */ }
    
    public Value get(Key key)
    { /* see next slides */ }
    
    public void delete(Key key)
    { /* see next slides */ }
    
    public Iterable<Key> iterator()
    { /* see next slides */ }
}
```

### 1.4 Binary search tree demo

**Search**. If less, go left; if greater, go right; if equal, search hit.

**Insert**. If less, go left; if greater, go right; if null, insert.

### 1.5 BST search: Java implementation

**Get**. Return value corresponding to given key, or ```null``` if no such key.

```java
public Value get(Key key)
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

**Cost**. Number of compares is equal to 1 + depth of node.

### 1.6 BST insert

**Put**. Associate value with key.

Search for key, then two cases:
- Key in tree ⇒ reset value.
- Key not in tree ⇒ add new node.

![image](https://s1.ax1x.com/2018/11/03/i44du4.png)

### 1.7 BST insert: Java implementation

**Put**. Associate value with key.

```java
public void put(Key key, Value val)
{ root = put(root, key, val); }

private Node put(Node x, Key key, Value val)
{
    if (x == null) return new Node(key, val);
    int cmp = key.compareTo(x.key);
    if (cmp < 0)
        x.left = put(x.left, key, val);
    else if (cmp > 0)
        x.right = put(x.right, key, val);
    else if (cmp == 0)
        x.val = val;
    return x;
}
```

**Cost**. Number of compares is equal to 1 + depth of node.

### 1.8 Tree shape

- Many BSTs correspond to same set of keys.
- Number of compares for search/insert is equal to 1 + depth of node.

![image](https://s1.ax1x.com/2018/11/03/i44DER.png)

**Remark**. Tree shape depends on order of insertion.

### 1.9 Correspondence between BSTs and quicksort partitioning

![image](https://s1.ax1x.com/2018/11/03/i44rU1.png)

**Remark**. Correspondence is 1-1 if array has no duplicate keys.

### 1.10 BSTs: mathematical analysis

**Proposition**. If `$N$` distinct keys are inserted into a BST in **random** order, the expected number of compares for a search/insert is `$\sim 2 \ln N$`

**Pf**. 1-1 correspondence with quicksort partitioning.

**Proposition**. [Reed, 2003] If `$N$` distinct keys are inserted in random order, expected height of tree is `$\sim 4.311 \ln N$`

**But**… Worst-case height is `$N$`. (exponentially small chance when keys are inserted in random order)

### 1.11 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/03/i44IUI.png)

## 2. ordered operations

### 2.1 Minimum and maximum

**Minimum**. Smallest key in table.

**Maximum**. Largest key in table.

![image](https://s1.ax1x.com/2018/11/03/i45qF1.png)

**Q**. How to find the min / max?

### 2.2 Floor and ceiling

**Floor**. Largest key ≤ a given key.

**Ceiling**. Smallest key ≥ a given key.

![image](https://s1.ax1x.com/2018/11/03/i45xyD.png)

**Q**. How to find the floor / ceiling?

### 2.3 Computing the floor

**Case 1**. [k equals the key at root]
The floor of k is k.

**Case 2**. [k is less than the key at root]
The floor of k is in the left subtree.

**Case 3**. [k is greater than the key at root]
The floor of k is in the right subtree
(if there is any key ≤ k in right subtree);
otherwise it is the key in the root.

![image](https://s1.ax1x.com/2018/11/03/i4IiFI.png)

```java
public Key floor(Key key)
{
    Node x = floor(root, key);
    if (x == null) return null;
    return x.key;
}

private Node floor(Node x, Key key)
{
    if (x == null) return null;
    int cmp = key.compareTo(x.key);
    
    if (cmp == 0) return x;
    
    if (cmp < 0) return floor(x.left, key);
    
    Node t = floor(x.right, key);
    if (t != null) return t;
    else return x;
}
```

### 2.4 Subtree counts

In each node, we store the number of nodes in the subtree rooted at that node; to implement ```size()```, return the count at the root.

![image](https://s1.ax1x.com/2018/11/03/i4IZ6S.png)

**Remark**. This facilitates efficient implementation of rank() and select().

### 2.5 BST implementation: subtree counts

```java
private class Node
{
    private Key key;
    private Value val;
    private Node left;
    private Node right;
    private int count;  // number of nodes in subtree
}
```

```java
public int size()
{ return size(root); }

private int size(Node x)
{
    if (x == null) return 0;    // ok to call when x is null
    return x.count;
}
```

```java
private Node put(Node x, Key key, Value val)
{
    if (x == null) return new Node(key, val, 1);
    int cmp = key.compareTo(x.key);
    if (cmp < 0) x.left = put(x.left, key, val);
    else if (cmp > 0) x.right = put(x.right, key, val);
    else if (cmp == 0) x.val = val;
    x.count = 1 + size(x.left) + size(x.right);
    return x;
}
```

### 2.6 Rank

**Rank**. How many keys `$< k$` ?

Easy recursive algorithm (3 cases!)

```java
public int rank(Key key)
{ return rank(key, root); }

private int rank(Key key, Node x)
{
    if (x == null) return 0;
    int cmp = key.compareTo(x.key);
    if (cmp < 0) return rank(key, x.left);
    else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
    else if (cmp == 0) return size(x.left);
}
```

### 2.7 Inorder traversal

- Traverse left subtree.
- Enqueue key.
- Traverse right subtree.

```java
public Iterable<Key> keys()
{
    Queue<Key> q = new Queue<Key>();
    inorder(root, q);
    return q;
}

private void inorder(Node x, Queue<Key> q)
{
    if (x == null) return;
    inorder(x.left, q);
    q.enqueue(x.key);
    inorder(x.right, q);
}
```

![image](https://s1.ax1x.com/2018/11/03/i4Ivhq.png)

**Property**. Inorder traversal of a BST yields keys in ascending order.

### 2.8 BST: ordered symbol table operations summary

![image](https://s1.ax1x.com/2018/11/03/i4Iz90.png)

## 3. deletion

### 3.1 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/03/i4oENR.png)

Next. Deletion in BSTs.

### 3.2 BST deletion: lazy approach

##### To remove a node with a given key:
- Set its value to null.
- Leave key in tree to guide search (but don't consider it equal in search).

![image](https://s1.ax1x.com/2018/11/03/i4o3Ed.png)

**Cost**. `$\sim 2 \ln N'$` per insert, search, and delete (if keys in random order), where `$N'$` is the number of key-value pairs ever inserted in the BST.

**Unsatisfactory solution**. Tombstone (memory) overload.

### 3.3 Deleting the minimum

##### To delete the minimum key:
- Go left until finding a node with a null left link.
- Replace that node by its right link.
- Update subtree counts.

![image](https://s1.ax1x.com/2018/11/03/i4oDEj.png)

```java
public void deleteMin()
{ root = deleteMin(root); }

private Node deleteMin(Node x)
{
    if (x.left == null) return x.right;
    x.left = deleteMin(x.left);
    x.count = 1 + size(x.left) + size(x.right);
    return x;
}
```

### 3.4 Hibbard deletion

To delete a node with key ```k```: search for node ```t``` containing key ```k```.

**Case 0**. [0 children] Delete ```t``` by setting parent link to null.

![image](https://s1.ax1x.com/2018/11/03/i4oWKU.png)

**Case 1**. [1 child] Delete ```t``` by replacing parent link.

![image](https://s1.ax1x.com/2018/11/03/i4ohb4.png)

**Case 2**. [2 children]
- Find successor ```x``` of ```t```.
- Delete the minimum in ```t```'s right subtree.
- Put ```x``` in ```t```'s spot.

![image](https://s1.ax1x.com/2018/11/03/i4oqxK.png)

x has no left child
but don't garbage collect x
still a BST

### 3.5 Hibbard deletion: Java implementation

```java
public void delete(Key key)
{ root = delete(root, key); }

private Node delete(Node x, Key key) {
    if (x == null) return null;
    int cmp = key.compareTo(x.key);
    // search for key
    if (cmp < 0) x.left = delete(x.left, key);
    else if (cmp > 0) x.right = delete(x.right, key);
    else {
        if (x.right == null) return x.left;    // no right child
        if (x.left == null) return x.right;    // no left child
        
        // replace with successor
        Node t = x;
        x = min(t.right);
        x.right = deleteMin(t.right);
        x.left = t.left;
    }
    x.count = size(x.left) + size(x.right) + 1;    // update subtree counts
    return x;
}
```

### 3.6 Hibbard deletion: analysis

**Unsatisfactory solution**. Not symmetric.

**Surprising consequence**. Trees not random (!) ⇒ `sqrt (N)` per op.

**Longstanding open problem**. Simple and efficient delete for BSTs.

### 3.7 ST implementations: summary

![image](https://s1.ax1x.com/2018/11/14/ijkSSO.md.png)

**Next lecture**. Guarantee logarithmic performance for all operations.