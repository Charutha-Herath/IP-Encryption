package com.example.encryption3;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

        Socket socket = new Socket(txtEnterIpAddress.getText(), 8000);
        System.out.println("Connected to server");

        txtAreaServerResponse.appendText("Connected to server\nPlease provide decryption key");


        // Receive challenge from server
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String challenge = reader.readLine();
        System.out.println("Server : " + challenge);


        btnSend.setOnAction(event -> {

            PrintWriter writer = null;
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);

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
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void btnSendOnAction(ActionEvent actionEvent) {

    }


}