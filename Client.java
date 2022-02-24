import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket clientSocket = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    private User user;
    private Message mes;
    final static int PORT_NUMBER = 9091;

    public Client (InetAddress ip, int port) throws IOException {

        clientSocket = new Socket(ip, port);
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public boolean checkPass(String str) {
        if (str.length() >= 8)
        {
            char ch;
            boolean capitalFlag = false;
            boolean lowerCaseFlag = false;
            boolean numberFlag = false;
            for(int i=0;i < str.length();i++) {
                ch = str.charAt(i);
                if( Character.isDigit(ch)) {
                    numberFlag = true;
                }
                else if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                }
                if(numberFlag && capitalFlag && lowerCaseFlag)
                    return true;
            }
        }

        return false;
    }


    /*---------------------------------------------------------------------------------
    * Func: public void sendMess()
    * Params:
    *Description: set up a message and send it out via socket. Check the content which is
    * in the message. If it equals with "Exit" String then stop send, in other read the message
    * through socket and print the message content out console.
    *
    ----------------------------------------------------------------------------------*/
    public void sendMess() throws IOException, ClassNotFoundException {
        while (true)
        {
            mes.setMessage();
            objectOutputStream.writeObject(mes);
            if(!mes.getMessage().equalsIgnoreCase("Exit"))
            {
                Message returnMes = (Message) objectInputStream.readObject();
                System.out.println("return message: " + returnMes.getMessage());
            }
            else
                break;;
        }
    }

    /* ----------------------------------------------------------------------------------------
    * Func: public void login()
    * Params:
    * Description: request User enters username and  password. After which send through for server
    * side. Concurrently It re-sends a message to check If it is equal "login succeed" then allow
    * a message be sent out.
    * Return type: void
    ------------------------------------------------------------------------------------------*/
    public void login() throws IOException, ClassNotFoundException {
        while (true)
        {
            setUser();
            objectOutputStream.writeObject(this.user);
            String serverRes = objectInputStream.readUTF();
            if(!serverRes.equalsIgnoreCase("login succeed"))
            {
                System.out.println("username or password aren't correct.");
            }
            else
            {
                System.out.println("Successful login.");
                sendMess();
                break;
            }
        }

    }

    public void setUser()
    {
        user.inputInforUser();
    }

    /* -----------------------------------------------------------------------------------------
     * Func: public void register()
     * Params:
     * Description: Request User enters username and password. Check if the password is valid or not.
     * if it is valid then send User object via server side.
     * Server responses a message to notify for client side this username existed beside database or not
     * if it is valid then invite user re-register, otherwise allows login user and sending message
     ------------------------------------------------------------------------------------------ */
    public void register() throws IOException, ClassNotFoundException {
        while (true)
        {
            setUser();
            if (checkPass(user.getPassword()))
            {
                objectOutputStream.writeObject(user);
                String serverRes = objectInputStream.readUTF();
                if (serverRes.equalsIgnoreCase("Username already existed"))
                {
                    System.out.println("invite you to re-register.");
                }
                else
                {
                    System.out.println("Register successfully.");
                    login();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client myClient = new Client(InetAddress.getByName("hostname"), PORT_NUMBER);

        Scanner scn = new Scanner(System.in);
        System.out.println("Invite user choose.");
        System.out.println("type 'register' to register.");
        System.out.println("type 'login' to login.");
        System.out.println("type 'message' to send a message.");
        System.out.println("type 'exit' to exit.");

        String choose = scn.nextLine();
        while (!choose.equalsIgnoreCase("Exit"))
        {
            if (choose.equalsIgnoreCase("Register"))
            {
                myClient.register();
            }
            else if (!choose.equalsIgnoreCase("Login"))
            {
                myClient.login();
            }
            else
                myClient.sendMess();
        }

    }
}
