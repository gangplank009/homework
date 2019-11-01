package stc21.exercise3;

import java.util.*;

public class MyGenericHashMap<K, V> implements Map<K, V> {

    private HashMap<Integer, Integer> map;
    private static class MyEntry<K, V> implements Map.Entry<K, V>{
        private final K key;
        private V value;
        private MyEntry<K, V> next;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + this.key + "--" + this.value + " next:" + this.next + "]";
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private int size = 0;
    private int capacity = 16;
    private MyEntry<K, V>[] buckets = new MyEntry[capacity];
    private double loadFactor = 0.75d;

    @Override
    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getMatchingEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (MyEntry<K, V> entry : this.buckets) {
            while (entry != null && !matches(value, entry.value)) {
                entry = entry.next;
            }
            if (entry != null)
                return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        MyEntry<K, V> matchingEntry = getMatchingEntry(key);
        return matchingEntry == null ? null : matchingEntry.value;
    }

    @Override
    public V put(K key, V value) {
        if (this.shouldResize()) {
            this.resize();
        }

        V oldValue = addEntry(new MyEntry<K, V>(key, value), this.buckets);
        if (oldValue == null)
            this.size++;
        return oldValue;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.capacity = 16;
        this.buckets = new MyEntry[this.capacity];
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Set<? extends Entry<? extends K, ? extends V>> entrySetToPut = m.entrySet();
        for (Entry<? extends K, ? extends V> entry : entrySetToPut) {
            Entry<K, V> entryToPut = (Entry<K, V>) entry;
            if (this.shouldResize()) {
                this.resize();
            }
            V oldValue = addEntry(new MyEntry<K, V>(entryToPut.getKey(), entryToPut.getValue()), this.buckets);
            if (oldValue == null)
                this.size++;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new LinkedHashSet<>();
        for (MyEntry<K, V> bucket : this.buckets) {
            MyEntry<K, V> current = bucket;
            if (current != null) {
                keySet.add(current.key);
                while (current.next != null) {
                    current = current.next;
                    keySet.add(current.key);
                }
            }
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        for (MyEntry<K, V> bucket : this.buckets) {
            MyEntry<K, V> current = bucket;
            if (current != null) {
                values.add(current.value);
                while (current.next != null) {
                    current = current.next;
                    values.add(current.value);
                }
            }
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new LinkedHashSet<>();
        for (MyEntry<K, V> bucket : this.buckets) {
            MyEntry<K, V> current = bucket;
            if (current != null) {
                entrySet.add(new MyEntry<>(current.key, current.value));
                while (current.next != null) {
                    current = current.next;
                    entrySet.add(new MyEntry<>(current.key, current.value));
                }
            }
        }
        return entrySet;
    }

    @Override
    public V remove(Object key) {
        int index = indexOf(key);
        MyEntry<K, V> currentEntry = this.buckets[index];
        MyEntry<K, V> prevEntry = null;

        while (currentEntry != null && currentEntry.next != null && !matches(key, currentEntry.key)) {
            prevEntry = currentEntry;
            currentEntry = currentEntry.next;
        }

        if (currentEntry != null) {
            if (matches(key, currentEntry.key)) {
                // предыдущего нет, следующих после текущего нет - в корзине 1 вхождение
                if (prevEntry == null && currentEntry.next == null) {
                    this.buckets[index] = null;
                }
                // предыдущего нет, следующие после текущего есть
                else if (prevEntry == null) {
                    this.buckets[index] = currentEntry.next;
                }
                // совпавший элемент в середине списка
                else if (currentEntry.next != null){
                    prevEntry.next = currentEntry.next;
                }
                // совпавший элемент - конец списка
                else {
                    prevEntry.next = null;
                }
                this.size--;
                return currentEntry.value;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        System.out.println();
        StringBuilder sb = new StringBuilder();
        for (MyEntry bucket : buckets) {
            if (bucket != null) {
                sb.append("[")
                        .append(bucket.key)
                        .append("--")
                        .append(bucket.value)
                        .append(" next:")
                        .append(bucket.next)
                        .append("]")
                        .append("\n");
            }
        }
        return sb.toString();
    }



    private boolean shouldResize() {
        return this.size > Math.ceil((double) this.capacity * this.loadFactor);
    }

    private void resize() {
        this.capacity = this.capacity * 2;

        MyEntry<K, V>[] newBuckets = new MyEntry[this.capacity];
        for (MyEntry<K, V> entry : this.buckets) {
            if (entry != null) {
                this.setEntry(entry, newBuckets);
            }
        }

        this.buckets = newBuckets;
    }

    private void setEntry(MyEntry<K, V> entry, MyEntry<K, V>[] buckets) {
        MyEntry<K, V> nextEntry = entry.next;
        entry.next = null;

        this.addEntry(entry, buckets);

        if (nextEntry != null) {
            this.setEntry(nextEntry, buckets);
        }
    }

    // внутренний метод для определения места добавления нового элемента
    private V addEntry(MyEntry<K, V> entry, MyEntry<K, V>[] buckets) {
        // индекс корзины, рассчитанный на основе хеша по ключу
        int index = indexOf(entry.key);
        // получение первого элемента корзины
        MyEntry<K, V> existingEntry = buckets[index];

        // при отсутсвии существующего элемента добавляемый становится первым в корзине
        if (existingEntry == null) {
            buckets[index] = entry;
        //    System.out.println("\tFirst item at bucket:" + index);
            return null;
        } else {
            // перебор списка элементов корзины
            while (!this.matches(entry.key, existingEntry.key) && existingEntry.next != null) {
                existingEntry = existingEntry.next;
            }

            // при нахождении элемента с ключем равным добавляемому обновляем значение элемента
            if (this.matches(entry.key, existingEntry.key)) {
                V oldValue = existingEntry.value;
                existingEntry.value = entry.value;
        //        System.out.println("\tUpdate item at bucket:" + index);
                return oldValue;
            }

            // ни один ключ не совпал - добавляем элемент в конец списка корзины
            existingEntry.next = entry;
        //    System.out.println("\tAdd item at bucket:" + index);
            return null;

        }
    }

    private MyEntry<K, V> getMatchingEntry(Object key) {
        MyEntry<K, V> existingEntry = this.buckets[indexOf(key)];

        while (existingEntry != null && !matches(key, existingEntry.key)) {
            existingEntry = existingEntry.next;
        }
        return existingEntry;
    }

    private int indexOf(Object key) {
        if (key == null)
            return 0;
        else {
            int index = hash(key) & (this.capacity - 1);
            return index;
        }
    }

    private boolean matches(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    private static int hash(Object key) {
        int hashCode;
        hashCode = key.hashCode();
        hashCode = hashCode ^ (hashCode >>> 16);
        if (key == null)
            return 0;
        else
            return hashCode;
    }
}
