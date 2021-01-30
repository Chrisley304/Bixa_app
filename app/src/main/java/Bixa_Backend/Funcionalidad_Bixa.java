/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bixa_Backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class Funcionalidad_Bixa {
    
    
    final static String archivoFuncionalidad_Bixa = "Bixa_Backend/Archivos_txt/funcionalidadesBixa.txt";
    public static ArrayList getFunciones() {
        
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> funciones = new ArrayList<>();
        
        try {
            
            fileIn = new Scanner(new FileReader(archivoFuncionalidad_Bixa));
            while (fileIn.hasNextLine() ){ 
                funciones.add(fileIn.nextLine());
            }
            fileIn.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
        return funciones;
    }   
    
}
