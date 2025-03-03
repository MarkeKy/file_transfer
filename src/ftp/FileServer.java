package ftp;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class FileServer {
    public static void main(String[] args) {
        int port = 12345; // Port to listen on
 
        ExecutorService executor = Executors.newFixedThreadPool(5); // Thread pool for handling clients
 
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
 
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());
 
                executor.submit(() -> {
                    try (InputStream in = socket.getInputStream();
                         FileOutputStream fileOut = new FileOutputStream("received_file_" + System.currentTimeMillis())) {
 
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            fileOut.write(buffer, 0, bytesRead);
                        }
 
                        System.out.println("File received and saved");
                    } catch (IOException e) {
                        System.err.println("Error receiving file: " + e.getMessage());
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            System.err.println("Error closing socket: " + ex.getMessage());
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            executor.shutdown(); // Graceful shutdown of thread pool
        }
    }
}