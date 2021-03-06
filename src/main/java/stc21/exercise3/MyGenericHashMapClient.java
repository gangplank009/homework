package stc21.exercise3;

import java.util.Map;
import java.util.Random;

public class MyGenericHashMapClient {

    private static MyGenericHashMap<String, String> myGenericHashMap, secondGenericHashMap;

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        myGenericHashMap = new MyGenericHashMap<>(String.class, String.class);
        myGenericHashMap.put(null, "NULL");
        String nullStr = myGenericHashMap.get(null);
        secondGenericHashMap = new MyGenericHashMap<>(String.class, String.class);
        fillHashMap(myGenericHashMap, 10, 20);
        fillHashMap(secondGenericHashMap, 20, 30);
        System.out.println(myGenericHashMap.toString());
        myGenericHashMap.putAll(secondGenericHashMap);
        System.out.println(myGenericHashMap.toString());
}

    private static void fillHashMap(Map map,int startIndex, int stopIndex) {
        for (int i = startIndex; i < stopIndex; i++) {
            String key = String.valueOf(i);
            String value = String.valueOf(i);
            map.put(key, value);
            System.out.println("Inserted in myHashMap: key=" + key + ", value=" + value);

        }
    }

    private static void removeMyHashMap() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            String key = String.valueOf(random.nextInt(64));
            String value = myGenericHashMap.remove(key);
            System.out.println("Remove from myHashMap: key=" + key + ", value=" + value);

        }
    }
}
