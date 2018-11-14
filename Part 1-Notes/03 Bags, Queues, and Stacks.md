# Bags, Queues, and Stacks

### 0.1 Stacks and queues

##### Fundamental data types.
- Value: collection of objects.
- Operations: insert, remove, iterate, test if empty.
- Intent is clear when we insert.
- Which item do we remove?

**Stack.** Examine the item most recently added.
> LIFO = "last in first out"

**Queue.** Examine the item least recently added.
> FIFO = "first in first out"

### 0.2 Client, implementation, interface

##### Separate interface and implementation.
> stack, queue, bag, priority queue, symbol table, union-find, .…

##### Benefits.
- Client can't know details of implementation⇒client has many implementation from which to choose.
- Implementation can't know details of client needs⇒many clients can re-use the same implementation.
- **Design**: creates modular, reusable libraries.
- **Performance**: use optimized implementation where it matters.

> - **Client**: program using operations defined in interface.
> - **Implementation**: actual code implementing operations.
> - **Interface**: description of data type, basic operations.

## 1. stacks

### 1.1 Stack API

**Warmup API.** Stack of strings data type.

public class StackOfStrings|/
---|---
StackOfStrings() | *create an empty stack*
void push(String item) | *insert a new string onto stack*
String pop() | *remove and return the string most recently added*
boolean isEmpty() | *is the stack empty?*
int size() | *number of strings on the stack*

**Warmup client.** Reverse sequence of strings from standard input.

### 1.2 Stack test client

Read strings from standard input.
- If string equals "-", pop string from stack and print.
- Otherwise, push string onto stack.

```java
public static void main(String[] args)
{
    StackOfStrings stack = new StackOfStrings();
    while (!StdIn.isEmpty())
    {
        String s = StdIn.readString();
        if (s.equals("-")) 
            StdOut.print(stack.pop());
        else 
            stack.push(s);
    }
}
```
> - % more tobe.txt
> - to be or not to - be - - that - - - is
> - % java StackOfStrings < tobe.txt
> - to be not that or be

### 1.3 Stack: linked-list representation

Maintain pointer to first node in a linked list; insert/remove from front.
![image](https://s1.ax1x.com/2018/10/19/i0Aeit.png)

### 1.4 Stack pop: linked-list implementation
![image](https://s1.ax1x.com/2018/10/19/i0AmJP.png)

### 1.5 Stack push: linked-list implementation
![image](https://s1.ax1x.com/2018/10/19/i0AVII.png)

### 1.6 Stack: linked-list implementation in Java

```java
public class LinkedStackOfStrings
{
    private Node first = null;
    
    //private inner class (access modifiers don't matter)
    private class Node
    {
        String item;
        Node next;
    }
    
    public boolean isEmpty()
    { return first == null; }
    
    public void push(String item)
    {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }
    
    public String pop()
    {
        String item = first.item;
        first = first.next;
        return item;
    }
}
```

### 1.7 Stack: linked-list implementation performance

**Proposition.** Every operation takes constant time in the worst case.

**Proposition.** A stack with N items uses ~ 40 N bytes.

![image](https://s1.ax1x.com/2018/10/19/i0Arw9.md.png)

**Remark.** This accounts for the memory for the stack
(but not the memory for strings themselves, which the client owns).

### 1.8 Stack: array implementation

##### Array implementation of a stack.
- Use array s[] to store N items on stack.
- push(): add new item at s[N].
- pop(): remove item from s[N-1].

![image](https://s1.ax1x.com/2018/10/19/i0AhOe.png)

**Defect.** Stack overflows when N exceeds capacity. [stay tuned]

```java
public class FixedCapacityStackOfStrings
{
    private String[] s;
    private int N = 0;
    
    public FixedCapacityStackOfStrings(int capacity)
    { s = new String[capacity]; }
    
    public boolean isEmpty()
    { return N == 0; }
    
    public void push(String item)
    { s[N++] = item; }
    //use to index into array; then increment N
    
    public String pop()
    { return s[--N]; }
    //decrement N; then use to index into array
}
```

### 1.9 Stack considerations

##### Overflow and underflow.
- Underflow: throw exception if pop from an empty stack.
- Overflow: use resizing array for array implementation. [stay tuned]

**Null items**. We allow null items to be inserted.

**Loitering**. Holding a reference to an object when it is no longer needed.

```java
// loitering
public String pop()
{ return s[--N]; }
```

```java
//this version avoids "loitering":
//garbage collector can reclaim memory only if no outstanding references
public String pop()
{
    String item = s[--N];
    s[N] = null;
    return item;
}
```

## 2. resizing arrays

### 2.1 Stack: resizing-array implementation

**Q**. How to grow and shrink array?

##### First try.
- push(): increase size of array s[] by 1.
- pop(): decrease size of array s[] by 1.

##### Too expensive.
- Need to copy all items to a new array.
- Inserting first N items takes time proportional to `$1+2+...+N \sim N^2 / 2$`
    > infeasible for large N

**Challenge**. Ensure that array resizing happens infrequently.

### 2.2 Stack: resizing-array implementation

**Q**. How to grow array?

**A**. If array is full, create a new array of twice ( **repeated doubling** ) the size, and copy items.

```java
public ResizingArrayStackOfStrings()
{ s = new String[1]; }

public void push(String item)
{
    if (N == s.length) resize(2 * s.length);
    s[N++] = item;
}

private void resize(int capacity)
{
    String[] copy = new String[capacity];
    for (int i = 0; i < N; i++)
        copy[i] = s[i];
    s = copy;
}
```

**Consequence**. Inserting first N items takes time proportional to `$N$` (not `$N^2$` ).

### 2.3 Stack: amortized cost of adding to a stack

##### Cost of inserting first N items.
`$N + (2 + 4 + 8 + … + N) \sim 3N$`
- `$N$` ：1 array access per push
- `$2 + 4 + 8 + … + N$` ：k array accesses to double to size k (ignoring cost to create new array)

![image](https://s1.ax1x.com/2018/10/20/i0xWTK.png)

### 2.4 Stack: resizing-array implementation

**Q**. How to shrink array?

##### First try.
- push(): double size of array s[] when array is full.
- pop(): halve size of array s[] when array is one-half full.

##### Too expensive in worst case.
- Consider push-pop-push-pop-… sequence (thrashing) when array is full.
- Each operation takes time proportional to N.

### 2.5 Stack: resizing-array implementation

**Q**. How to shrink array?

##### Efficient solution.
- push(): double size of array s[] when array is full.
- pop(): halve size of array s[] when array is one-quarter full.

```java
public String pop()
{
    String item = s[--N];
    s[N] = null;
    if (N > 0 && N == s.length/4) resize(s.length/2);
    return item;
}
```

**Invariant**. Array is between 25% and 100% full.

### 2.6 Stack: resizing-array implementation trace

![image](https://s1.ax1x.com/2018/10/20/i0zSpQ.png)

### 2.7 Stack resizing-array implementation: performance

**Amortized analysis**. Average running time per operation over a worst-case sequence of operations.

**Proposition**. Starting from an empty stack, any sequence of M push and pop operations takes time proportional to M.

![image](https://s1.ax1x.com/2018/10/20/i0zEkT.png)

### 2.8 Stack resizing-array implementation: memory usage

**Proposition**. Uses between `$\sim 8N$` and `$\sim 32N$` bytes to represent a stack with `$N$` items.
- `$\sim 8N$` when full.
- `$\sim 32N$` when one-quarter full.

![image](https://s1.ax1x.com/2018/10/20/i0zGtO.png)

**Remark**. This accounts for the memory for the stack
(but not the memory for strings themselves, which the client owns).

### 2.9 Stack implementations: resizing array vs. linked list

**Tradeoffs**. Can implement a stack with either resizing array or linked list; client can use interchangeably. Which one is better?

##### Linked-list implementation.
- Every operation takes constant time in the worst case.
- Uses extra time and space to deal with the links.

##### Resizing-array implementation.
- Every operation takes constant amortized time.
- Less wasted space.

## 3. queues

### 3.1 Queue API

public class QueueOfStrings | /
---|---
QueueOfStrings() | *create an empty queue*
void enqueue(String item) | *insert a new string onto queue*
String dequeue() | *remove and return the string least recently added*
boolean isEmpty() | *is the queue empty?*
int size() | *number of strings on the queue*

### 3.2 Queue: linked-list representation

Maintain pointer to first and last nodes in a linked list;
insert/remove from opposite ends.

![image](https://s1.ax1x.com/2018/10/20/iBS0xJ.png)

### 3.3 Queue dequeue: linked-list implementation

![image](https://s1.ax1x.com/2018/10/20/iBSsq1.png)

**Remark**. Identical code to linked-list stack pop().

### 3.4 Queue enqueue: linked-list implementation

![image](https://s1.ax1x.com/2018/10/20/iBSfRe.png)

### 3.5 Queue: linked-list implementation in Java

```java
public class LinkedQueueOfStrings
{
    private Node first, last;
    
    private class Node
    { /* same as in StackOfStrings */ }
    
    public boolean isEmpty()
    { return first == null; }
    
    public void enqueue(String item)
    {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        //special cases for empty queue
        if (isEmpty()) first = last;
        else oldlast.next = last;
    }
    
    public String dequeue()
    {
        String item = first.item;
        first = first.next;
        //special cases for empty queue
        if (isEmpty()) last = null;
        return item;
    }
}
```

### 3.6 Queue: resizing array implementation

##### Array implementation of a queue.
- Use array q[] to store items in queue.
- enqueue(): add new item at q[tail].
- dequeue(): remove item from q[head].
- Update head and tail modulo the capacity.
- Add resizing array.

![image](https://s1.ax1x.com/2018/10/20/iBS5Md.png)

**Q**. How to resize?

## 4. generics

### 4.1 Parameterized stack

**We implemented**: StackOfStrings.

**We also want**: StackOfURLs, StackOfInts, StackOfVans, ….

**Attempt 1**. Implement a separate stack class for each type.
- Rewriting code is tedious and error-prone.
- Maintaining cut-and-pasted code is tedious and error-prone.

**Attempt 2**. Implement a stack with items of type Object.
- Casting is required in client.
- Casting is error-prone: run-time error if types mismatch.

```java
StackOfObjects s = new StackOfObjects();
Apple a = new Apple();
Orange b = new Orange();
s.push(a);
s.push(b);
a = (Apple) (s.pop());  //run-time error
```

**Attempt 3**. Java generics.
- Avoid casting in client.
- Discover type mismatch errors at compile-time instead of run-time.

```java
Stack<Apple> s = new Stack<Apple>();    //type parameter
Apple a = new Apple();
Orange b = new Orange();
s.push(a);
s.push(b);  //compile-time error
a = s.pop();
```

**Guiding principles**. Welcome compile-time errors; avoid run-time errors.

### 4.2 Generic stack: linked-list implementation

```java
//generic type name
public class Stack<Item>
{
    private Node first = null;
    
    private class Node
    {
        Item item;
        Node next;
    }
    
    public boolean isEmpty()
    { return first == null; }
    
    public void push(Item item)
    {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
    }
    
    public Item pop()
    {
        Item item = first.item;
        first = first.next;
        return item;
    }
}
```

### 4.3 Generic stack: array implementation

```java
public class FixedCapacityStack<Item>
{
    private Item[] s;
    private int N = 0;
    
    public FixedCapacityStack(int capacity)
    {
        s = (Item[]) new Object[capacity];  //the ugly cast
        // s = new Item[capacity];
        // generic array creation not allowed in Java
    }
    
    public boolean isEmpty()
    { return N == 0; }
    
    public void push(Item item)
    { s[N++] = item; }
    
    public Item pop()
    { return s[--N]; }
}
```

### 4.4 Unchecked cast

![image](https://s1.ax1x.com/2018/10/20/iBpRwq.png)

### 4.5 Generic data types: autoboxing

**Q**. What to do about primitive types?

##### Wrapper type.
- Each primitive type has a wrapper object type.
- Ex: Integer is wrapper type for int.

**Autoboxing**. Automatic cast between a primitive type and its wrapper.

```java
Stack<Integer> s = new Stack<Integer>();
s.push(17); // s.push(Integer.valueOf(17));
int a = s.pop(); // int a = s.pop().intValue();
```

**Bottom line**. Client code can use generic stack for any type of data.

## 5. iterators

### 5.1 Iteration

**Design challenge**. Support iteration over stack items by client, without revealing the internal representation of the stack.

**Java solution**. Make stack implement the ```java.lang.Iterable``` interface.

### 5.2 Iterators

**Q**. What is an ```Iterable``` ?
**A**. Has a method that returns an ```Iterator```.

```java
//Iterable interface
public interface Iterable<Item>
{
    Iterator<Item> iterator();
}
```

**Q**. What is an ```Iterator``` ?
**A**. Has methods ```hasNext()``` and ```next()```.

```java
//Iterator interface
public interface Iterator<Item>
{
    boolean hasNext();
    Item next();
    //void remove();    //optional; use at your own risk
}
```

**Q**. Why make data structures ```Iterable``` ?
**A**. Java supports elegant client code.

```java
//“foreach” statement (shorthand)
for (String s : stack)
    StdOut.println(s);
```

```java
//equivalent code (longhand)
Iterator<String> i = stack.iterator();
while (i.hasNext())
{
    String s = i.next();
    StdOut.println(s);
}
```

### 5.3 Stack iterator: linked-list implementation

```java
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>
{
    ...
    
    public Iterator<Item> iterator() { return new ListIterator(); }
    
    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;
        
        public boolean hasNext() { return current != null; }
        
        //throw UnsupportedOperationException
        public void remove() { /* not supported */ }
        
        //throw NoSuchElementException if no more items in iteration
        public Item next()
        {
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
```

### 5.4 Stack iterator: array implementation

```java
import java.util.Iterator;

public class Stack<Item> implements Iterable<Item>
{
    …
    
    public Iterator<Item> iterator()
    { return new ReverseArrayIterator(); }
    
    private class ReverseArrayIterator implements Iterator<Item>
    {
        private int i = N;
        
        public boolean hasNext() { return i > 0; }
        public void remove() { /* not supported */ }
        public Item next() { return s[--i]; }
    }
}
```

### 5.5 Bag API

**Main application**. Adding items to a collection and iterating (when order doesn't matter).

public class Bag<Item> implements Iterable<Item> | /
---|---
Bag() | *create an empty bag*
void add(Item x) | *insert a new item onto bag*
int size() | *number of items in bag*
Iterable<Item> | *iterator() iterator for all items in bag*

**Implementation**. Stack (without pop) or queue (without dequeue).

## 6. applications

### 6.1 Java collections library

**List interface**. ```java.util.List``` is API for an sequence of items.

public interface List<Item> implements Iterable<Item> | /
---|---
List() | *create an empty list*
boolean isEmpty() | *is the list empty?*
int size() | *number of items*
void add(Item item) | *append item to the end*
Item get(int index) | *return item at given index*
Item remove(int index) | *return and delete item at given index*
boolean contains(Item item) | *does the list contain the given item?*
Iterator<Item> iterator() | *iterator over all items in the list*
... | 

**Implementations**. ```java.util.ArrayList``` uses resizing array; ```java.util.LinkedList``` uses linked list.

##### java.util.Stack.
- Supports push(), pop(), and and iteration.
- Extends ```java.util.Vector```, which implements ```java.util.List``` interface from previous slide, including, get() and remove().
- Bloated and poorly-designed API (why?)

**java.util.Queue**. An interface, not an implementation of a queue.

**Best practices**. Use our implementations of Stack, Queue, and Bag.

### 6.2 War story (from Assignment 1)

##### Generate random open sites in an N-by-N percolation system.
- Jenny: pick (i, j) at random; if already open, repeat. Takes `$\sim c_1 N^2$` seconds.
- Kenny: create a ```java.util.ArrayList``` of `$N^2$` closed sites. Pick an index at random and delete. Takes `$\sim c_2 N^4$` seconds.

**Lesson**. Don't use a library until you understand its API!

**This course**. Can't use a library until we've implemented it in class.

### 6.3 Stack applications

- Parsing in a compiler.
- Java virtual machine.
- Undo in a word processor.
- Back button in a Web browser.
- PostScript language for printers.
- Implementing function calls in a compiler.
- ...

### 6.4 Function calls

##### How a compiler implements a function.
- Function call: push local environment and return address.
- Return: pop return address and local environment.

**Recursive function**. Function that calls itself.

**Note**. Can always use an explicit stack to remove recursion.

### 6.5 Arithmetic expression evaluation

**Goal**. Evaluate infix expressions.
```math
(1+((2+3)*(4*5)))
```

##### Two-stack algorithm. [E. W. Dijkstra]
- Value: push onto the value stack.
- Operator: push onto the operator stack.
- Left parenthesis: ignore.
- Right parenthesis: pop operator and two values; push the result of applying that operator to those values onto the operand stack.

**Context**. An interpreter!

```java
public class Evaluate
{
    public static void main(String[] args)
    {
        Stack<String> ops = new Stack<String>();
        Stack<Double> vals = new Stack<Double>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if      (s.equals("(")) ;
            else if (s.equals("+")) ops.push(s);
            else if (s.equals("*")) ops.push(s);
            else if (s.equals(")"))
            {
                String op = ops.pop();
                if      (op.equals("+")) vals.push(vals.pop() + vals.pop());
                else if (op.equals("*")) vals.push(vals.pop() * vals.pop());
            }
            else vals.push(Double.parseDouble(s));
        }
        StdOut.println(vals.pop());
    }
}
```

### 6.6 Correctness

**Q**. Why correct?
**A**. When algorithm encounters an operator surrounded by two values within parentheses, it leaves the result on the value stack.
```math
( 1 + ( ( 2 + 3 ) * ( 4 * 5 ) ) )
```
as if the original input were:
```math
( 1 + ( 5 * ( 4 * 5 ) ) )
```
Repeating the argument:
```math
( 1 + ( 5 * 20 ) )

( 1 + 100 )

101
```
**Extensions**. More ops, precedence order, associativity.

### 6.7 Stack-based programming languages

**Observation 1**. Dijkstra's two-stack algorithm computes the same value if
the operator occurs after the two values.
```math
( 1 ( ( 2 3 + ) ( 4 5 * ) * ) + )
```

**Observation 2**. All of the parentheses are redundant!
```math
1 2 3 + 4 5 * * +
```

**Bottom line**. Postfix or "reverse Polish" notation.

**Applications**. Postscript, Forth, calculators, Java virtual machine, …