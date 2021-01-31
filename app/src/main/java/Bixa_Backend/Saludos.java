
package Bixa_Backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Saludos {
    final static String Saludos_txt =  "Archivos_txt/saludo.txt";
    final static String Despedidas_txt = "Archivos_txt/despedida.txt";



    // recibe el nombre el del usuario.name
    public static String getSaludo() {

        File archivoSaludos = new File(Saludos_txt);
        File archivoDespedidas = new File(getFilesDir(), Despedidas_txt);
        archivoSaludos.getPath();
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> saludos = new ArrayList<>();

        try {

            fileIn = new Scanner(new FileReader(archivoSaludos));
            while (fileIn.hasNextLine()) {
                saludos.add(fileIn.nextLine());
            }
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return saludos.get(chis.nextInt(saludos.size() - 1));
    }

    // recibe el nombre el del usuario.name
    public static String getDespedida() {

        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> despedidas = new ArrayList<>();

        try {

            fileIn = new Scanner(new FileReader(archivoDespedidas));
            while (fileIn.hasNextLine()) {
                despedidas.add(fileIn.nextLine());
            }
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return despedidas.get(chis.nextInt(despedidas.size() - 1));
    }

}
