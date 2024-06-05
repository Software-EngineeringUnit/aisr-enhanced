package ais;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// This class implements a server that listens for client connections
public class Server implements Runnable {
    private ServerSocket serverSocket;
    
    // Constructor that initializes the server socket on the specified port
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    // The run method is executed when the server thread is started
    @Override
    public void run() {
        try {
            // Continuously listen for client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Start a new thread to handle client communication
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
