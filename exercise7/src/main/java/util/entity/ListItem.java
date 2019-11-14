package util.entity;

/**
 * @author Pavel Efimov
 *
 * Класс ListItem является оберткой для вывода файлов из вложенных каталогов с отступами.
 * Поле itemName содержит название файла/каталога.
 * Поле nestingLvl представляет из себя множитель. Используется directories.jsp для добавления отступа.
 * */

public class ListItem {
    private String itemName;
    private int nestingLvl;

    public ListItem(String itemName, int nestingLvl) {
        this.itemName = itemName;
        this.nestingLvl = nestingLvl;
    }

    public String getItemName() {
        return itemName;
    }

    public int getNestingLvl() {
        return nestingLvl;
    }
}
