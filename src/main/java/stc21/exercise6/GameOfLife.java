package stc21.exercise6;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author PavelEfimov
 *
 * Класс GameOfLife используется для реализации "игры в жизнь". При создании объекта класса в параметры конструктора
 * передается имя файла с начальным состоянием поля, имя файла для записи результата, число шагов "игры в жизнь".
 * Содержит метод processSingleThread() и processMultiThread() для рассчета каждого нового шага в одном потоке или
 * в нескольких(ЧИСЛО ЛОГИЧЕСКИХ ЯДЕР - 1) через ExecutorService.
 *
 * */

public class GameOfLife {

    private char[][] field = new char[0][0];
    private char[][] nextStepField = new char[0][0];

    private String startStateFile;
    private String stopStateFile;
    private int steps;
    private int sideSize;
    private long processTime;

    public GameOfLife() {
    }

    public GameOfLife(String startStateFile, String stopStateFile, int steps) {
        String currentDir = System.getProperty("user.dir");
        this.startStateFile = currentDir + "\\src\\main\\java\\stc21\\exercise6\\" + startStateFile;
        this.stopStateFile = currentDir + "\\src\\main\\java\\stc21\\exercise6\\" + stopStateFile;
        this.steps = steps;
    }

    // метод для генерации поля заданной стороны
    public void generateStartFileInput(int sideSize) {
        if (startStateFile == null)
            return;
        // сгенерировали поле в start.txt
        Random randomGen = new Random();
        try (FileOutputStream fos = new FileOutputStream(startStateFile, false)) {
            for (int y = 0; y < sideSize; y++) {
                for (int x = 0; x < sideSize; x++)
                    fos.write(randomGen.nextBoolean() ? '#' : '.');
                fos.write(new byte[]{'\r', '\n'});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readStartStateFile() {
        if (startStateFile == null)
            return;
        int character = 0;

        // считали размер поля
        sideSize = 0;
        try (FileInputStream bufferedReader = new FileInputStream(startStateFile)) {
            while ((character = bufferedReader.read()) != -1) {
                sideSize++;
                if ((char) character == '\r') {
                    sideSize--;
                    field = new char[sideSize][sideSize];
                    nextStepField = new char[sideSize][sideSize];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // считали поле из start.txt
        try (FileInputStream fin = new FileInputStream(startStateFile)) {
            int xCounter = 0;
            int yCounter = 0;
            while ((character = fin.read()) != -1) {
                if ((char) character == '\r')
                    continue;

                if ((char) character == '\n') {
                    yCounter++;
                    xCounter = 0;
                    continue;
                }
                field[yCounter][xCounter++] = (char) character;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStopStateFile() {
        if (stopStateFile == null)
            return;
        // записали поле в stop.txt
        try (FileOutputStream fos = new FileOutputStream(stopStateFile)) {
            for (int y = 0; y < sideSize; y++) {
                for (int x = 0; x < sideSize; x++)
                    fos.write(field[y][x]);
                fos.write(new byte[]{'\r', '\n'});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processSingleThread() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        long before = System.currentTimeMillis();

        // на каждом шаге - новая задача экзекьютору
        for (int step = 0; step < steps; step++) {
            Future<char[][]> future = executorService.submit(new FieldLineHandler(field, 0, true));
            nextStepField = future.get();

            field = nextStepField;
            // можно добавить вывод в консоль drawStep(step);
        }
        processTime = System.currentTimeMillis() - before;
        executorService.shutdown();
    }

    public void processMultiThread(int threadNumber) throws InterruptedException, ExecutionException {
        List<FieldLineHandler> fieldLineHandlerList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);

        long before = System.currentTimeMillis();

        for (int step = 0; step < steps; step++) {
            fieldLineHandlerList.clear();

            // создаем список из задач на выполнение экзекьютору, равный стороне поля
            for (int i = 0; i < sideSize; i++)
                fieldLineHandlerList.add(new FieldLineHandler(field, i, false));
            // отправляем их на обработку
            List<Future<char[][]>> futures = executorService.invokeAll(fieldLineHandlerList);

            // ожидаем ответ от каждой задачи
            for (int index = 0; index < futures.size(); index++) {
                Future<char[][]> future = futures.get(index);
                char[][] buffer;
                buffer = future.get();
                nextStepField[index] = buffer[0];
            }

            field = nextStepField;
            // можно добавить вывод в консоль drawStep(step);
        }
        processTime = System.currentTimeMillis() - before;
        executorService.shutdown();
    }

    public long getProcessTime() {
        return processTime;
    }

    private void drawStep(int step) {
        // вывод в консоль
        System.out.println("Шаг №" + (step + 1));
        for (int y = 0; y < sideSize; y++) {
            for (int x = 0; x < sideSize; x++)
                System.out.print(field[y][x] + " ");
            System.out.println();
        }
        System.out.println();
    }
}


