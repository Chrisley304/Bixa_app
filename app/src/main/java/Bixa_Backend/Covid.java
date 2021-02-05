
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
/**
 * En esta clase se tiene el algoritmo para que Bixa te diga recomendaciones sobre el covid
 */

public class Covid {
    final static String archivoSintomas = "sintomas.txt";
    final static String archivoMascara = "mascara.txt";

    /**
     * Metodo donde se encuentra el algoritmo para que bixa "diga" sintomas del Covid
     * @param context contexto de la aplicacion requerido para abrir los archivos txt
     * @return Regresa el chiste en una String
     */
    public static HashSet getSintomas(Context context){
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

    /**
     * Metodo donde se encuentra el algoritmo para que bixa "diga" datos sobre el uso del cubrebocas
     * @param context contexto de la aplicacion requerido para abrir los archivos txt
     * @return Regresa el chiste en una String
     */
    public static ArrayList usoCubrebocas(Context context){

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
                mascara.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
             e.printStackTrace();
         }

        return mascara;
    }
    
    
}
