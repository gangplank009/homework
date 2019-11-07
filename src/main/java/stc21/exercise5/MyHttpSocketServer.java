package stc21.exercise5;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyHttpSocketServer {
    public static void main(String[] args) throws IOException {
        final int serverPort = 8090;
        final ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.printf("Listening for connection on port %d ...\n", serverPort);
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected");

                String httpResponse;
                // на GET запросы возвращаем список файлов и каталогов рабочей директории(каталог проекта)
                // на все остальные возвращаем код 404 Not Found
                if (isGetRequest(clientSocket))
                    httpResponse = buildPositiveResponse();
                else
                    httpResponse = buildNegativeResponse();

                clientSocket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private static boolean isGetRequest(Socket clientSocket) throws IOException {
        List<String> request = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        String line = reader.readLine();
        while (!line.isEmpty()) {
            request.add(line);
            line = reader.readLine();
        }
        for (String requestLine : request) {
            if (requestLine.contains("GET") && requestLine.contains("HTTP/1.1")) {
                return true;
            }
        }
        return false;
    }

    private static String buildPositiveResponse() {
        File currentDir = new File(System.getProperty("user.dir"));
        File[] filesAndDirectories = currentDir.listFiles();

        StringBuilder httpResponse = new StringBuilder("HTTP/1.1 200 OK\r\n\r\n");
        httpResponse.append("Current directory:")
                    .append(currentDir)
                    .append("Files and catalogs:\n");

        if (filesAndDirectories != null) {
            for (File file : filesAndDirectories)
                httpResponse.append("\t")
                        .append(file)
                        .append("\n");
        }
        return httpResponse.toString();
    }

    private static String buildNegativeResponse() {
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }
}
