
package Bixa_Backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Datos {
    final static String archivoDatosInteresantes = "Bixa_Backend/Archivos_txt/datosInteresantes.txt";
    public static String getDatosInteresantes() {
        
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> datos = new ArrayList<>();
        
        try {
            
            fileIn = new Scanner(new FileReader(archivoDatosInteresantes));
            while (fileIn.hasNextLine() ){ 
                datos.add(fileIn.nextLine());
            }
            fileIn.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        return datos.get(chis.nextInt(datos.size()-1));
    }   
    
}
