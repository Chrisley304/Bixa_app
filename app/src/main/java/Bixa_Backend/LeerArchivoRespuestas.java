
package Bixa_Backend;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Esta clase lee el achivo txt que le indiques y te devuelve una linea de este aleatoriamente
 */
public class LeerArchivoRespuestas {
    /**
     * Metodo donde se encuentra el algoritmo para que bixa "diga" lo que este en el archivo txt
     * @param context contexto de la aplicacion requerido para abrir los archivos txt
     * @param archivo incluye el nombre del archivo txt a leer
     * @return Regresa el chiste en una String
     */
    public static String getRespuesta(Context context, String archivo) {
        
        Scanner fileIn;
        Random al = new Random();
        ArrayList<String> datos = new ArrayList<>();
        
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
                datos.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datos.get(al.nextInt(datos.size()-1));
    }   
    
}
