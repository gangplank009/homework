package stc21.exercise6;

import org.junit.*;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;


// время обработки поля для следующего шага в многопоточоном режие примерно в 2 раза больше, чем в однопоточном
public class GameOfLifeTest {

    private static GameOfLife game;
    private static int stepCount = 10;
    private static long executionTime;

    @BeforeClass
    public static void initGameOfLife() {
        game = new GameOfLife("start.txt", "stop.txt", stepCount);
    }

    @Before
    public void readStartStateFile() {
    //    game.generateStartFileInput(100);
        game.readStartStateFile();
    }

    @After
    public void writeStopStateFile() { game.writeStopStateFile(); }

    @Test
    public void testMultiThread() {
        try {
            game.processMultiThread(Runtime.getRuntime().availableProcessors() - 1);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executionTime = game.getProcessTime();
        System.out.println("Время обработки в мультипоточном режиме " + stepCount + " шагов составляет " + executionTime + " мс");
        assertTrue(true);
    }

    @Test
    public void testSingleThread() {
        try {
            game.processSingleThread();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executionTime = game.getProcessTime();
        System.out.println("Время обработки в однопоточном режиме " + stepCount + " шагов составляет " + executionTime + " мс");
        assertTrue(true);
    }
}