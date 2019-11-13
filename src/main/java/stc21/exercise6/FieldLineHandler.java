package stc21.exercise6;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author  PavelEfimov
 *
 * Класс FieldLineHandler представляет собой единицу работы для ExecutorService по обработке поля.
 * В конструкторе передается поле "игры в жизнь", индекс обрабатываемой строки поля, флаг выполнения в
 * одном потоке. При выполнении в одном потоке задача обрабатывает сразу все строки входного поля.
 *
 * */

public class FieldLineHandler implements Callable<char[][]> {

    // константы положения на поле
    public static final String UP_LEFT_CORNER = "UP LEFT CORNER";
    public static final String UP_SIDE = "UP SIDE";
    public static final String UP_RIGHT_CORNER = "UP RIGHT CORNER";
    public static final String RIGHT_SIDE = "RIGHT SIDE";
    public static final String DOWN_RIGHT_CORNER = "DOWN RIGHT CORNER";
    public static final String DOWN_SIDE = "DOWN SIDE";
    public static final String DOWN_LEFT_CORNER = "DOWN LEFT CORNER";
    public static final String LEFT_SIDE = "LEFT SIDE";
    public static final String CENTER = "CENTER";

    // константы-символы
    public static final char LIFE = '#';
    public static final char NO_LIFE = '.';

    private char[][] field;
    private char[][] nextStepField;

    private int sideSize;

    private int lineIndex;
    private boolean singleThread;

    public FieldLineHandler(char[][] field, int lineIndex, boolean singleThread) {
        this.field = field;
        this.lineIndex = lineIndex;
        this.singleThread = singleThread;
        this.sideSize = field[0].length;
        this.nextStepField = new char[sideSize][sideSize];
    }

    @Override
    public char[][] call() {
        String positionOnField = null;
        if (singleThread) {
            for (int y = 0; y < sideSize; y++) {
                for (int x = 0; x < sideSize; x++) {
                    positionOnField = definePositionOnField(y, x, sideSize);
                    nextStepField[y][x] = setLife(positionOnField, field[y][x], y, x);
                }
            }
            return nextStepField;
        } else {
            for (int x = 0; x < sideSize; x++) {
                positionOnField = definePositionOnField(lineIndex, x, sideSize);
                nextStepField[lineIndex][x] = setLife(positionOnField, field[lineIndex][x], lineIndex, x);
            }
            char[][] nextStepLine = new char[1][sideSize];
            nextStepLine[0] = nextStepField[lineIndex];
            return nextStepLine;
        }
    }

    // метод для определения положения клетки на поле(в углу, у стороны, в центе)
    private String definePositionOnField(int y, int x, int sideSize) {
        sideSize -= 1;
        if (y == 0 && x == 0)
            return UP_LEFT_CORNER;
        if (y == 0 && x != sideSize)
            return UP_SIDE;
        if (y == 0 && x == sideSize)
            return UP_RIGHT_CORNER;
        if (y != sideSize && x == sideSize)
            return RIGHT_SIDE;
        if (y == sideSize && x == sideSize)
            return DOWN_RIGHT_CORNER;
        if (y == sideSize && x != 0)
            return DOWN_SIDE;
        if (y == sideSize && x == 0)
            return DOWN_LEFT_CORNER;
        if (y != sideSize && x == 0)
            return LEFT_SIDE;
        return CENTER;
    }

    // определяем будет ли жизнь в клетке на следующем шаге
    // область просмотра соседей определяется положением клетки на поле
    private char setLife(String positionOnField, char lifeState, int currentTileY, int currentTileX) {
        switch (positionOnField) {
            case UP_LEFT_CORNER:
                return shouldBeLife(lifeState, 0, 1, 0, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case UP_SIDE:
                return shouldBeLife(lifeState, 0, 1, -1, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case UP_RIGHT_CORNER:
                return shouldBeLife(lifeState, 0, 1, -1, 0, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case RIGHT_SIDE:
                return shouldBeLife(lifeState, -1, 1, -1, 0, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case DOWN_RIGHT_CORNER:
                return shouldBeLife(lifeState, -1, 0, -1, 0, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case DOWN_SIDE:
                return shouldBeLife(lifeState, -1, 0, -1, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case DOWN_LEFT_CORNER:
                return shouldBeLife(lifeState, -1, 0, 0, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case LEFT_SIDE:
                return shouldBeLife(lifeState, -1, 1, 0, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
            case CENTER:
                return shouldBeLife(lifeState, -1, 1, -1, 1, currentTileY, currentTileX) ? LIFE : NO_LIFE;
        }
        return NO_LIFE;
    }

    // в зависимости от положения клетки на поле передаем смещения, в которых нужно искать соседей
    // просматриваем соседей
    // возвращаем boolean, ориентируясь на наличие жизни в переданной клетке и в клетках соседях
    private boolean shouldBeLife(char lifeState, int startY, int stopY, int startX, int stopX, int currentTileY, int currentTileX) {
        int neighbourCounter = 0;
        for (int offsetY = startY; offsetY <= stopY; offsetY++) {
            for (int offsetX = startX; offsetX <= stopX; offsetX++) {
                if (offsetX == 0 && offsetY == 0)
                    continue;
                if (field[currentTileY + offsetY][currentTileX + offsetX] == LIFE)
                    neighbourCounter++;
            }
        }
        if (lifeState == LIFE)
            return neighbourCounter >= 2 && neighbourCounter <= 3;
        else
            return neighbourCounter == 3;

    }
}
