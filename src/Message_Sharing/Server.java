package Message_Sharing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){

        try (ServerSocket serverSocket = new ServerSocket(5100)) {
            System.out.println("Listening to port number: 5100");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

                    while (true) {
                        Object clientMessage = ois.readObject();

                        String message = (String) clientMessage;
                        System.out.println("From Client: " + message);

                        if ("stop".equalsIgnoreCase(message)) {
                            break;
                        }

                        String serverMessage = message.toUpperCase();
                        oos.writeObject(serverMessage);
                        oos.flush();

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
