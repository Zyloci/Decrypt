import java.io.File;

public class App{
    public static void main(String args[]){
        File file = new File("SecretMessage.txt");
        Decrypt dec = new Decrypt();
        dec.findKeyAndDecrypt(file);
    }
}