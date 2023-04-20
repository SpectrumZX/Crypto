package ru.crypto;

import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class Lesson1 {

    private static String BASIC_MODE = "basic";
    private static String SECURE_MODE = "secure";
    private static String[] PREDICTIONS = new String[]{"У вас сегодня будет удача в делах!", "Сегодня хороший день для саморазвития!"};
    private static Random basicRandom = new Random();
    private static SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя пользователя");
        String userName = scanner.nextLine();
        System.out.println("Введите способ получения прогноза (basic/secure)");
        String inputMode = scanner.nextLine();

        int i = 0;

        if (BASIC_MODE.equals(inputMode)) {
            i = basicRandom.nextInt(PREDICTIONS.length);
            printPredictionText(userName, i);
        } else if (SECURE_MODE.equals(inputMode)) {
            i = secureRandom.nextInt(PREDICTIONS.length);
            printPredictionText(userName, i);
        } else System.out.println("Неизвестный способ получения прогноза");

    }

    private static void printPredictionText(String userName, int i) {
        String message = userName + ". " + PREDICTIONS[i];
        System.out.println(message);
    }
}
