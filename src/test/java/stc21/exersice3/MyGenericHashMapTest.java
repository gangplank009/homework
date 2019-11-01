package stc21.exersice3;

import org.junit.Test;
import stc21.exercise3.MyGenericHashMap;

import static org.junit.Assert.*;

public class MyGenericHashMapTest {

    private MyGenericHashMap<String, String> map = new MyGenericHashMap<>();
    private String testKey = "key";
    private String testValue = "value";

    @Test
    public void isEmpty() {
        boolean beforePut = map.isEmpty();

        map.put(testKey, testValue);
        map.remove(testKey);

        boolean afterRemove = map.isEmpty();
        assertEquals(beforePut, afterRemove);
    }

    @Test
    public void getSize() {
        map.put(testKey, testValue);
        assertEquals(map.size(), 1);
    }

    @Test
    public void containsKey() {
        map.put(testKey, testValue);
        assertTrue(map.containsKey(testKey));
    }

    @Test
    public void containsValue() {
    }

    @Test
    public void get() {
    }

    @Test
    public void put() {
    }

    @Test
    public void remove() {
    }
}