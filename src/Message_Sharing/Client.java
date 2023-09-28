package Message_Sharing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        try {
            System.out.println("Client started ... ");
            try (Socket socket = new Socket("127.0.0.1", 5100);
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 Scanner sc = new Scanner(System.in)) {

                while (true) {
                    System.out.print("Input >> ");
                    String message = sc.nextLine();
                    oos.writeObject(message);
                    oos.flush();
                    if ("stop".equalsIgnoreCase(message)) {
                        break;
                    }

                    Object fromServer = ois.readObject();
                    System.out.println("From Server: " + (String) fromServer);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
