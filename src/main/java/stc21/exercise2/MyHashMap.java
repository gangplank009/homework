package stc21.exercise2;

import java.util.Objects;

public class MyHashMap {

    private int size = 0;
    private int capacity = 16;
    private MyEntry[] buckets = new MyEntry[capacity];
    private double loadFactor = 0.75d;

    private static class MyEntry {
        private Object key;
        private Object value;
        private MyEntry next;

        public MyEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + this.key + "--" + this.value + " next:" + this.next + "]";
        }
    }
    public boolean isEmpty() {
        return this.size == 0;
    }

    public int getSize() {
        return this.size;
    }

    public boolean containsKey(Object key) {
        return getMatchingEntry(key) != null;
    }

    public boolean containsValue(Object value) {
        for (MyEntry entry : this.buckets) {
            while (entry != null && !matches(value, entry.value)) {
                entry = entry.next;
            }
            if (entry != null)
                return true;
        }
        return false;
    }

    public void put(Object key, Object value) {
        if (this.shouldResize()) {
            this.resize();
        }

        if (addEntry(new MyEntry(key, value), this.buckets))
            this.size++;
    }

    public Object get(Object key) {
        MyEntry matchingEntry = getMatchingEntry(key);
        return matchingEntry == null ? null : matchingEntry.value;
    }

    public void remove(Object key) {
        int index = indexOf(key);
        MyEntry currentEntry = this.buckets[index];

        while (currentEntry != null && currentEntry.next != null
                && !matches(key, currentEntry.next.key)) {
            currentEntry = currentEntry.next;
        }

        if (currentEntry != null) {
            if (matches(key, currentEntry.key)) {
                this.buckets[index] = null;
                System.out.println();
            } else if (currentEntry.next != null) {
                currentEntry.next = currentEntry.next.next;
            }
            this.size--;
        }
    }

    private boolean shouldResize() {
        return this.size > Math.ceil((double) this.capacity * this.loadFactor);
    }

    private void resize() {
        this.capacity = this.capacity << 1;

        MyEntry[] newBuckets = new MyEntry[this.capacity];
        for (MyEntry entry : this.buckets) {
            if (entry != null) {
                this.setEntry(entry, newBuckets);
            }
        }
        this.buckets = newBuckets;
    }

    private void setEntry(MyEntry entry, MyEntry[] buckets) {
        MyEntry nextEntry = entry.next;
        entry.next = null;

        this.addEntry(entry, buckets);

        if (nextEntry != null) {
            this.setEntry(nextEntry, buckets);
        }
    }

    private boolean addEntry(MyEntry entry, MyEntry[] buckets) {
        // индекс корзины, рассчитанный на основе хеша по ключу
        int index = indexOf(entry.key);
        // получение первого элемента корзины
        MyEntry existingEntry = buckets[index];

        // при отсутсвии существующего элемента добавляемый становится первым в корзине
        if (existingEntry == null) {
            buckets[index] = entry;
            System.out.println("\tFirst item at bucket:" + index);
            return true;
        } else {
            // перебор списка элементов корзины
            while (!this.matches(entry.key, existingEntry.key) && existingEntry.next != null) {
                existingEntry = existingEntry.next;
            }

            // при нахождении элемента с ключем равным добавляемому обновляем значение элемента
            if (this.matches(entry.key, existingEntry.key)) {
                existingEntry.value = entry.value;
                System.out.println("\tUpdate item at bucket:" + index);
                return false;
            }

            // ни один ключ не совпал - добавляем элемент в конец списка корзины
            existingEntry.next = entry;
            System.out.println("\tAdd item at bucket:" + index);
            return true;
        }
    }

    private MyEntry getMatchingEntry(Object key) {
        MyEntry existingEntry = this.buckets[indexOf(key)];

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
}
