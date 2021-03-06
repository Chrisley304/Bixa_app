
package Bixa_Backend;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * En esta clase se lee el archivo txt de datos.txt para que bixa elija un dato curioso de los almacenados en este
 */
public class Datos {
    final static String archivoDatosInteresantes = "datosInteresantes.txt";

    /**
     * Metodo donde se encuentra el algoritmo para que bixa "diga" datos interesantes
     * @param context contexto de la aplicacion requerido para abrir los archivos txt
     * @return Regresa el chiste en una String
     */
    public static String getDatosInteresantes(Context context) {
        
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> datos = new ArrayList<>();
        
        try {
            // Se lee el archivo
            InputStream lector = context.getAssets().open(archivoDatosInteresantes);
            int size = lector.available();
            byte[] buffer = new byte[size];
            lector.read(buffer);
            lector.close();

            String contenido = new String(buffer);
            // Separa el contenido en saltos de linea
            StringTokenizer stk = new StringTokenizer(contenido, "\n");
            while(stk.hasMoreTokens()) {
                datos.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos.get(chis.nextInt(datos.size()-1));
    }   
    
}
