
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
public class MyTreeMap<K,V> implements MySortedMap<K,V>, java.io.Serializable
Supports a set of elements in the form of (key, value) pairs.    

Usage:	public MyTreeMap();
*/
public class MyTreeMap<K, V> implements MySortedMap<K, V>, java.io.Serializable
{
    private class Entry<K, V> implements MyMap.Entry<K, V>	//Defines node structure in a Map similar to BSTNode
    {
        private K key;
        private V value;
        private Entry<K, V> left, right, parent;

        public Entry(K key, V value, Entry<K, V> parent)
        {
            this.key = key;
            this.value = value;
            this.parent = parent;
            left = null;
            right = null;
        }

        public K getKey()
        {
            return key;
        }

        public V getValue()
        {
            return value;
        }

        public V setValue(V value)
        {
            V oldValue = this.value;
            this.value = value;

            return oldValue;
        }

        public String toString()
        {
            return key + "=" + value;
        }
    }



    private Entry<K, V> root;			            //similar to data structure in BSTree
    private int mapSize;

    //Return: A reference to the node containing key or null if the search fails
    private Entry<K, V> findNode(K key)
    {
        Entry<K, V> t = root;

        while(t != null)
        {
            int result = ((Comparable)key).compareTo(t.getKey());

            if (result == 0)
                return t;
            else if (result < 0)
                t = t.left;
            else t = t.right;
        }

        return null;
    }

    private void removeNode(Entry<K, V> D) 	        //similar to removeNode in BSTree except the highlighted
    {
        Entry<K, V> pOfD = D.parent, R, pOfR;

        if ((D.left == null) && (D.right == null))
            R = null;
        else if ((D.left == null) && (D.right != null))
        {
            R = D.right;
            R.parent = pOfD;
        }
        else if ((D.left != null) && (D.right == null))
        {
            R = D.left;
            R.parent = pOfD;
        }
        else if (D.right.left == null)
        {
            R = D.right;
            R.left = D.left;
            R.parent = pOfD;
            D.left.parent = R;
        }
        else
        {
            pOfR = D;
            R = D.right;

            while(R.left != null)
            {
                pOfR = R;
                R = R.left;
            }

            pOfR.left = R.right;
            if (R.right != null)
                R.right.parent = pOfR;

            R.left = D.left;
            R.right = D.right;
            R.parent = pOfD;
            R.left.parent = R;
            R.right.parent = R;
        }

        if (pOfD == null)
            root = R;
        else if (((Comparable)D.getKey()).compareTo(pOfD.getKey()) < 0)
            pOfD.left = R;
        else pOfD.right = R;
    }

    //Post: MyTreeMap initialized to empty.
    public MyTreeMap()					            //similar to BSTree
    {
        root = null;
        mapSize = 0;
    }

    public int size()					            //similar to BSTree
    {
        return mapSize;
    }

    public boolean isEmpty()			            //similar to BSTree
    {
        return mapSize == 0;
    }

    public void clear()					            //similar to BSTree
    {
        mapSize = 0;
        root = null;
    }

    public boolean containsKey(Object key)			//similar to BSTree
    {
        Entry<K, V> t = findNode((K)key);

        return (t == null) ? false : true;
    }

    public V get(Object key)
    {
        Entry<K, V> t = findNode((K)key);

        if (t != null)
            return t.value;
        else
            return null;
    }

    public V put(K key, V value)				    //similar to add in BSTree
    {
        Entry<K, V> t = root, parent = null, newNode;
        int result = 0;

        while(t != null)
        {
            parent = t;
            result = ((Comparable<K>)key).compareTo(t.getKey());

            if (result == 0)
            {
                V temp = t.value;
                t.value = value;
                return temp;
            }
            else if (result < 0)
                t = t.left;
            else
                t = t.right;
        }

        newNode = new Entry<K, V>(key, value, parent);

        if (parent == null)
            root = newNode;
        else if (result < 0)
            parent.left = newNode;
        else
            parent.right = newNode;

        mapSize++;

        return null;
    }

    public V remove(Object key)				        //similar to BSTree
    {
        Entry<K, V> t  = findNode((K)key);

        if (t == null)
            return null;

        V temp = t.value;
        removeNode(t);

        mapSize --;

        return temp;
    }

    public K firstKey()
    {
        Entry<K, V> t = root;

        if (t == null)
            return null;

        while (t.left != null)	//the smallest element is the leftmost node 
            t = t.left;

        return t.getKey();
    }

    public K lastKey()
    {
        Entry<K, V> t = root;

        if (t == null)
            return null;

        while (t.right != null)	//the largest element is the rightmost node
            t = t.right;

        return t.getKey();
    }

    //Return: A string representation of this Map, which is a comma separated list of elements in the form key=value 
    //        in ascending order of their keys enclosed in curly braces	.
//    public String toString()
//    {
//        String s = "[";
//        Iterator<MyMap.Entry<K, V>> iter = entrySet().iterator();
//
//        while (iter.hasNext())
//        {
//            s += iter.next();
//            if (iter.hasNext())
//                s += ", ";
//        }
//
//        s += "]";
//
//        return s;
//    }

    private MySet<K> ks = null;

    public MySet<K> keySet()
    {
        if (ks == null)
        {
            ks = new MySet<K>()
            {
                public Iterator<K> iterator()
                {
                    return new KeyIterator();
                }

                public int size()
                {
                    return MyTreeMap.this.size();
                }

                public void clear()
                {
                    MyTreeMap.this.clear();
                }

                public boolean isEmpty()
                {
                    return MyTreeMap.this.isEmpty();
                }

                public boolean contains(Object key)
                {
                    return containsKey(key);
                }

                public boolean remove(Object key)
                {
                    int oldSize = mapSize;

                    MyTreeMap.this.remove(key);	//returns V

                    return mapSize != oldSize;
                }

                public Object[] toArray()
                {
                    Object[] arr = new Object[size()];

                    Iterator<K> iter = iterator();
                    for (int i = 0; i < arr.length; i++)
                        arr[i] = iter.next();

                    return arr;
                }

                public String toString()
                {
                    String s = "[";

                    Iterator<K> iter = iterator();
                    while (iter.hasNext())
                    {
                        s += iter.next();
                        if (iter.hasNext())	s += ", ";
                    }

                    s += "]";

                    return s;
                }

                public boolean add(K key)
                {
                    throw new UnsupportedOperationException();
                }
            };
        }
        return ks;
    }

    protected class KeyIterator implements Iterator<K>
    {
        private Entry<K, V> lastReturned = null;
        private Entry<K, V> nextNode = null;

        //Post: nextNode is the leftmost node	
        public KeyIterator()
        {
            nextNode = root;			        //same as nextNode = MyTreeMap.this.root;
            if (nextNode != null)
                while (nextNode.left != null)
                    nextNode = nextNode.left;   //next node is leftmost node
        }

        public boolean hasNext()
        {
            return nextNode != null;
        }

        public K next()
        {
            if (nextNode == null)
                throw new NoSuchElementException("No more elements");

            lastReturned = nextNode;

            Entry<K, V> p;

            if (nextNode.right != null)		//next node is the leftmost in its right subtree
            {
                nextNode = nextNode.right;

                while (nextNode.left != null)
                    nextNode = nextNode.left;
            }
            else				            //next node is its closest left ancestor
            {
                p = nextNode.parent;

                while (p != null && nextNode == p.right)	//short circuit &&
                {
                    nextNode = p;
                    p = p.parent;
                }
                nextNode = p;
            }
            return lastReturned.getKey();	//line 101
        }

        public void remove()
        {
            if (lastReturned == null)
                throw new IllegalStateException("Iterator call to next() required before calling remove()");

            removeNode(lastReturned);
            lastReturned = null;

            mapSize--;
        }
    }



    private MySet<MyMap.Entry<K, V>> es = null;

    public MySet<MyMap.Entry<K, V>> entrySet()
    {
        if (es == null)
        {
            es = new MySet<MyMap.Entry<K, V>>()
            {
                public Iterator<MyMap.Entry<K, V>> iterator()
                {
                    return new ElementIterator();
                }

                public int size()
                {
                    return MyTreeMap.this.size();
                }

                public void clear()
                {
                    MyTreeMap.this.clear();
                }

                public boolean isEmpty()
                {
                    return MyTreeMap.this.isEmpty();
                }

                public boolean contains(Object item)
                {
                    throw new UnsupportedOperationException();
                }

                public boolean remove(Object item)
                {
                    throw new UnsupportedOperationException();
                }

                public Object[] toArray()
                {
                    Object[] arr = new Object[size()];

                    Iterator<MyMap.Entry<K, V>> iter = iterator();

                    for (int i = 0; i < arr.length; i++)
                        arr[i] = iter.next();

                    return arr;
                }

                public String toString()
                {
                    return MyTreeMap.this.toString();
                }

                public boolean add(MyMap.Entry<K, V> obj)
                {
                    throw new UnsupportedOperationException();
                }
            };
        }
        return es;
    }

    protected class ElementIterator implements Iterator<MyMap.Entry<K,V>>
    {
        private Entry<K,V> lastReturned = null;	//MyTreeMap.Entry<K,V>
        private Entry<K,V> nextNode = null;

        //Post: nextNode is the leftmost node
        public ElementIterator ()
        {
            nextNode = root;

            if (nextNode != null)
                while (nextNode.left != null)
                    nextNode = nextNode.left;
        }

        public boolean hasNext()
        {
            return nextNode != null;
        }

        public MyMap.Entry<K, V> next()
        {
            if (nextNode == null)
                throw new NoSuchElementException("No more elements");

            lastReturned = nextNode;

            Entry<K, V> p;			        //MyTreeMap.Entry<K,V>

            if (nextNode.right != null)		//next node is the leftmost in its right subtree
            {
                nextNode = nextNode.right;
                while (nextNode.left != null)
                    nextNode = nextNode.left;
            }
            else				            //next node is its closest left ancestor
            {
                p = nextNode.parent;

                while (p != null && nextNode == p.right)	//short circuit &&
                {
                    nextNode = p;
                    p = p.parent;
                }

                nextNode = p;
            }
            return lastReturned;
        }

        public void remove()
        {
            if (lastReturned == null)
                throw new IllegalStateException("Iterator call to next() required before calling remove()");

            removeNode(lastReturned);
            lastReturned = null;
            mapSize--;
        }
    }
}
