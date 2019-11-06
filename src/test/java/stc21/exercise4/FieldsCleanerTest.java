package stc21.exercise4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class FieldsCleanerTest {

    private static Set<String> fieldsToCleanUp;
    private static Set<String> fieldsToOutput;
    private static FieldsCleaner fieldsCleaner;

    private Map<String, String> hashMap;
    private TestObject testObject;

    public static class TestObject {

        public static final String BOOL_FIELD_NAME = "boolField";
        public static final String CHAR_FIELD_NAME = "charField";
        public static final String BYTE_FIELD_NAME = "byteField";
        public static final String SHORT_FIELD_NAME = "shortField";
        public static final String INT_FIELD_NAME = "intField";
        public static final String LONG_FIELD_NAME = "longField";
        public static final String FLOAT_FIELD_NAME = "floatField";
        public static final String DOUBLE_FIELD_NAME = "doubleField";
        public static final String OBJECT_FIELD_NAME = "objectField";
        public static final String STRING_FIELD_NAME = "strField";

        public boolean boolField;
        protected char charField;
        byte byteField;
        private short shortField;
        private int intField;
        private long longField;
        private float floatField;
        private double doubleField;
        private Object objectField;
        private String strField;

        public TestObject setBoolField(boolean boolField) {
            this.boolField = boolField;
            return this;
        }

        public TestObject setCharField(char charField) {
            this.charField = charField;
            return this;
        }

        public TestObject setByteField(byte byteField) {
            this.byteField = byteField;
            return this;
        }

        public TestObject setShortField(short shortField) {
            this.shortField = shortField;
            return this;
        }

        public TestObject setIntField(int intField) {
            this.intField = intField;
            return this;
        }

        public TestObject setLongField(long longField) {
            this.longField = longField;
            return this;
        }

        public TestObject setFloatField(float floatField) {
            this.floatField = floatField;
            return this;
        }

        public TestObject setDoubleField(double doubleField) {
            this.doubleField = doubleField;
            return this;
        }

        public TestObject setObjectField(Object objectField) {
            this.objectField = objectField;
            return this;
        }

        public TestObject setStringField(String strField) {
            this.strField = strField;
            return this;
        }
    }

    @BeforeClass
    public static void initInputSets() {
        fieldsCleaner = new FieldsCleaner();
    }

    @Before
    public void initTestObjects() {
        testObject = new TestObject()
                .setBoolField(true)
                .setCharField('a')
                .setByteField((byte) 100)
                .setShortField((short) 1000)
                .setIntField(100_000)
                .setLongField(100_000_000L)
                .setFloatField(250.987F)
                .setDoubleField(250_000.123456789D)
                .setObjectField(new Object())
                .setStringField("Hello");

        hashMap = new HashMap<>();
        hashMap.put(TestObject.BOOL_FIELD_NAME, "bool");
        hashMap.put(TestObject.CHAR_FIELD_NAME, "char");
        hashMap.put(TestObject.BYTE_FIELD_NAME, "byte");
        hashMap.put(TestObject.SHORT_FIELD_NAME, "short");
        hashMap.put(TestObject.INT_FIELD_NAME, "int");
        hashMap.put(TestObject.LONG_FIELD_NAME, "long");
        hashMap.put(TestObject.FLOAT_FIELD_NAME, "float");
        hashMap.put(TestObject.DOUBLE_FIELD_NAME, "double");
        hashMap.put(TestObject.OBJECT_FIELD_NAME, "obj");
        hashMap.put(TestObject.STRING_FIELD_NAME, "str");

        fieldsToCleanUp = new LinkedHashSet<>();
        fieldsToCleanUp.add(TestObject.BOOL_FIELD_NAME);
        fieldsToCleanUp.add(TestObject.LONG_FIELD_NAME);
        fieldsToCleanUp.add(TestObject.OBJECT_FIELD_NAME);

        fieldsToOutput = new LinkedHashSet<>();
        fieldsToOutput.add(TestObject.BOOL_FIELD_NAME);
        fieldsToOutput.add(TestObject.CHAR_FIELD_NAME);
        fieldsToOutput.add(TestObject.BYTE_FIELD_NAME);
        fieldsToOutput.add(TestObject.SHORT_FIELD_NAME);
        fieldsToOutput.add(TestObject.INT_FIELD_NAME);
        fieldsToOutput.add(TestObject.LONG_FIELD_NAME);
        fieldsToOutput.add(TestObject.FLOAT_FIELD_NAME);
        fieldsToOutput.add(TestObject.DOUBLE_FIELD_NAME);
        fieldsToOutput.add(TestObject.OBJECT_FIELD_NAME);
        fieldsToOutput.add(TestObject.STRING_FIELD_NAME);
    }

    @Test
    public void cleanUpObject() {
        fieldsCleaner.cleanUp(testObject, fieldsToCleanUp, fieldsToOutput);

        assertFalse(testObject.boolField);
        assertNotEquals(testObject.charField, '\u0000');
        assertNotEquals(testObject.byteField, (byte)0);
        assertNotEquals(testObject.shortField, (short)0);
        assertNotEquals(testObject.intField, 0);
        assertEquals(testObject.longField, 0L);
        assertNotEquals(testObject.floatField, 0.0F);
        assertNotEquals(testObject.doubleField, 0.0D);
        assertNull(testObject.objectField);
        assertNotEquals(testObject.strField, 0);
    }

    @Test
    public void cleanUpMap() {
        int hashMapSize = hashMap.size();
        int hashMapSizeCleaned = hashMapSize - 3;
        for (String key : fieldsToCleanUp) {
            assertNotNull(hashMap.get(key));
        }
        fieldsCleaner.cleanUp(hashMap, fieldsToCleanUp, fieldsToOutput);
        assertEquals(hashMapSizeCleaned, hashMap.size());
        for (String key : fieldsToCleanUp) {
            assertNull(hashMap.get(key));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void cleanUpObjectException() {
        fieldsToCleanUp.add("nonexistentField");
        fieldsCleaner.cleanUp(testObject, fieldsToCleanUp, fieldsToOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cleanUpMapException() {
        fieldsToCleanUp.add("nonexistentKey");
        fieldsCleaner.cleanUp(hashMap, fieldsToCleanUp, fieldsToOutput);
    }
}