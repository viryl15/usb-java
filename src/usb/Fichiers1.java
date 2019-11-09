/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;


/**
 *
 * @author DYLAN
 */
public class Fichiers1 {
    
    
    
    public static void main(String []args) throws IOException{
        
            Path path = Paths.get("C:\\Users\\DYLAN\\Documents\\NetBeansProjects\\USB\\src\\res/file1.txt");
            
            
            if(!Files.exists(path)){
                Files.createFile(path);
                System.out.println("Fichier cree !");
            }
            
        
    }
    
}
