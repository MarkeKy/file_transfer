package ftp;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class FileClient {
    public static void main(String[] args) {
        String localhost = "127.0.0.1"; // Server IP
        int port = 12345; // Server port
        String[] filesToSend = {"Input1.txt", "Input2.txt", "Input3.txt"}; // List of files to send
 
        ExecutorService executor = Executors.newFixedThreadPool(3); // Thread pool for file transfer
 
        for (String filePath : filesToSend) {
            executor.submit(() -> {
                try (Socket socket = new Socket(localhost, port);
                     FileInputStream fileIn = new FileInputStream(filePath);
                     OutputStream out = socket.getOutputStream()) {
 
                    System.out.println("Connected to the server for file: " + filePath);
 
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileIn.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
 
                    System.out.println("File sent successfully: " + filePath);
                } catch (IOException e) {
                    System.err.println("Error sending file " + filePath + ": " + e.getMessage());
                }
            });
        }
 
        executor.shutdown(); // Graceful shutdown of thread pool
    }
}