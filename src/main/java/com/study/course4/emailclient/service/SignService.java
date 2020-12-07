package com.study.course4.emailclient.service;

import lombok.SneakyThrows;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class SignService {

    @SneakyThrows
    public static void createSign(String content, List<DataSource> attachments) {
        String signContent = String.valueOf(content);
        for(DataSource attachment : attachments) {
            String attCont = new BufferedReader(
                    new InputStreamReader(attachment.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            signContent += attCont;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] hash = messageDigest.digest(Base64.getEncoder().encode(signContent.getBytes()));
        Signature signature = Signature.getInstance("SHA1withDSA");
        SecureRandom secureRandom = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        signature.initSign(keyPair.getPrivate(), secureRandom);
        signature.update(hash);
        byte[] sign = Base64.getEncoder().encode(signature.sign());
        byte[] publicKeyBytes = Base64.getEncoder().encode(keyPair.getPublic().getEncoded());
        byte[] data = new byte[sign.length + publicKeyBytes.length];
        System.arraycopy(publicKeyBytes, 0, data, 0, publicKeyBytes.length);
        System.arraycopy(sign, 0, data, publicKeyBytes.length, sign.length);
        DataSource signDataSource = new ByteArrayDataSource(data, "text/plain");
        ((ByteArrayDataSource) signDataSource).setName("pubandsign");
        attachments.add(signDataSource);
    }

    @SneakyThrows
    public static boolean checkSign(String content, List<DataSource> attachments) {
        DataSource pubandsignDataSource= attachments.stream()
                .filter(attachment -> attachment.getName().equals("pubandsign"))
                .findAny()
                .get();
        attachments.remove(pubandsignDataSource);
        InputStream is = pubandsignDataSource.getInputStream();
        byte[] pubKey = new byte[592];
        is.read(pubKey);
        byte[] sign = new byte[is.available()];
        is.read(sign);


        String signContent = String.valueOf(content);
        for(DataSource attachment : attachments) {
            String attCont = new BufferedReader(
                    new InputStreamReader(attachment.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            signContent += attCont;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        byte[] hash = messageDigest.digest(Base64.getEncoder().encode(signContent.getBytes()));
        Signature signature = Signature.getInstance("SHA1withDSA");
        KeyFactory rsaKf = KeyFactory.getInstance("DSA"); // or "EC" or whatever
        PublicKey publicKey = rsaKf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey)));
        signature.initVerify(publicKey);
        signature.update(hash);
        boolean result = signature.verify(Base64.getDecoder().decode(sign));
        return result;
    }
}
