package stc21.exercise5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Efimov
 *
 * Класс MyHttpSocketServer всю логику работы содержит в методе main():
 * 1) Создает ServerSocket, который слушает порт 8090 и в бесконечном цикле ждет подключения от клиента.
 * 2) Через InputStream сокета клиента читает запрос.
 * 3) Определяет его тип с помощью функции isGetRequest(List<String> request).
 * 4) Возвращает ответ со списком директорий проекта(buildPositiveResponse()) или ошибку (buildNegativeResponse())
 * 5) Записывает ответ в OutputStream сокета клиента.
 *
 * */

public class MyHttpSocketServer {

    private static final int serverPort = 8090;

    public static void main(String[] args) throws IOException {

        final ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.printf("Listening for connection on port %d ...\n", serverPort);
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();

                StringBuilder requestBuilder = new StringBuilder();

                int ch;
                while ((ch = in.read()) != '\0' && ch != -1)
                    requestBuilder.append((char) ch);

                // на GET запросы возвращаем список файлов и каталогов рабочей директории(каталог проекта)
                // на все остальные возвращаем код 404 Not Found
                String httpResponse;
                if (isGetRequest(requestBuilder.toString()))
                    httpResponse = buildPositiveResponse();
                else
                    httpResponse = buildNegativeResponse();

                httpResponse = httpResponse + '\0';
                out.write(httpResponse.getBytes());
            }
        }
    }

    // По первой строке входного запроса определяет какой тип и протокол использовался
    private static boolean isGetRequest(String request) {
        if (request.contains("GET") && request.contains("HTTP/1.1")) {
            return true;
        }
        return false;
    }

    // Возвращает строку с HTTP-заголовком(200 ОК) и телом в виде html-документа,
    // содержащим список всех папок директории проекта.
    // Вызывается при GET запросе
    public static String buildPositiveResponse() {
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

    // возвращает строку с HTTP-заголовком(404 Not Found) и телом в виде html-документа,
    // содержащим код ответа и сообщение об ошибке.
    // Вызывается при всех запросах, кроме GET
    public static String buildNegativeResponse() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<html>\n" +
                "<body>\n" +
                "<b>404 Not Found</b>\n" +
                "</body>\n" +
                "<html>\n";
    }
}
