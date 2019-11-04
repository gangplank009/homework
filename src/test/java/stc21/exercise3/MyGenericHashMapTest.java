package stc21.exercise3;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import stc21.exercise3.MyGenericHashMap;

import java.util.*;

import static org.junit.Assert.*;

public class MyGenericHashMapTest {

    private static Map<String, String> myGenericHashMap;
    private static Map<String, String> javaMap;
    private static List<TestEntry> listTestEntries = new ArrayList<>();
    private String testKey = "key";
    private String testValue = "value";

    private static class TestEntry {

        private TestEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private String value;
    }

    @BeforeClass
    public static void initEnv() {
        Random random = new Random();
        myGenericHashMap = new MyGenericHashMap<>(String.class, String.class);
        javaMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            listTestEntries.add(new TestEntry(String.valueOf(random.nextInt()),
                    String.valueOf(random.nextInt())));
        }
    }

    @Before
    public void setupMaps() {

    }

    @After
    public void clearMaps() {
        myGenericHashMap.clear();
        javaMap.clear();
    }

    private static void fillMaps() {
        for (TestEntry testEntry : listTestEntries) {
            myGenericHashMap.put(testEntry.key, testEntry.value);
            javaMap.put(testEntry.key, testEntry.value);
        }
    }

    @Test
    public void isEmpty() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertEquals(myGenericHashMap.isEmpty(), javaMap.isEmpty());
    }

    @Test
    public void size() {
        fillMaps();
        assertEquals(myGenericHashMap.size(), javaMap.size());
    }

    @Test
    public void containsKey() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertTrue(myGenericHashMap.containsKey(testKey));
        assertTrue(javaMap.containsKey(testKey));
    }

    // тест MyGenericHashMap на недопустимый тип ключа
    @Test(expected = ClassCastException.class)
    public void containsKeyException() {
        myGenericHashMap.put(testKey, testValue);
        myGenericHashMap.containsKey(20);
    }

    @Test
    public void containsValue() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertTrue(myGenericHashMap.containsValue(testValue));
        assertTrue(javaMap.containsValue(testValue));
    }

    // тест MyGenericHashMap на недопустимый тип значения
    @Test(expected = ClassCastException.class)
    public void containsValueException() {
        myGenericHashMap.put(testKey, testValue);
        myGenericHashMap.containsValue(20);
    }

    @Test
    public void get() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertEquals(myGenericHashMap.get(testKey), javaMap.get(testKey));
    }

    // тест MyGenericHashMap на недопустимый тип ключа при получении значения
    @Test(expected = ClassCastException.class)
    public void getException() {
        myGenericHashMap.put(testKey, testValue);
        myGenericHashMap.get(new Object());
    }

    @Test
    public void put() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertEquals(myGenericHashMap.get(testKey), javaMap.get(testKey));
        assertEquals(myGenericHashMap.size(), javaMap.size());
    }

    @Test
    public void clear() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertEquals(myGenericHashMap.isEmpty(), javaMap.isEmpty());
        myGenericHashMap.clear();
        javaMap.clear();
        assertEquals(myGenericHashMap.isEmpty(), javaMap.isEmpty());
    }

    @Test
    public void putAll() {
        Map<String, String> inputMap = new HashMap<String, String>() {{
            put("key1", "value1");
            put("key2", "value2");
            put("key3", "value3");
        }};
        fillMaps();
        myGenericHashMap.putAll(inputMap);
        javaMap.putAll(inputMap);
        assertEquals(myGenericHashMap.size(), javaMap.size());
    }

    @Test
    public void keySet() {
        fillMaps();
        Set<String> myKeySet = myGenericHashMap.keySet();
        Set<String> javaKeySet = javaMap.keySet();
        assertEquals(myKeySet.size(), javaKeySet.size());
        assertEquals(myKeySet, javaKeySet);
    }

    @Test
    public void values() {
        fillMaps();
        Collection<String> myValues = myGenericHashMap.values();
        Collection<String> javaValues = javaMap.values();
        assertEquals(myValues.size(), javaValues.size());
        String[] myValuesArr = myValues.toArray(new String[]{});
        for (String s : myValuesArr) {
            assertTrue(javaValues.contains(s));
        }
    }

    @Test
    public void entrySet() {
        fillMaps();
        Set<Map.Entry<String, String>> myEntrySet = myGenericHashMap.entrySet();
        Set<Map.Entry<String, String>> javaEntrySet = javaMap.entrySet();
        assertEquals(myEntrySet.size(), javaEntrySet.size());
        Iterator myIterator = myEntrySet.iterator();
        Iterator javaIterator = javaEntrySet.iterator();
        while (myIterator.hasNext() && javaIterator.hasNext()) {
            Map.Entry<String, String> myEntry = (Map.Entry<String, String>) myIterator.next();
            Map.Entry<String, String> javaEntry = (Map.Entry<String, String>) javaIterator.next();
            assertEquals(myEntry.getKey(), javaEntry.getKey());
            assertEquals(myEntry.getValue(), javaEntry.getValue());
        }
    }

    @Test
    public void remove() {
        myGenericHashMap.put(testKey, testValue);
        javaMap.put(testKey, testValue);
        assertEquals(1, myGenericHashMap.size());
        assertEquals(1, javaMap.size());
        myGenericHashMap.remove(testKey);
        javaMap.remove(testKey);
        assertEquals(0, myGenericHashMap.size());
        assertEquals(0, javaMap.size());
    }
}