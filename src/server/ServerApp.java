package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.threads.ClientHandler;
import shared.Constants;
import shared.MessageController;

public class ServerApp {

    private List<ClientHandler> users = new ArrayList<>();

    public void sendMessagesToOthers(String message, ClientHandler senderOfTheMessage) throws IOException {

        for ( ClientHandler client : users ){
            if (client != senderOfTheMessage) {
                client.sendMessage(message);
            }
        }

    }

    public void deleteUser(String name, ClientHandler userToDelete){
        users.remove(userToDelete);
        System.out.println("\n\tSe ha desconectado el usuario : " + "| " + name + " |\n");
    }

    private void executeServer() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)){
            
            MessageController messageController = new MessageController();

            while (true) {

                System.out.println("========================");
                System.out.println("Esperando por el cliente");
                System.out.println("========================");

                Socket clientSocket = serverSocket.accept();

                ClientHandler newClient = new ClientHandler(clientSocket, messageController, this);
                users.add(newClient);
                newClient.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ServerApp server = new ServerApp();
        server.executeServer();
    }

}