package server.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.ServerApp;
import shared.MessageController;

public class ClientHandler extends Thread {

    private ServerApp serverApp;
    private Socket clientSocket;
    private MessageController messageController;
    private DataInputStream fromClientStream;
    private DataOutputStream toClientStream;

    public ClientHandler(Socket clientSocket, MessageController messageController, ServerApp serverApp) {
        this.clientSocket = clientSocket;
        this.messageController = messageController;
        this.serverApp = serverApp;
    }

    public void sendMessage(String message) throws IOException {
        toClientStream.writeUTF(message);
    }

    public void run() {

        try {

            String msg = "La conexión se ha realizado con ";
            msg += clientSocket.getLocalAddress().toString();
            System.out.println(msg);

            fromClientStream = new DataInputStream(clientSocket.getInputStream());
            toClientStream = new DataOutputStream(clientSocket.getOutputStream());

            String messages = messageController.takeMessages();
            toClientStream.writeUTF(messages);

            String clientName = fromClientStream.readUTF();
            System.out.println("Se ha conectado el usuario " + clientName);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            while (true) {

                Date date = new Date();
                String clientMessage = fromClientStream.readUTF();

                if (clientMessage.equals("bye")) {
                    toClientStream.writeUTF("goodbye");
                    serverApp.deleteUser(clientName, this);
                    break;
                }

                String fullMessage ="["  + dateFormat.format(date) + "] " +  clientName + ": " + clientMessage;
                System.out.println(fullMessage);

                messageController.insertMessages(fullMessage);

            }
            System.out.println(clientName + " se ha ido");
            fromClientStream.close();
        } catch (IOException e) {
            System.out.println("\n--------------------");
            System.out.println("ERROR DE CONEXIÓN");
            System.out.println("--------------------\n");
        }
    }
}