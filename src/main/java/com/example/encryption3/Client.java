package com.example.encryption3;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends Application {


    public TextField txtEnterIpAddress;
    public JFXButton btnConnect;
    public TextArea txtAreaServerResponse;
    public TextField txtDecryptionKey;
    public JFXButton btnSend;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void initialize(){
        btnSend.setDisable(true);
        txtDecryptionKey.setDisable(true);

 /*       txtEnterIpAddress.setText("192.168.252.57");
//        txtAreaServerResponse.appendText("Connected to server\n" +
//                "Server : Please provide decryption key");

        //txtDecryptionKey.setText("XF1cmdeBDvzuGMMSQaYOeQ==");

        txtAreaServerResponse.appendText("Connected to server\n" +
                "Server : Please provide decryption key\n" +
                "Server response: Access denied\n" +
                "Access denied");*/
    }

    public void btnConnectOnAction(ActionEvent actionEvent) throws IOException {

        /*Socket socket = new Socket(txtEnterIpAddress.getText(), 8000);
        System.out.println("Connected to server");*/
        Socket socket = null;

        try{
            socket = new Socket(txtEnterIpAddress.getText(), 8000);
            System.out.println("Connected to server");
        } catch (Exception e){
            txtAreaServerResponse.appendText("Wrong IP Address");
        }

        txtAreaServerResponse.appendText("Connected to server\nPlease provide decryption key");
        btnSend.setDisable(false);
        txtDecryptionKey.setDisable(false);

        // Receive challenge from server

        if (socket==null){
            txtAreaServerResponse.appendText("Wrong IP Address");


            try {

            new Alert(Alert.AlertType.WARNING,"Wrong IP Address").showAndWait();
                Thread.sleep(5000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            System.exit(0);
            //new Timeline(new KeyFrame(Duration.seconds(2), event -> System.exit(0))).play();


        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String challenge = reader.readLine();
        System.out.println("Server : " + challenge);


        Socket finalSocket = socket;
        btnSend.setOnAction(event -> {

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(finalSocket.getOutputStream(), true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writer.println(txtDecryptionKey.getText());
            txtDecryptionKey.setText("");

            // Receive response from server
            String response = "No";
            try {
                response = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Server response: " + response);

            if (response.equals("Access granted")) {
                System.out.println("Connected to server successfully");
                txtAreaServerResponse.appendText("\nConnected to server successfully");
                // Continue with the connection
            } else {
                System.out.println("\nAccess denied");
                txtAreaServerResponse.appendText("\n\nAccess denied");
                try {
                    finalSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            btnSend.setDisable(true);
        });
        btnConnect.setDisable(true);
    }

    public void btnSendOnAction(ActionEvent actionEvent) {

    }

    private void showAlertAndExit(String title, String message) {
        // Show the alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        // Use Timeline to delay the application exit without blocking the UI thread
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> System.exit(0)));
        timeline.play();
    }

}