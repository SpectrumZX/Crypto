package ru.crypto.finProject;

import java.util.Scanner;

public class Utils {
    public static String getParam(Scanner sc, String message) {
        System.out.println(message);
        String data = sc.nextLine();
        if (data == null || data.isBlank()) {
            System.out.println("Параметр не дожен быть пустым");
            getParam(sc, message);
        }
        return data;
    }
}
