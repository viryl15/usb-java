/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 *
 * @author DYLAN
 */
public class UsbConsole {

    public UsbConsole() throws IOException{
        copyAllFast1();
    }
    
    
    
    private void copyAllFast1() throws IOException{
        
        System.out.println("Listing ...");
        for (File listRoot : File.listRoots()) {
            if (listRoot.isDirectory()){
                if (listRoot.list()[0].equals("System Volume Information")){
                    //btnCopyAll.setDisable(true);
                    FileInputStream fis;
                    FileOutputStream fos;
                    //BufferedInputStream bis;
                    FileChannel fc;

                    File newDirectory;
                    newDirectory = new File(File.listRoots()[0]+"newDirectory");
                    newDirectory.mkdirs();

                    File[] newDirectoryList = newDirectory.listFiles();
                    ArrayList<String> newDirectoryListString = new ArrayList<>();
                    for(File f : newDirectoryList){
                        newDirectoryListString.add(f.getName());
                        System.out.println("File name "+f.getName());
                    }
                    
                    ArrayList<String> filesList = listAllFiles(listRoot);
                    System.out.println("File 5 => "+filesList.get(5));
                    for(int j=0; j < filesList.size(); j++){
                        try{
                            //Création des objets
                            File fileTest = new File(filesList.get(j));
                                System.out.println(filesList.get(j));
                            //Empecher la copie si le fichier existe deja dans la destination
                            if(newDirectoryListString.contains(fileTest.getName())){
                                System.out.println("This file already exist !!!!!");
                                continue;
                            }
                            fis = new FileInputStream(fileTest);
                            //On récupère le canal
                            fc = fis.getChannel();
                            //On en déduit la taille
                            int size = (int)fc.size();
                            //On crée un buffer
                            //correspondant à la taille du fichier
                            ByteBuffer bBuff = ByteBuffer.allocate(size);
                            //Démarrage de la lecture
                            fc.read(bBuff);
                            //On prépare à la lecture avec l'appel à flip
                            bBuff.flip();
                            //La méthode array retourne un tableau de byte
                            byte[] tabByte = bBuff.array();
                            //creation de la copie

                            String[] d = filesList.get(j).replace("\\", "/").split("/");
                            String route = "";
                            for(int k=1; k < d.length-1; k++){
                                route += "\\" +d[k];
                            }
                            newDirectory = new File(File.listRoots()[0]+"newDirectory1"+route);
                            newDirectory.mkdirs();
                            //System.out.println(fileLv2.getItems().get(j).replace("\\", "/").split("/")[0]);
                            File f2 = new File(newDirectory.getPath()+"\\"+d[d.length -1]);
                            //System.out.println("newDirectory.getPath()+\"\\\\\"+d[d.length -1]:"+newDirectory.getPath()+"\\"+d[d.length -1]);
                            f2.createNewFile();
            //                copyProgressThread = new Thread(new progressThread(fileTest, f2));
            //                copyProgressThread.start();
                            fos = new FileOutputStream(f2);
                            fos.write(tabByte);

                            fis.close();
                            fos.close();
                            System.out.println("Copie terminee !!!!!");
                        }catch(FileNotFoundException ex){

                        }
                    }
                }
            }
        }
    }
    private ArrayList listAllFiles(File f){
        ArrayList<String> filesList = new ArrayList<>();
        if(f.isDirectory()){
            File[] f1 = f.listFiles();
            for (File file : f1){
                listAllFiles(file);
                //System.out.println(file.getPath());
            }
        }else if(f.isFile()) {
            filesList.add(f.getPath().trim());
        }
        return filesList;
    }
    public static void main(String[] args) throws IOException{
        UsbConsole usbc = new UsbConsole();
    }
}
