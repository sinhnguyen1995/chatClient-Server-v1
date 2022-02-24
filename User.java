import java.io.Serializable;
import java.util.Scanner;
//import java.util.Scanner;

public class User implements Serializable {
    private String username;
    private String password;

    public User()
    {

    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void inputInforUser()
    {
        Scanner scn = new Scanner(System.in);
        System.out.println("username: ");
        String username = scn.nextLine();
        this.username = username;
        System.out.println("Password: ");
        String password = scn.nextLine();
        this.password = password;
    }
}
