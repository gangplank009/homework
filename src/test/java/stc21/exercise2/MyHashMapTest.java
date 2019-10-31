package stc21.exercise2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class MyHashMapTest {

    private static MyHashMap map;
    private static int[] keySet = {10, 20, 30};
    private static int[] valueSet = {10, 20, 30};

    @Before
    public void initMap() {
        map = new MyHashMap();
    }

    @After
    public void clearMap() {
        for (int i = 0; i < keySet.length; i++) {
            map.remove(keySet[i]);
        }
    }

    @Test
    public void isEmpty() {
        assertTrue(map.isEmpty());
    }

    @Test
    public void getSize() {
        map.put(keySet[0], valueSet[0]);
        assertEquals(1, map.getSize());
        map.remove(keySet[0]);
        assertEquals(0, map.getSize());
    }

    @Test
    public void containsKey() {
        map.put(keySet[1], valueSet[1]);
        assertTrue(map.containsKey(keySet[1]));
    }

    @Test
    public void containsValue() {
        map.put(keySet[1], valueSet[1]);
        assertTrue(map.containsValue(valueSet[1]));
    }

    @Test
    public void put() {
        map.put(keySet[0], valueSet[0]);
        assertTrue(map.containsKey(keySet[0]));
        assertTrue(map.containsValue(valueSet[0]));
        assertEquals(1, map.getSize());
    }

    @Test
    public void get() {
        map.put(keySet[0], valueSet[0]);
        assertEquals(valueSet[0], map.get(keySet[0]));
    }

    @Test
    public void remove() {
        map.put(keySet[0], valueSet[0]);
        map.put(keySet[1], valueSet[1]);
        map.put(keySet[2], valueSet[2]);
        map.remove(keySet[0]);
        assertEquals(2, map.getSize());
        map.remove(keySet[1]);
        assertEquals(1, map.getSize());
        map.remove(keySet[2]);
        assertEquals(0, map.getSize());
    }
}