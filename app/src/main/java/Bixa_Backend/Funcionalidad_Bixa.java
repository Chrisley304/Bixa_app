/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Admin
 */
public class Funcionalidad_Bixa {
    
    
    final static String archivoFuncionalidad_Bixa = "funcionalidadesBixa.txt";
    public static ArrayList getFunciones(Context context) {
        
        Scanner fileIn;
        Random chis = new Random();
        ArrayList<String> funciones = new ArrayList<>();

        try {
            // Se lee el archivo
            InputStream lector = context.getAssets().open(archivoFuncionalidad_Bixa);
            int size = lector.available();
            byte[] buffer = new byte[size];
            lector.read(buffer);
            lector.close();

            String contenido = new String(buffer);
            // Separa el contenido en saltos de linea
            StringTokenizer stk = new StringTokenizer(contenido, "\n");
            while(stk.hasMoreTokens()) {
                funciones.add(stk.nextToken());
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return funciones;
    }   
    
}
