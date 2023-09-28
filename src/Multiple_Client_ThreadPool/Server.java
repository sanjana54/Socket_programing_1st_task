package Multiple_Client_ThreadPool;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Create a thread pool with 5 threads
        try (ServerSocket serverSocket = new ServerSocket(5100)) {
            System.out.println("Listening to port number: 5100");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                    // Create a new thread to handle the client's messages
                    executorService.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Shutdown the thread pool when done
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                Object clientMessage = ois.readObject();

                String message = (String) clientMessage;
                System.out.println("From " + clientSocket.getRemoteSocketAddress() + ": " + message);

                if ("stop".equalsIgnoreCase(message)) {
                    break;
                }

                String serverMessage = message.toUpperCase();
                oos.writeObject(serverMessage);
                oos.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Close the client socket when done
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}