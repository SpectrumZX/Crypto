package ru.crypto.lesson5;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KeystoreEncrypt {
    public static void main(String[] args) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла хранилища");
        String keystoreFileName = scanner.nextLine();
        System.out.println("Введите длину ключа RSA");
        int keyLength = scanner.nextInt();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);

        CertAndKeyGen generator = new CertAndKeyGen("RSA", "SHA256withRSA");
        generator.generate(keyLength);
        Key privateKey = generator.getPrivateKey();
        X509Certificate cert = generator.getSelfCertificate(new X500Name("CN=ROOT"), (long)365*24*3600);
        X509Certificate[] certChain = new X509Certificate[1];
        certChain[0] = cert;

        keyStore.setKeyEntry("testKey", privateKey, "keyPass".toCharArray(), certChain);
        keyStore.store(new FileOutputStream(keystoreFileName), "storePass".toCharArray());

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
        byte[] encryptText = cipher.doFinal("Java".getBytes(UTF_8));

        FileOutputStream outputStream = new FileOutputStream("encryptTextFile.bin");
        outputStream.write(encryptText);
        outputStream.close();
    }
}
