import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.Math;
import java.util.LinkedList;
import java.util.HashMap;

//-Pedro Dalmazo Vaz
//Class tasked with reading, decrypting and returning an answer
public class Decrypt{
    //Class variables
    File encryptedFile;
    LinkedList<String> message;
    Character[] charVect = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V',
    'W','X','Y','Z','.',',',';','!','?'};

    //Placeholder constructor method
    public Decrypt(){
    }
    
    //First call method for decryption
    public void findKeyAndDecrypt(File file){
        findKeyAndDecrypt(file, 0); //0 being the index for the most common letter in portuguese 'a'
    }
    //Actual method for finding the key and decrypting
    public void findKeyAndDecrypt(File file, int mostCommonLetterPort){
        int mostCommonLetterTxt = mostCommonLetterIndex(file);
        int key = Math.abs(mostCommonLetterTxt - mostCommonLetterPort -1);
        decryptMessage(file, key);
    }
    
    //Method that decrypts an encrypted file with the key
    public void decryptMessage(File file, int key){
        this.encryptedFile = file;
        try(BufferedReader buffReader = new BufferedReader(new FileReader(file))){
            String line = "", word = "";
            Boolean accepted = true;
            message = new LinkedList<String>(); 
            while((line = buffReader.readLine()) != null){
                String[] wordLine = line.split("#");
                for(String s: wordLine){
                   for(int i=0; i<s.length(); i++){
                       word = word + decryptChar(s.charAt(i), key);
                   }
                   message.add(word);
                   word = "";
                } 
                message.add("\n");
            }
            for(String str: message){
                for(int j=0; j<str.length(); j++){
                    if(str.charAt(j)=='K' || str.charAt(j)=='W' || str.charAt(j)=='Y' ){ //Letters that can't appear in portuguese
                        accepted = false;
                    }
                }
            }
            if(accepted==true){
                writeSolvedMessage(message);
            }
            else{
                findKeyAndDecrypt(file, 4); //4 being the index for the second most commun letter in portuguese 'e'
            }
        }
        catch(IOException except){
            System.out.println("File error");
        }
    }

//-------------------------------------------------------------------------------------------------------------------
    
    //Method used to find the most commum letter index in the text 
    public int mostCommonLetterIndex(File file){
        this.encryptedFile = file;
        try(BufferedReader buffReader = new BufferedReader(new FileReader(file))){
            String line = "";
            Character common = 'a';
            HashMap<Character, Integer> charInt = generateIntCharTable();
            HashMap<Character, Integer> distribution = new HashMap<Character, Integer>();
            distribution.put(common, 0); 
            while((line = buffReader.readLine()) != null){
                String[] wordLine = line.split("#");
                for(String s: wordLine){
                    for(int i=0; i<s.length(); i++){
                        if(distribution.containsKey(s.charAt(i))){
                            distribution.put(s.charAt(i), distribution.get(s.charAt(i))+ 1);
                            if(distribution.get(s.charAt(i)) > distribution.get(common)){
                                common = s.charAt(i);
                            } 
                        }
                        else{
                            distribution.put(s.charAt(i), 1);
                            if(distribution.get(s.charAt(i)) > distribution.get(common)){
                                common = s.charAt(i);
                            } 
                        }
                    }
                }
            } 
            return charInt.get(common);
        }
        catch(IOException except){
            return 0;
        }
    }

    //Method that takes an encrypted char and a key and returns the decrypted char 
    public Character decryptChar(Character c, int key){
        HashMap<Integer, Character> intToChar = generateCharIntTable();
        HashMap<Character, Integer> charToInt = generateIntCharTable();       
        int decryptValue = charToInt.get(c) + key;
        while(decryptValue > 30){
            decryptValue = decryptValue - 31;
        }
        return intToChar.get(decryptValue);
    }

    //Generates a map with int as keys and characters as values
    public HashMap<Integer, Character> generateCharIntTable(){
        HashMap<Integer, Character> table = new HashMap<Integer, Character>();
        for(int i=0; i<=30; i++){
            table.put(i, charVect[i]);
        }
        return table;
    } 
    //Generates a map with characters as keys and int as values
    public HashMap<Character, Integer> generateIntCharTable(){
        HashMap<Character, Integer> table = new HashMap<Character, Integer>();
        for(int i=0; i<=30; i++){
            table.put(charVect[i], i);
        }
        return table;
    } 

    //Writes the drecrypted message on "SolvedMessage.txt"
    public void writeSolvedMessage(LinkedList<String> message){
        File file = new File("SolvedMessage.txt");
        if(file.delete()){
            try{
                file.createNewFile();
            }
            catch(IOException e){
                System.out.println("Morreu");
            }
        }
        FileWriter writer;
        try{
            writer = new FileWriter("SolvedMessage.txt");
            for(String s: message){
                writer.write(s+" ");
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Morreu");
        }         
    }
}