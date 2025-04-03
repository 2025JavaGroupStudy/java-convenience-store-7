package store.utility;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setFirst(K key){this.key = key;}
    public void setSecond(V value){this.value = value;}
    public K getFirst() { return key; }
    public V getSecond() { return value; }

    public boolean isEmpty(){
        return key==null&&value==null;
    }

    @Override
    public String toString() {
        return key + " - " + value;
    }
}