package stc21.exercise4;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Pavel Efimov
 *
 * Класс FieldsCleaner имеет единственный открытый метод cleanUp().
 * Данный метод может с помощью Refcletion API устанавливать поля объекта в значения по умолчанию и
 * ссылочные типы в null, имена которых указанны в Set<String> fieldsToCleanUp. Выводить
 * в консоль значения всех полей, чьи имена указаны в Set<String> fieldsToOutput.
 * При передаче в метод объект, реализующий Map, будут затираться пары ключ-значение, ключ которых
 * входит в Set<String> fieldsToCleanUp, и выводиться все пары, чьи ключи входят в Set<String> fieldsToOutput.
 *
 * Если в Set<String> fieldsToCleanUp присутствуют имена полей/ключи, которые не присутствуют в объекте/мапе,
 * то выбрасывается
 * @throws IllegalArgumentException
 * */

public class FieldsCleaner {

    public void cleanUp(Object object, Set<String> fieldsToCleanUp, Set<String> fieldsToOutput) throws IllegalArgumentException {
        if (object instanceof Map) {
            try {
                processMap(object, fieldsToCleanUp, fieldsToOutput);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            processNotMap(object, fieldsToCleanUp, fieldsToOutput);
        }
    }

    // метод для обратоки объекта, реализующего интерфейс Map через Reflection API
    private void processMap(Object map, Set<String> fieldsToCleanUp, Set<String> fieldsToOutput) throws InvocationTargetException,
            IllegalAccessException, NoSuchMethodException, IllegalArgumentException {
        Class clazz = map.getClass();
        Method removeMethod;
        Method getMethod;
        Method keySetMethod;

        removeMethod = clazz.getDeclaredMethod("remove", Object.class);
        getMethod = clazz.getDeclaredMethod("get", Object.class);
        keySetMethod = clazz.getDeclaredMethod("keySet");

        Set<String> keySet;
        keySet = (Set<String>) keySetMethod.invoke(map);

        for (String fieldToClear : fieldsToCleanUp) {
            if (!keySet.contains(fieldToClear))
                throw new IllegalArgumentException();
        }

        for (String keyToRemove : fieldsToCleanUp) {
            removeMethod.invoke(map, keyToRemove);
        }

        StringBuilder resultString = new StringBuilder();
        for (String keyToPrint : fieldsToOutput) {
            resultString.append(keyToPrint)
                    .append(":")
                    .append(getMethod.invoke(map, keyToPrint))
                    .append("\n");
        }
        System.out.println(resultString.toString());
    }


    // методы для обратоки объекта через Reflection API, если он не является реализацией Map
    private void processNotMap(Object object, Set<String> fieldsToCleanUp, Set<String> fieldsToOutput) throws IllegalArgumentException{
        Class clazz = object.getClass();
        List<Field> reflectionFields = new ArrayList<>();
        List<String> reflectionFieldsNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        // получение списка всех полей(если private - разрешить доступ)
        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            reflectionFields.add(field);
        }

        // заполнение списка именами полученных полей
        for (Field field : reflectionFields) {
            reflectionFieldsNames.add(field.getName());
        }

        // если хоть одно имя поля из входного сета fieldsToCleanUp отсутствует у объекта
        // выбрасываем исключение
        for (String fieldToClear : fieldsToCleanUp) {
            if (!reflectionFieldsNames.contains(fieldToClear))
                throw new IllegalArgumentException();
        }

        StringBuilder resultString = new StringBuilder();
        // обработка каждого поля
        for (Field field : reflectionFields) {
            // приравнять к значению по умолчанию или null
            if (fieldsToCleanUp.contains(field.getName()))
                setDefaultOfNull(object, field);
            // преобразовать значение поля в строку
            if (fieldsToOutput.contains(field.getName()))
                resultString.append(convertToString(object, field)).append("\n");
        }
        System.out.println(resultString.toString());
    }

    // установка поля в значение по умолчанию для примитивов и в null для ссылочных типов
    private void setDefaultOfNull(Object workObject, Field field) {
        Class<?> fieldType = field.getType();
        try {
            switch (fieldType.getName()) {
                case "boolean":
                    field.setBoolean(workObject, false);
                    break;
                case "char":
                    field.setChar(workObject, '\u0000');
                    break;
                case "byte":
                    field.setByte(workObject, (byte) 0);
                    break;
                case "short":
                    field.setShort(workObject, (short) 0);
                    break;
                case "int":
                    field.setInt(workObject, (int) 0);
                    break;
                case "long":
                    field.setLong(workObject, 0L);
                    break;
                case "float":
                    field.setFloat(workObject, 0.0F);
                    break;
                case "double":
                    field.setDouble(workObject, 0.0D);
                    break;
                default:
                    field.set(workObject, null);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // преобразует в строку с учетом типа передаваемого поля
    private String convertToString(Object workObject, Field field) {
        Class<?> fieldType = field.getType();
        try {
            switch (fieldType.getName()) {
                case "boolean":
                    return getString(field, field.getBoolean(workObject));
                case "char":
                    return getString(field, field.getChar(workObject)) +
                            " codePoint:" + Character.codePointAt(new char[]{field.getChar(workObject)}, 0);
                case "byte":
                    return getString(field, field.getByte(workObject));
                case "short":
                    return getString(field, field.getShort(workObject));
                case "int":
                    return getString(field, field.getInt(workObject));
                case "long":
                    return getString(field, field.getLong(workObject));
                case "float":
                    return getString(field, field.getFloat(workObject));
                case "double":
                    return getString(field, field.getDouble(workObject));
                default:
                    return getString(field, field.get(workObject));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Функция возвращает строковое представление поля объекта, полученного с помощью
     * Reflection API. Если поле имеет ссылочный тип и уже приравнено к null,
     * т.е. fieldValue = null, то вместо его значения подставляется строка "null".
     *
     * @param field      поле, от которого мы хотим получить строковое представление
     * @param fieldValue значение, хранящееся в поле field
     * @return строку типа "тип имя_поля значение"
     */
    private String getString(Field field, Object fieldValue) {
        StringBuilder sb = new StringBuilder()
                .append(field.getType().getName())
                .append(" ")
                .append(field.getName())
                .append(" ")
                .append(fieldValue);
        return sb.toString();
    }
}
