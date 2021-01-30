
package Bixa_Backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Saludos {

    final static String archivoSaludos = "Bixa_Backend/Archivos_txt/saludo.txt";
    final static String archivoDespedidas = "Bixa_Backend/Archivos_txt/despedida.txt";

    // recibe el nombre el del usuario.name
    public static String getSaludo() {

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
