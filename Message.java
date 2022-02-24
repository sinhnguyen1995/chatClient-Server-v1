import java.io.Serializable;
import java.util.Scanner;

public class Message implements Serializable {

    private String message;

    public Message(String inMessage) {
        String outMessage = inMessage.replaceAll("[^a-zA-Z]+", " ");
        this.message = outMessage;
    }
    public void setMessage() {
        Scanner scn = new Scanner(System.in);

        System.out.print("Enter message: ");
        this.message = scn.nextLine();
        System.out.println();
    }

    public String getMessage() {
        return message;
    }

}