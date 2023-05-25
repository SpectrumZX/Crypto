package ru.crypto.finProject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Locale;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.crypto.finProject.Utils.getParam;

public class Decryptor {
    public static void main(String[] args) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException {

        Scanner scanner = new Scanner(System.in);
        String keystorePath = getParam(scanner, "Введите полный путь до keystore");
        String typeKeyStore = getParam(scanner, "Введите тип keystore JKS/JCEKS");
        String password = getParam(scanner, "Введите пароль keystore");
        String keyAlias = getParam(scanner, "Введите имя ключа в keystore");
        byte[] encryptedText = Base64.getDecoder().decode(getParam(scanner, "Введите зашифрованный текст"));
        byte[] signature = Base64.getDecoder().decode(getParam(scanner, "Введите цифровую подпись"));
        scanner.close();

        KeyStore keyStore = KeyStore.getInstance(typeKeyStore.toUpperCase());
        keyStore.load(new FileInputStream(keystorePath), password.toCharArray());
        Key key = keyStore.getKey(keyAlias, password.toCharArray());

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        System.out.println("Расшифрованный текст: " + new String(decryptedText, UTF_8));
        validateSignature(encryptedText, keyStore.getCertificate(keyAlias), signature);
    }

    private static void validateSignature(byte[] encryptedText, Certificate cert, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        try {
            sign.initVerify(cert);
            sign.update(encryptedText);
            if (!sign.verify(signature)) {
                System.out.println("Неверная электронная подпись!");
            }
        } catch (SignatureException e) {
            System.out.println("Ошибка валидации цифровой подписи!");
        }

    }
}
