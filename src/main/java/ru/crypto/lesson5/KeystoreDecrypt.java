package ru.crypto.lesson5;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KeystoreDecrypt {
    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла хранилища");
        String keystoreFileName = scanner.nextLine();

        FileInputStream outputStream = new FileInputStream("encryptTextFile.bin");
        byte[] encryptedText = outputStream.readAllBytes();
        outputStream.close();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keystoreFileName), "storePass".toCharArray());
        Key key = keyStore.getKey("testKey", "keyPass".toCharArray());

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        System.out.println("Расшифрованное слово: " + new String(decryptedText, UTF_8));

    }
}
