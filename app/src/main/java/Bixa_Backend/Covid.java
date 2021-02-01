
package Bixa_Backend;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Covid {
    final static String archivoSintomas = "sintomas.txt";
    final static String archivoMascara = "mascara.txt";

    public static HashSet getSintomas(Context context){
        Scanner fileIn;
        Random chis = new Random();
        HashSet<String> sintomas = new HashSet<>();
         try {
             // Se lee el archivo
             InputStream lector = context.getAssets().open(archivoSintomas);
             int size = lector.available();
             byte[] buffer = new byte[size];
             lector.read(buffer);
             lector.close();

             String contenido = new String(buffer);
             // Separa el contenido en saltos de linea
             StringTokenizer stk = new StringTokenizer(contenido, "\n");
             while(stk.hasMoreTokens()) {
                 sintomas.add(stk.nextToken());
             }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
             e.printStackTrace();
         }

        return sintomas;
    }
    
    /*public static ArrayList usoCubrebocas(){
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> mascara = new ArrayList<>();
         try {
            // Se lee el archivo
            InputStream lector = context.getAssets().open(archivoMascara);
            int size = lector.available();
            byte[] buffer = new byte[size];
            lector.read(buffer);
            lector.close();

            String contenido = new String(buffer);
            // Separa el contenido en saltos de linea
            StringTokenizer stk = new StringTokenizer(contenido, "\n");
            while(stk.hasMoreTokens()) {
                saludos.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        
        return mascara;
    }*/
    
    
}
