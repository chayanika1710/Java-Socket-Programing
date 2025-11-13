import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;


// massage thread
class MessageHandler extends Thread {
    private Socket socket;

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("Device1: " + msg); // Prints messages from Device1
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
    }
}
// This class handles file sending in a separate thread
class FileSender extends Thread {
    private Socket socket;

    public FileSender(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("If you want , Enter your file name to send: ");
                String name = scanner.nextLine();

                if ("exit".equalsIgnoreCase(name)) {
                    System.out.println("Exiting file sender thread.");
                    break;
                }

                File file = new File("H:\\final\\final2.0\\" + name + ".txt");
                if (!file.exists()) {
                    System.out.println("File not found. Please try again.");
                    continue;
                }

                String fileName = file.getName();
                long fileSize = file.length();
                System.out.println("Sending file: " + fileName + " (" + fileSize + " bytes)");

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Send control signal indicating a file is being sent
                out.println("SEND");

                // Send file name
                out.println(fileName);

                // Create a stream to send file to the client
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                OutputStream outputStream = socket.getOutputStream();

                // Sending the file
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    System.out.println("Transferring...");
                }

                bufferedInputStream.close();
                outputStream.close();
                System.out.println("File transfer complete.");
                System.out.println("Connection closed.");
                System.exit(0);
            }

            // After exiting, close the socket and output stream
            socket.close();
            

        } catch (Exception e) {
            System.out.println("Error sending file: " + e.getMessage());
        }
    }
}

// This class handles file receiving in a separate thread
class FileReceiver extends Thread {
    private Socket socket;

    public FileReceiver(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                // Wait for a signal that a file is about to be sent
                String action = in.readLine();
                if ("SEND".equals(action)) {
                    String fileName = in.readLine();
                    System.out.println("\nReceiving file: " + fileName);

                    File file = new File("H:\\final\\final2.0\\" + fileName);
                    InputStream inputStream = socket.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                    byte[] buffer = new byte[2];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, bytesRead);
                    }

                    bufferedOutputStream.close();
                    inputStream.close();
                    System.out.println("File received and saved.\n");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            System.exit(0);
            System.out.println("Error receiving file: " + e.getMessage());
        }
    }
}

public class device2 {
    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket serverSocket = null;
        int choice;
        System.out.println("...................I am device2 .............................");

        try {
            // Try to connect as a client
            socket = new Socket("localhost", 9806);
            System.out.println("Connected to device1 as client.");
        } catch (Exception e) {
            System.out.println("Failed to connect as client, starting server...");
            try {
                // If client connection fails, start a server
                serverSocket = new ServerSocket(9806);
                socket = serverSocket.accept();
                System.out.println("Device1 connected to me as server.");
            } catch (Exception ex) {
                System.out.println("Failed to start server: " + ex.getMessage());
            }
        }

        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Enter 1 for massege || 2 for file transfer");
        choice = scanner1.nextInt();
       // Start the file sending and receiving threads
       if (socket != null) {

        // if(choice == 2){
            new FileReceiver(socket).start();
            new FileSender(socket).start();
        // }
        // else if(choice == 1){
        //     try {
        //         new MessageHandler(socket).start();
        //         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //         BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        //         String str;
    
        //         // Read user input and send messages
        //         while (true) {
        //             str = userInput.readLine();
        //             if (str.equalsIgnoreCase("exit")) {
        //                 out.println("device1 has left the chat.");
        //                 break;
        //             }
        //             out.println(str);
        //         }
    
        //         // Close sockets and input streams
        //         socket.close();
        //         if (serverSocket != null) {
        //             serverSocket.close();
        //         }
        //         userInput.close();
        //     } catch (Exception e) {
        //         System.out.println("Some Error Occurred");
        //         e.printStackTrace();
        //     }
        // }
    }
    }
}
