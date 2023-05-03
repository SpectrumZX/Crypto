package ru.crypto.lesson2;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import static java.security.DrbgParameters.Capability.PR_AND_RESEED;

public class DigestAndSymmetricCrypto {

    private final static String CHARSET = "UTF-8";

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите секретное слово");
        String secretWord = scanner.nextLine();
        scanner.close();

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] wordDigest = messageDigest.digest(secretWord.getBytes(CHARSET));

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        Key key = keyGenerator.generateKey();

        IvParameterSpec ivSpec = new IvParameterSpec(getRandomBytes());

        Cipher chiper = Cipher.getInstance("AES/CBC/PKCS5Padding");
        chiper.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptWord = chiper.doFinal(secretWord.getBytes(CHARSET));

        System.out.println("Хеш слова: " + new String(wordDigest, CHARSET));
        System.out.println("Зашифрованное слово: " + new String(encryptWord, CHARSET));

        chiper.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decryptWord = chiper.doFinal(encryptWord);

        System.out.println("Расшифрованное слово: " + new String(decryptWord, CHARSET));
    }

    private static byte[] getRandomBytes() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("DRBG",
                DrbgParameters.instantiation(256, PR_AND_RESEED, null));
        byte[] sec_bytes = new byte[16];
        secureRandom.nextBytes(sec_bytes);
        return sec_bytes;
    }

}
