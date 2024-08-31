package com.example.encryption3;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.BadPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

import static java.lang.String.copyValueOf;

public class Server extends Application {
    public JFXButton btnNoId;
    public JFXButton btnYesId;
    public Label lblFixedLocalhost;
    public Label lblFixedEncryptedIp;
    public Label lblLocalhost;
    public Label lblEncryptedIp;
    public AnchorPane paneFixedPrivateKey;
    public Label lblPrivateKey;
    public JFXButton btnCopy;

    private Stage stage;
    @Override
    public void start(Stage stage) throws IOException  {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    public void initialize(){
        lblFixedLocalhost.setVisible(false);
        lblFixedEncryptedIp.setVisible(false);
        paneFixedPrivateKey.setVisible(false);


        btnNoId.setOnAction(this::btnNoOnAction);
    }

    /*public void btnYesOnAction(ActionEvent actionEvent) throws Exception{

        String ipAddress = IpReader.getIPAddress(); // server's IP address
        SecretKeySpec secretKey = (SecretKeySpec) KeyGen.generateKey();
        String keyBytes1 = KeyGen.getKeyBytes(secretKey);

        System.out.println("The Key is : "+keyBytes1);
        String encryptedIpAddress = Encrypter.encrypt(ipAddress, secretKey);
        System.out.println("Encrypted IP address: " + encryptedIpAddress);

        String decryptedIpAddress1 = Decrypter.decrypt(encryptedIpAddress, secretKey);
        System.out.println("Decrypted IP address: " + decryptedIpAddress1);



        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Server started. Waiting for client connection...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        // Send challenge to client
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("Please provide decryption key");

        // Receive decryption key from client
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String decryptionKey = reader.readLine();

        System.out.println("Decryption key: " + decryptionKey);

        String base64Key = decryptionKey;

        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        try {
            String decryptedIpAddress = Decrypter.decrypt(encryptedIpAddress, secretKeySpec);

            if (decryptedIpAddress!= null && decryptedIpAddress.equals(ipAddress)) {
                System.out.println("Decryption successful. Granting access to client");
                writer.println("Access granted");
                // Continue with the connection
            } else {
                System.out.println("Decryption failed. Terminating connection");
                socket.close();
            }
        } catch (BadPaddingException e) {
            // Handle bad padding exception
            System.out.println("Error: Bad padding. Decryption failed.");
            writer.println("Access denied");
            socket.close();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }*/
    public void btnYesOnAction(ActionEvent actionEvent) throws Exception {
        String ipAddress = IpReader.getIPAddress(); // server's IP address

        SecretKeySpec secretKey = (SecretKeySpec) KeyGen.generateKey();
        String keyBytes1 = KeyGen.getKeyBytes(secretKey);

        System.out.println("The Key is : " + keyBytes1);
        String encryptedIpAddress = Encrypter.encrypt(ipAddress, secretKey);
        System.out.println("Encrypted IP address: " + encryptedIpAddress);

        String decryptedIpAddress1 = Decrypter.decrypt(encryptedIpAddress, secretKey);
        System.out.println("Decrypted IP address: " + decryptedIpAddress1);

        btnYesId.setDisable(true);
        btnNoId.setDisable(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> lblFixedLocalhost.setVisible(true)),
                new KeyFrame(Duration.seconds(0), event -> lblLocalhost.setText(ipAddress)),
                new KeyFrame(Duration.seconds(2), event -> lblFixedEncryptedIp.setVisible(true)),
                new KeyFrame(Duration.seconds(2), event -> lblEncryptedIp.setText("E n c r y p t i n g . .")),
                new KeyFrame(Duration.seconds(4), event -> lblEncryptedIp.setText(encryptedIpAddress)),
                new KeyFrame(Duration.seconds(5.5), event -> paneFixedPrivateKey.setVisible(true)),
                new KeyFrame(Duration.seconds(5.5), event -> lblPrivateKey.setText(keyBytes1))
        );
        timeline.play();


        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8000)) {
                System.out.println("Server started. Waiting for client connection...");

                Socket socket = serverSocket.accept();
                System.out.println("Client connected");


                try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    writer.println("Please provide decryption key");


                    String decryptionKey = reader.readLine();
                    System.out.println("Decryption key: " + decryptionKey);

                    String base64Key = decryptionKey;
                    byte[] keyBytes = Base64.getDecoder().decode(base64Key);
                    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

                    String decryptedIpAddress = Decrypter.decrypt(encryptedIpAddress, secretKeySpec);

                    if (decryptedIpAddress != null && decryptedIpAddress.equals(ipAddress)) {
                        System.out.println("Decryption successful. Granting access to client");
                        writer.println("Access granted");
                    } else {
                        System.out.println("Decryption failed. Terminating connection");
                        writer.println("Access denied");
                    }
                } catch (BadPaddingException e) {
                    System.out.println("Error: Bad padding. Decryption failed.");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Server socket error: " + e.getMessage());
            }
        }).start();
    }

   /* public void btnYesOnAction(ActionEvent actionEvent) throws Exception {


            String ipAddress = IpReader.getIPAddress(); // server's IP address

            SecretKeySpec secretKey = (SecretKeySpec) KeyGen.generateKey();
            String keyBytes1 = KeyGen.getKeyBytes(secretKey);

            System.out.println("The Key is : "+keyBytes1);
            String encryptedIpAddress = Encrypter.encrypt(ipAddress, secretKey);
            System.out.println("Encrypted IP address: " + encryptedIpAddress);

            String decryptedIpAddress1 = Decrypter.decrypt(encryptedIpAddress, secretKey);
            System.out.println("Decrypted IP address: " + decryptedIpAddress1);



            btnYesId.setDisable(true);
            btnNoId.setDisable(true);

            //timeLine( ipAddress,encryptedIpAddress,keyBytes1);


            *//*lblFixedLocalhost.setVisible(true);
            lblLocalhost.setText(ipAddress);
            lblFixedEncryptedIp.setVisible(true);
            lblEncryptedIp.setText(encryptedIpAddress);
            paneFixedPrivateKey.setVisible(true);
            lblPrivateKey.setText(keyBytes1);*//*


            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), event -> lblFixedLocalhost.setVisible(true)),
                    new KeyFrame(Duration.seconds(0), event -> lblLocalhost.setText(ipAddress)),
                    new KeyFrame(Duration.seconds(2), event -> lblFixedEncryptedIp.setVisible(true)),
                    new KeyFrame(Duration.seconds(2), event -> lblEncryptedIp.setText("E n c r y p t i n g . .")),
                    new KeyFrame(Duration.seconds(4), event -> lblEncryptedIp.setText(encryptedIpAddress)),
                    new KeyFrame(Duration.seconds(5.5), event -> paneFixedPrivateKey.setVisible(true)),
                    new KeyFrame(Duration.seconds(5.5), event -> lblPrivateKey.setText(keyBytes1))



            );
            timeline.play();



            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server started. Waiting for client connection...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            // Send challenge to client
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Please provide decryption key");

            // Receive decryption key from client
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String decryptionKey = reader.readLine();

            System.out.println("Decryption key: " + decryptionKey);

            String base64Key = decryptionKey;

            byte[] keyBytes = Base64.getDecoder().decode(base64Key);

            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            try {
                String decryptedIpAddress = Decrypter.decrypt(encryptedIpAddress, secretKeySpec);

                if (decryptedIpAddress!= null && decryptedIpAddress.equals(ipAddress)) {
                    System.out.println("Decryption successful. Granting access to client");
                    writer.println("Access granted");
                    // Continue with the connection
                } else {
                    System.out.println("Decryption failed. Terminating connection");
                    socket.close();
                }
            } catch (BadPaddingException e) {
                // Handle bad padding exception
                System.out.println("Error: Bad padding. Decryption failed.");
                writer.println("Access denied");
                socket.close();
            } catch (Exception e) {
                // Handle other exceptions
                e.printStackTrace();
            }

    }*/

    public void btnCopyOnAction(ActionEvent actionEvent) {

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(lblPrivateKey.getText());
        clipboard.setContent(content);
    }

    public void timeLine(String ipAddress, String encryptedIpAddress, String keyBytes1){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> lblFixedLocalhost.setVisible(true)),
                new KeyFrame(Duration.seconds(0), event -> lblLocalhost.setText(ipAddress)),
                new KeyFrame(Duration.seconds(2), event -> lblFixedEncryptedIp.setVisible(true)),
                new KeyFrame(Duration.seconds(2), event -> lblEncryptedIp.setText("E n c r y p t i n g . .")),
                new KeyFrame(Duration.seconds(4), event -> lblEncryptedIp.setText(encryptedIpAddress)),
                new KeyFrame(Duration.seconds(5.5), event -> paneFixedPrivateKey.setVisible(true)),
                new KeyFrame(Duration.seconds(5.5), event -> lblPrivateKey.setText(keyBytes1))



        );
        timeline.play();
    }

    public void btnNoOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }
}