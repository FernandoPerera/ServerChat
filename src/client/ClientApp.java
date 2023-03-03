package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import shared.Constants;

public class ClientApp {
    public static void main(String[] args) throws Exception {

        try {

            InetAddress myIp = InetAddress.getLocalHost();
            Socket socket = new Socket(myIp, Constants.SERVER_PORT);

            DataOutputStream toServerStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream fromServerStream = new DataInputStream(socket.getInputStream());

            Scanner keyboardReader = new Scanner(System.in);

            System.out.print("Introduce tu nombre : ");
            String name = keyboardReader.nextLine();
            toServerStream.writeUTF(name);

            System.out.println(fromServerStream.readUTF());

            System.out.println("\t·····································");
            System.out.println("\tDebe introducir el comando 'message:'");
            System.out.println("\t     para poder enviar mensajes");
            System.out.println("\t·····································\n");

            Boolean exitChat = false;

            while (true) {

                Boolean exitLoop = false;

                System.out.print(" |- ");
                String clientMessage = keyboardReader.nextLine();

                while ( exitLoop == false ) {

                    if (clientMessage.toLowerCase().equals("bye")) {
                        exitChat = true;
                        exitLoop = true;
                    } else if( clientMessage.startsWith("message:") ) {
                        exitLoop = true;
                    } else{
                        System.out.println("\t| Debe introducir el comando 'message:' |");
                        System.out.print(" |- ");
                        clientMessage = keyboardReader.nextLine();
                    }
                    
                }

                toServerStream.writeUTF(clientMessage.substring(8));

                if (exitChat) {
                    System.out.println("here");
                    String farewell = fromServerStream.readUTF();

                    if (farewell.equals("goodbye")) {
                        System.out.println("\n------------------");
                        System.out.println(farewell);
                        System.out.println("------------------");
                    }
                    break;
                }

            }

            keyboardReader.close();
            toServerStream.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("=================");
            System.out.println("ERROR DE CONEXIÓN");
            System.out.println("=================");
        }

    }
}
