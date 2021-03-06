
package Bixa_Backend;

import java.util.*;
import java.io.*;

import android.content.Context;
import android.content.res.AssetManager;


/**
 * En esta clase se lee el archivo txt de chistes.txt para que bixa elija un chiste de los almacenados en este
 */
public class Chis {
    final static String archivo = "chistes.txt";

    /**
     * Metodo donde se encuentra el algoritmo para que bixa "diga" chistes
     * @param context contexto de la aplicacion requerido para abrir los archivos txt
     * @return Regresa el chiste en una String
     */
    public static String getChiste(Context context) {

        Random chis = new Random();
        ArrayList<String> chistes = new ArrayList<>();

        try {

            // Se lee el archivo
            InputStream lector = context.getAssets().open(archivo);
            int size = lector.available();
            byte[] buffer = new byte[size];
            lector.read(buffer);
            lector.close();

            String contenido = new String(buffer);
            // Separa el contenido en saltos de linea
            StringTokenizer stk = new StringTokenizer(contenido, "\n");
            while(stk.hasMoreTokens()) {
                chistes.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chistes.get(chis.nextInt(chistes.size()-1));
    }   
} 
