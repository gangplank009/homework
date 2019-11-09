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

                List<String> request = new ArrayList<>();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String line = in.readLine();
                if (line != null) {
                    while (!line.isEmpty()) {
                        request.add(line);
                        System.out.println(line);
                        line = in.readLine();
                    }
                }

                // на GET запросы возвращаем список файлов и каталогов рабочей директории(каталог проекта)
                // на все остальные возвращаем код 404 Not Found

                if (isGetRequest(request))
                    httpResponse = buildPositiveResponse();
                else
                    httpResponse = buildNegativeResponse();

                clientSocket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private static boolean isGetRequest(List<String> request) {
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

        // построение заголовка и контента для запроса GET
        StringBuilder httpResponse = new StringBuilder("HTTP/1.1 200 OK\r\n");
        httpResponse.append("Content-Type: text/html\r\n\r\n")
                .append("<html>\n")
                .append("<body>\n")
                .append("<p>Current directory:</p>\n")
                .append("<p>").append(currentDir).append("</p>\n")
                .append("<p>Files and catalogs:</p>\n");
        // заполнение параграфов файлами и директориями
        if (filesAndDirectories != null) {
            for (File file : filesAndDirectories)
                httpResponse.append("<p>").append(file).append("</p>\n");
        }
        // добавление тегов конца контента
        httpResponse.append("</body>\n")
                .append("<html>\n");

        return httpResponse.toString();
    }

    private static String buildNegativeResponse() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<html>\n" +
                "<body>\n" +
                "<b>404 Not Found</b>\n" +
                "</body>\n" +
                "<html>\n";
    }
}
