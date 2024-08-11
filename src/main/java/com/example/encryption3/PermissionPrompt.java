package com.example.encryption3;

import java.util.Scanner;

public class PermissionPrompt {
    public static boolean askForPermission() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you give permission to retrieve the IP address? (yes/no)");
        String response = scanner.nextLine();
        return response.equalsIgnoreCase("yes");
    }
}
