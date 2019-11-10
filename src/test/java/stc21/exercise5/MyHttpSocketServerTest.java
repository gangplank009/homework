package stc21.exercise5;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.junit.Assert.*;

public class MyHttpSocketServerTest {

    private static Process process;

    @BeforeClass
    public static void startServer() throws IOException {
        File currentDir = new File(System.getProperty("user.dir"));
        String currentDirName = currentDir.getAbsolutePath();
        String pathToJava = "javac -cp " + currentDirName + "\\src\\main\\java\\stc21\\exercise5\\MyHttpSocketServer.java";
        String pathToClass = "java -cp " + currentDirName + "\\target\\classes stc21.exercise5.MyHttpSocketServer";
        process = Runtime.getRuntime().exec(pathToJava);
        process = Runtime.getRuntime().exec(pathToClass);
    }

    @AfterClass
    public static void stopServer() {
        process.destroy();
    }


    @Test
    public void testPositiveRequest() {
        try {
            Socket clientSocket = new Socket("localhost", 8090);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            String request = "GET localhost HTTP/1.1" + '\0';
            out.write(request.getBytes());

            int ch;
            StringBuilder positiveResponseFromServer = new StringBuilder();
            while ((ch = in.read()) != '\0' && ch != -1)
                positiveResponseFromServer.append((char) ch);

            String positiveResponse = MyHttpSocketServer.buildPositiveResponse();
            assertEquals(positiveResponseFromServer.toString(), positiveResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNegativeRequest() {
        try {
            Socket clientSocket = new Socket("localhost", 8090);
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            String request = "POST localhost HTTP/1.1" + '\0';
            out.write(request.getBytes());

            int ch;
            StringBuilder negativeResponseFromServer = new StringBuilder();
            while ((ch = in.read()) != '\0' && ch != -1)
                negativeResponseFromServer.append((char) ch);

            String negativeResponse = MyHttpSocketServer.buildNegativeResponse();
            assertEquals(negativeResponseFromServer.toString(), negativeResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}