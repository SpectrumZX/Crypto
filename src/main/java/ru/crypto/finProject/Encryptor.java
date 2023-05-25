package ru.crypto.finProject;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.DrbgParameters.Capability.PR_AND_RESEED;
import static ru.crypto.finProject.Utils.getParam;

public class Encryptor {

    private static final String BASIC_MODE = "basic";
    private static final String SECURE_MODE = "secure";
    private static final String[] KEYSTORE_TYPE = new String[]{"JKS", "JCEKS"};
    private static final String KEY_ALIAS = "testKey";
    public static void main(String[] args) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, SignatureException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnrecoverableKeyException {

        Scanner scanner = new Scanner(System.in);
        String randomType = getParam(scanner, "Введите способ выбора keystore Basic/Secure");
        String keyStoreType = getKeystoreType(randomType);
        if(keyStoreType==null) {
            System.out.println("Введен неизвестный способ выбора keystore");
            return;
        }
        String password = getParam(scanner, "Введите пароль для keystore и ключа");
        String text = getParam(scanner, "Введите текст для шифрования");
        scanner.close();

        KeyStore keyStore = generateAndSaveKeys(keyStoreType, password);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyStore.getCertificate(KEY_ALIAS).getPublicKey());
        byte[] encryptText = cipher.doFinal(text.getBytes(UTF_8));

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign((PrivateKey) keyStore.getKey(KEY_ALIAS, password.toCharArray()));
        signature.update(encryptText);
        byte[] digitalSignature = signature.sign();

        System.out.println("Тип keystore: " + keyStoreType);
        System.out.println("Имя ключа: " + KEY_ALIAS);
        System.out.println("Зашифрованное слово: " + Base64.getEncoder().encodeToString(encryptText));
        System.out.println("Подпись: " + Base64.getEncoder().encodeToString(digitalSignature));

    }

    public static KeyStore generateAndSaveKeys(String keyStoreType, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {

        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        CertAndKeyGen generator = new CertAndKeyGen("RSA", "SHA256withRSA");
        generator.generate(2048);
        Key privateKey = generator.getPrivateKey();
        X509Certificate cert = generator.getSelfCertificate(new X500Name("CN=ROOT"), (long)365*24*3600);
        X509Certificate[] certChain = new X509Certificate[1];
        certChain[0] = cert;

        keyStore.setKeyEntry(KEY_ALIAS, privateKey, password.toCharArray(), certChain);
        keyStore.store(new FileOutputStream("keystore."+keyStoreType.toLowerCase()), password.toCharArray());
        return keyStore;
    }

    private static String getKeystoreType(String randomType) throws NoSuchAlgorithmException {
        String keyStoreType = null;
        if (BASIC_MODE.equalsIgnoreCase(randomType)) {
            Random basicRandom = new Random();
            keyStoreType = KEYSTORE_TYPE[basicRandom.nextInt(KEYSTORE_TYPE.length)];
        } else if (SECURE_MODE.equalsIgnoreCase(randomType)) {
            SecureRandom secureRandom = SecureRandom.getInstance("DRBG",
                    DrbgParameters.instantiation(256, PR_AND_RESEED, null));
            keyStoreType = KEYSTORE_TYPE[secureRandom.nextInt(KEYSTORE_TYPE.length)];
        }
        return keyStoreType;
    }

}
