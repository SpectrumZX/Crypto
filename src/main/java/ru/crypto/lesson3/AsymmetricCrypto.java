package ru.crypto.lesson3;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AsymmetricCrypto {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException {

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(4096);
        KeyPair pair = keyGenerator.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
        byte[] encryptText = cipher.doFinal("Java".getBytes(UTF_8));
        System.out.println("Зашифрованное слово: " + Base64.getEncoder().encodeToString(encryptText));

        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(pair.getPrivate());
        sign.update(encryptText);
        byte[] digitalSignature = sign.sign();
        System.out.println("Подпись: " + Base64.getEncoder().encodeToString(digitalSignature));

        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
        byte[] decryptedText = cipher.doFinal(encryptText);
        System.out.println("Расшифрованное слово: " + new String(decryptedText, UTF_8));

        sign.initVerify(pair.getPublic());
        sign.update(encryptText);
        System.out.println("Верификация подписи: " + sign.verify(digitalSignature));
    }
}
