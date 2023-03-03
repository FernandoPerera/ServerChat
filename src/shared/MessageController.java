package shared;

import java.util.ArrayList;
import java.util.List;

public class MessageController {

    private List<String> messagesList = new ArrayList<>();
    private String message;

    public void insertMessages(String newMessage){
        this.messagesList.add(newMessage);
    }

    synchronized public String takeMessages() {

        this.message = "\nMensajes enviados : {\n";

        if (this.messagesList.isEmpty()) {
            this.message += "\tno se han enviado mensajes\n";
        } else {
            this.messagesList.forEach( (message) -> {
                this.message += "\t- " + message + "\n";
            });
        }

        this.message += "}\n";

        return message;

    }

    synchronized public List<String> getMessageList(){
        return this.messagesList;
    }
    
}
