import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Enter your username: ");
            String username = consoleReader.readLine();
            out.println(username);

            // Start a separate thread to continuously read messages from the server
            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Main thread for sending messages to the server
            System.out.println("Type your messages. Type 'exit' to quit.");
            String message;
            while (true) {
                message = consoleReader.readLine();
                out.println(message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
