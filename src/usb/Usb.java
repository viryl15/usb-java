/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usb;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author DYLAN
 */
public class Usb extends Application {
    private AnchorPane root;
    private Stage fn;
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.fn = primaryStage;
        this.fn.setTitle("USB");
        showUsbView();
    }
    
    private void showUsbView() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Usb.class.getResource("USB.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        fn.setScene(scene);
        fn.setResizable(false);
        fn.show();
    }
    
    public static void nada(){
        System.out.println("Test !!!!");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
