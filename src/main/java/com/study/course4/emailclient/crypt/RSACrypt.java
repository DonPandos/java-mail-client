package com.study.course4.emailclient.crypt;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSACrypt {

    @SneakyThrows
    public static String generatePair(String fromEmail, String toEmail) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        String pubKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        byte[] privKeyString = Base64.getEncoder().encode(keyPair.getPrivate().getEncoded());
        File file = new File("src/main/java/com/study/course4/emailclient/files/" + fromEmail + "/crypt/rsapriv/" + toEmail);
        file.getParentFile().mkdirs();
        //file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        out.write(privKeyString);
        out.close();

        return pubKeyString;
    }

    @SneakyThrows
    public static boolean isPublicRsaKey(String rsaPubKeyString) {
        try {
            KeyFactory rsaKf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
            PublicKey publicKey = rsaKf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPubKeyString)));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SneakyThrows
    public static PublicKey getPublicKeyFromString(String rsaPubKeyString) {
        KeyFactory rsaKf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PublicKey publicKey = rsaKf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPubKeyString)));
        return publicKey;
    }

    @SneakyThrows
    public static PrivateKey getPrivateKeyFromString(String rsaPrivKeyString) {
        KeyFactory rsaKf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PrivateKey privateKey = rsaKf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivKeyString)));
        return privateKey;
    }

    @SneakyThrows
    public static PrivateKey getPrivateKeyFromByteArray(byte[] rsaPrivKeyBytes) {
        KeyFactory rsaKf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PrivateKey privateKey = rsaKf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivKeyBytes)));

        return privateKey;
    }

    @SneakyThrows
    public static byte[] encrypt(byte[] plainText, PublicKey publicKey) {
        byte[] text = Base64.getEncoder().encode(plainText);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] rsaEncrypted = cipher.doFinal(text);
        return rsaEncrypted;
    }

    @SneakyThrows
    public static byte[] decrypt(byte[] encryptedText, PrivateKey privateKey) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] rsaDecrypted = cipher.doFinal(encryptedText);
        return Base64.getDecoder().decode(rsaDecrypted);
    }
//    @
}
