
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

public class Saludos {

    final static String archivoSaludos = "saludo.txt";
    final static String archivoDespedidas = "despedida.txt";

    // recibe el nombre el del usuario.name
    public static String getSaludo(Context context) {

        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> saludos = new ArrayList<>();

        try {
            // Se lee el archivo
            InputStream lector = context.getAssets().open(archivoSaludos);
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
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saludos.get(chis.nextInt(saludos.size() - 1));
    }

    // recibe el nombre el del usuario.name
    public static String getDespedida(Context context) {

        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> despedidas = new ArrayList<>();

        try {
// Se lee el archivo
            InputStream lector = context.getAssets().open(archivoDespedidas);
            int size = lector.available();
            byte[] buffer = new byte[size];
            lector.read(buffer);
            lector.close();

            String contenido = new String(buffer);
            // Separa el contenido en saltos de linea
            StringTokenizer stk = new StringTokenizer(contenido, "\n");
            while(stk.hasMoreTokens()) {
                despedidas.add(stk.nextToken());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return despedidas.get(chis.nextInt(despedidas.size() - 1));
    }

}
