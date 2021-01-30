
package Bixa_Backend;
import java.util.*;
import java.io.*;

public class Chis {    
    //final static String archivo = "C:\\Users\\Admin\\Documents\\Semestre 2021-1\\POO\\IA\\\\chistes.txt";
    final static String archivo = "Bixa_Backend/Arhivos_txt/chistes.txt";
    public static String getChiste() {
        
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> chistes = new ArrayList<>();
        
        try {
            
            fileIn = new Scanner(new FileReader(archivo));
            while (fileIn.hasNextLine() ){ 
                chistes.add(fileIn.nextLine());
            }
            fileIn.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }

        return chistes.get(chis.nextInt(chistes.size()-1));
    }   
} 
