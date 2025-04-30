import java.io.*;
import java.net.*;

public class TcpEchoServerClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TcpEchoServerClient <server|client>");
            System.exit(1);
        }

        if (args[0].equalsIgnoreCase("server")) {
            runServer();
        } else if (args[0].equalsIgnoreCase("client")) {
            runClient();
        } else {
            System.err.println("Invalid argument. Use 'server' or 'client'.");
        }
    }

    private static void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(13531)) {
            System.out.println("Server started. Waiting for client...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                out.println("Echo: " + inputLine); // Отправляем обратно клиенту
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void runClient() {
        try (Socket socket = new Socket("localhost", 13531);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Type messages (or 'exit' to quit):");
            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(userInput); // Отправляем серверу
                System.out.println("Server reply: " + in.readLine()); // Читаем ответ
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}