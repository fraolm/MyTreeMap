



public interface MyMap<K,V>
{
    //Return: The number of elements in this MyMap.
    int size();

    //Return: true if the MyMap contains no elements, false otherwise
    boolean isEmpty();

    //Post:	All elements removed from this MyMap.
    void clear();

    //Post  : The element specified by key removed if element exists
    //Return: the value component for an element specified by key if element exists, null if element
    //	      does not exist
    V remove(Object key);

    //Return: true if the MyMap contains an element with the specified key, false otherwise
    boolean containsKey(Object key);

    //Post  : the value component for an element specified by key replaced by newValue if element
    //	      exists, the element (key, newValue) pair added if element does not exist
    //Return: the value component for an element specified by key if element exists before the
    //	      update, null if element does not exist
    V put(K key, V newValue);

    //Return: the value component for an element specified by key if element exists, null if element does not exist
    V get(Object key);

    //Return: A MySet object of all the keys contained in this MyMap. The MySet is backed by the
    //	      MyMap, so changes to the MyMap are reflected in the MySet, and vice-versa
    MySet<K> keySet();

    //Return: A MySet object of all the MyMap.Entry elements contained in this MyMap.
    //        The MySet is backed by the MyMap, so changes to the MyMap are reflected in the MySet, and vice-versa
    MySet<MyMap.Entry<K, V>> entrySet();

    interface Entry<K, V>
    {
        //Return: The key of this entry.
        K getKey();

        //Return: The value of this entry.
        V getValue();

        //Post  : The value of this entry replaced by newValue.
        //Return: The old value of this entry.
        V setValue(V newValue);
    }
}

