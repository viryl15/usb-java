/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import com.jfoenix.controls.JFXProgressBar;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.omg.SendingContext.RunTime;

/**
 *
 * @author DYLAN
 */
public class UsbController implements Initializable  {
    private Usb usb;
    private Thread copyThread, copyAllThread, copyProgressThread;
    private JFileChooser fch;
    private ArrayList<File> files = new ArrayList<>();
    @FXML
    private ListView<String> fileLv1;
    @FXML
    private ListView<String> fileLv2;
    @FXML
    private JFXProgressBar progressbar;
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnCopy;
    @FXML
    private TextField etExtension;
    @FXML
    private Button btnCopyAll;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressbar.setProgress(0);
        Tooltip etTtp = new Tooltip("Enter file extension");
        etExtension.setTooltip(etTtp);
        Tooltip lvEltTtp = new Tooltip("Files list");
        fileLv1.setTooltip(lvEltTtp);
//        fch = new JFileChooser();
//        fch.showOpenDialog(new JFrame());
    }
    
    @FXML
    private void list(){
        System.out.println("Listing ...");
        fileLv1.getItems().clear();
        for (File listRoot : File.listRoots()) {
            if (listRoot.isDirectory()){
                if (listRoot.list()[0].equals("System Volume Information")){
                    
                    listAllFiles(listRoot);
                }
            }
        }
    }
    
    @FXML
    private void copyAll() throws IOException{
        System.out.println("Copy all");
        copyAllThread = new Thread(new CopyAllFastProgress());
        copyAllThread.start();
    }
    @FXML
    private void copy() throws IOException{
        System.out.println("Copying ...");
        copyThread = new Thread(new CopyFastProgress());
        copyThread.start();
    }
    
    private void progress(File original, File copy)  throws IOException{
        //Création des objets
            FileInputStream fiso;
            FileInputStream fisc;
            FileChannel fco;
            FileChannel fcc;
            fiso = new FileInputStream(original);
            //On récupère le canal
            fco = fiso.getChannel();
            //On en déduit la taille
            int sizeo = (int)fco.size();
            int sizec = 0;
            //while(sizeo >= sizec){
                fisc = new FileInputStream(copy);
                //On récupère le canal
                fcc = fisc.getChannel();
                //On en déduit la taille
                sizec = (int)fcc.size();
                progressbar.setProgress((double)sizec/(double)sizeo);
                System.out.println((double)sizec/(double)sizeo);
                //if(sizec == sizeo)break;
            //}
    }
    private void copyAllFast1() throws IOException{
        //btnCopyAll.setDisable(true);
        FileInputStream fis;
        FileOutputStream fos;
        //BufferedInputStream bis;
        FileChannel fc;
        
        File newDirectory;
        newDirectory = new File(File.listRoots()[0]+"newDirectory1");
        newDirectory.mkdirs();
        
        File[] newDirectoryList = newDirectory.listFiles();
        ArrayList<String> newDirectoryListString = new ArrayList<>();
        for(File f : newDirectoryList){
            newDirectoryListString.add(f.getName());
            System.out.println("File name "+f.getName());
        }
        for(int j=0; j < fileLv1.getItems().size(); j++){
            try{
                //Création des objets
                File fileTest = new File(fileLv1.getItems().get(j));
                    System.out.println(fileLv1.getItems().get(j));
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
                
                String[] d = fileLv1.getItems().get(j).replace("\\", "/").split("/");
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
        btnCopyAll.setDisable(false);
    }
    class CopyAllFastProgress implements Runnable{

        @Override
        public void run() {
            try {
                btnCopyAll.setDisable(true);
                copyAllFast1();
            } catch (IOException ex) {
                Logger.getLogger(UsbController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void copyFast1() throws IOException{
        FileInputStream fis;
        FileOutputStream fos;
        //BufferedInputStream bis;
        FileChannel fc;
        
        File newDirectory = new File(File.listRoots()[0]+"newDirectory");
        newDirectory.mkdir();
        File[] newDirectoryList = newDirectory.listFiles();
        ArrayList<String> newDirectoryListString = new ArrayList<>();
        for(File f : newDirectoryList){
            newDirectoryListString.add(f.getName());
            System.out.println("File name "+f.getName());
        }
        for(int j=0; j < fileLv2.getItems().size(); j++){
            try{
                //Création des objets
                File fileTest = new File(fileLv2.getItems().get(j));
                    System.out.println(fileLv2.getItems().get(j));
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
                String[] d = fileLv2.getItems().get(j).replace("\\", "/").split("/");
                String route = "";
                for(int k=1; k < d.length-1; k++){
                    route += "\\" +d[k];
                }
                newDirectory = new File(File.listRoots()[0]+"newDirectory"+route);
                newDirectory.mkdirs();
                //System.out.println(fileLv2.getItems().get(j).replace("\\", "/").split("/")[0]);
                File f2 = new File(newDirectory.getPath()+"\\"+d[d.length -1]);
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
        btnCopy.setDisable(false);
    }
    private void copie() throws IOException{
        FileInputStream fis;
        FileOutputStream fos;
        File newDirectory = new File(File.listRoots()[0]+"newDirectory");
        newDirectory.mkdir();
        for(int j=0; j < fileLv2.getItems().size(); j++){
            try{
                //On instancie nos objets.
                //fis va lire le fichier et
                //fos va écrire dans le nouveau !
                
                File fileTest = new File(fileLv2.getItems().get(j));
                String[] d = fileLv2.getItems().get(j).replace("\\", "/").split("/");
                fis = new FileInputStream(fileTest);
                File f2=new File(newDirectory.getPath()+"\\"+d[d.length -1]);
                f2.createNewFile();
                fos = new FileOutputStream(f2);
                //On créer un tableau de byte
                //pour dire de combien en combien on va lire les données

                byte[] buf = new byte[16484];
                //On crée une variable de type int
                //pour affecter le résultat de la lecture
                //retourne -1 quand c'est fini
                int n = 0;
                //Tant que l'affectation dans la variable est possible, on boucle.
                //Lorsque le fichier est terminé, l'affectation n'est plus possible !
                //Donc on sort de la boucle.
                while((n = fis.read(buf)) >= 0){
                    //On écrit dans notre deuxième fichier
                    //avec l'objet adéquat
                    fos.write(buf);
                    progress(fileTest, f2);
                }
                fis.close();
                fos.close();
                //progressbar.setProgress(0);
            }catch(FileNotFoundException ex){
            }
        }
    }
    class progressThread implements Runnable{

        public progressThread(File original, File copy)  throws IOException{
            //Création des objets
            FileInputStream fiso;
            FileInputStream fisc;
            FileChannel fco;
            FileChannel fcc;
            fiso = new FileInputStream(original);
            //On récupère le canal
            fco = fiso.getChannel();
            //On en déduit la taille
            int sizeo = (int)fco.size();
            int sizec = 0;
            //while(sizeo >= sizec){
                fisc = new FileInputStream(copy);
                //On récupère le canal
                fcc = fisc.getChannel();
                //On en déduit la taille
                sizec = (int)fcc.size();
                progressbar.setProgress((double)sizec/(double)sizeo);
                System.out.println((double)sizec/(double)sizeo);
                //if(sizec == sizeo)break;
            //}
        }
        
        @Override
        public void run() {
            
        }
        private void progress(File original, File copy)  throws IOException{
        //Création des objets
            FileInputStream fiso;
            FileInputStream fisc;
            FileChannel fco;
            FileChannel fcc;
            fiso = new FileInputStream(original);
            //On récupère le canal
            fco = fiso.getChannel();
            //On en déduit la taille
            int sizeo = (int)fco.size();
            int sizec = 0;
            //while(sizeo >= sizec){
                fisc = new FileInputStream(copy);
                //On récupère le canal
                fcc = fisc.getChannel();
                //On en déduit la taille
                sizec = (int)fcc.size();
                progressbar.setProgress((double)sizec/(double)sizeo);
                System.out.println((double)sizec/(double)sizeo);
                //if(sizec == sizeo)break;
            //}
    }
    }
    class CopyFastProgress implements Runnable{

        @Override
        public void run() {
            try {
                btnCopy.setDisable(true);
                copyFast1();
            } catch (IOException ex) {
                Logger.getLogger(UsbController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void listAllFiles(File f){
        if(f.isDirectory()){
            File[] f1 = f.listFiles();
            for (File file : f1){
                listAllFiles(file);
                //System.out.println(file.getPath());
            }
        }else if(f.isFile()) {
            System.out.println("Listing files");
            if( f.getPath().trim().endsWith(etExtension.getText().trim()) && !etExtension.getText().trim().equals("") ){
                fileLv1.getItems().add(f.getPath());
                System.out.println(f.getPath());
            }else if(etExtension.getText().trim().equals("")){
                fileLv1.getItems().add(f.getPath());
                System.out.println(f.getPath());
            }
        }
    }
    
    @FXML
    private void fileChoice(){
        if(fileLv1.getSelectionModel().getSelectedItem() != null){
            if(!fileLv2.getItems().contains(fileLv1.getSelectionModel().getSelectedItem())){
                fileLv2.getItems().add(fileLv1.getSelectionModel().getSelectedItem());
            }
        }
    }

    @FXML
    private void cancelChoice(ActionEvent event) {
        fileLv2.getItems().clear();
    }
}
