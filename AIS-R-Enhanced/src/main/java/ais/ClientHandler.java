package ais;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// This class handles communication with a connected client
public class ClientHandler implements Runnable {

    private Socket clientSocket;

    // Constructor that initializes the client socket
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    // The run method is executed when the client handler thread is started
    @Override
    public void run() {
        try (
                // Create output stream to send data to the client
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream()); // Create input stream to receive data from the client
                  ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            Object receivedObject = in.readObject();
            out.writeObject("Response from server");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    // Method to close the client socket
    private void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
