
package Bixa_Backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Covid {
    final static String archivoSintomas = "Bixa_Backend/Archivos_txt/sintomas.txt";
    final static String archivoMascara = "Bixa_Backend/Archivos_txt/mascara.txt";

    public static HashSet getSintomas(){
        Scanner fileIn;
        Random chis = new Random();
        HashSet<String> sintomas = new HashSet<>();
         try {
            
            fileIn = new Scanner(new FileReader(archivoSintomas));
            while (fileIn.hasNextLine() ){ 
                sintomas.add(fileIn.nextLine());
            }
            fileIn.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return sintomas;
    }
    
    public static ArrayList usoCubrebocas(){
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> mascara = new ArrayList<>();
         try {
            
            fileIn = new Scanner(new FileReader(archivoMascara));
            while (fileIn.hasNextLine() ){ 
                mascara.add(fileIn.nextLine());
            }
            fileIn.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return mascara;
    }
    
    
}
