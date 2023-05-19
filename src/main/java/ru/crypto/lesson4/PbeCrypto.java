package ru.crypto.lesson4;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

public class PbeCrypto {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите пароль");
        String password = scanner.nextLine();
        if (password!=null && password.isBlank()) {
            System.out.println("Пароль не должен быть пустым");
            return;
        }

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), "SaltSaltSalt".getBytes(StandardCharsets.UTF_8), Short.MAX_VALUE, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey key = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");

        byte[] encryptedText = encodeAndPrint(key, "Java");
        String decryptWord = decodeAndPrint(key, encryptedText);
    }

    private static byte[] encodeAndPrint(SecretKey key, String secretWord) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if(key!=null) {
            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedWord = encryptCipher.doFinal(secretWord.getBytes(StandardCharsets.UTF_8));
            System.out.println("Зашифрованное слово: " + Base64.getEncoder().encodeToString(encryptedWord));
            return encryptedWord;
        }
        else {
            System.out.println("encodeAndPrint() аргумент key пустой");
            return null;
        }
    }

    private static String decodeAndPrint(SecretKey key, byte[] encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        if(key!=null && encryptedText!=null) {
            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedText = encryptCipher.doFinal(encryptedText);
            String decryptWord = new String(decryptedText, "UTF-8");
            System.out.println("Расшифрованное слово: " + decryptWord);
        }
        else System.out.println("decodeAndPrint() пустой аргумент");
        return null;
    }

}
