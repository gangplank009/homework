package stc21.exercise2;

import java.util.Random;

public class MyHashMapClient {

    private static MyHashMap myHashMap = new MyHashMap();

    public static void main(String[] args) {
        testMyHashMap();
        testPutOneHundredEntries();
    }

    private static void testMyHashMap() {
        String strKey = "strKey";
        String strValue = "strValue";

        Integer intKey = 100;
        Integer intValue = 1;

        Boolean boolKey = Boolean.FALSE;
        Boolean boolValue = Boolean.FALSE;

        Object objKey = new Object();
        Object objValue = new Object();

        System.out.println("Is empty? " + myHashMap.isEmpty());

        myHashMap.put(strKey, strValue);
        myHashMap.put(intKey, intValue);
        myHashMap.put(boolKey, boolValue);
        myHashMap.put(objKey, objValue);

        System.out.println("Put 4 different key-value pairs");
        System.out.println("Is empty? " + myHashMap.isEmpty());
        System.out.println("Map size = " + myHashMap.getSize());

        System.out.println("Contains key=" + strKey + myHashMap.containsKey(strKey));
        System.out.println("Contains key=" + intKey + "? " + myHashMap.containsKey(intKey));
        System.out.println("Contains key=" + boolKey + "? " + myHashMap.containsKey(boolKey));
        System.out.println("Contains key=" + objKey + "? " + myHashMap.containsKey(objKey));

        System.out.println("Contains value=" + "\"" + strValue + "\"? " + myHashMap.containsValue(strValue));
        System.out.println("Contains value=" + intValue + "? " + myHashMap.containsValue(intValue));
        System.out.println("Contains value=" + boolValue + "? " + myHashMap.containsValue(boolValue));
        System.out.println("Contains value=" + objValue + "? " + myHashMap.containsValue(objValue));

        myHashMap.remove(strKey);
        myHashMap.remove(boolKey);

        System.out.println("Remove " + strKey + ", " + boolKey);
        System.out.println("Is empty? " + myHashMap.isEmpty());
        System.out.println("Map size = " + myHashMap.getSize());

        System.out.println("Get key=" + strKey + " value=" + myHashMap.get(strKey));
        System.out.println("Get key=" + intKey + " value=" + myHashMap.get(intKey));
        System.out.println("Get key=" + boolKey + " value=" + myHashMap.get(boolKey));
        System.out.println("Get key=" + objKey + " value=" + myHashMap.get(objKey));

        myHashMap.put(intKey, 2);
        myHashMap.put(objKey, new Object());

        System.out.println("Get key=" + strKey + " value=" + myHashMap.get(strKey));
        System.out.println("Get key=" + intKey + " value=" + myHashMap.get(intKey));
        System.out.println("Get key=" + boolKey + " value=" + myHashMap.get(boolKey));
        System.out.println("Get key=" + objKey + " value=" + myHashMap.get(objKey));

        myHashMap.put(null, "NULL KEY");
        System.out.println("Get key=" + null + " value=" + myHashMap.get(null));
        System.out.println("Contains key=" + null + " " + myHashMap.containsKey(null));
        myHashMap.put(null, null);
        System.out.println("Get key=" + null + " value=" + myHashMap.get(null));
        System.out.println("Contains key=" + null + " " + myHashMap.containsKey(null));
        System.out.println();
    }

    private static void testPutOneHundredEntries() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            myHashMap.put(random.nextInt(16), i);
        }
        System.out.println(myHashMap.toString());
    }
}
