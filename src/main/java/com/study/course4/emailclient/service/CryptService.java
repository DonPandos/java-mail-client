package com.study.course4.emailclient.service;

import com.study.course4.emailclient.crypt.DESCrypt;
import com.study.course4.emailclient.crypt.RSACrypt;
import javafx.util.Pair;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.study.course4.emailclient.controller.MainFormController.mailSession;

public class CryptService {
    @SneakyThrows
    public static Pair<String, List<DataSource>> encrypt(String content, List<DataSource> attachments, String rsaPubKeyString) {
        String encryptedContent;
        List<DataSource> encryptedAttachments = new ArrayList<>();

        String base64content = Base64.getEncoder().encodeToString(content.getBytes());

        SecretKey desKey = DESCrypt.generateKey();
        DESCrypt desCrypt = new DESCrypt(desKey);
        encryptedContent = desCrypt.encrypt(base64content);

        for(DataSource attachment : attachments) {
            String attCont = new BufferedReader(
                     new InputStreamReader(attachment.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String encryptedAttCont = desCrypt.encrypt(Base64.getEncoder().encodeToString(attCont.getBytes()));
            DataSource encryptedAttachment = new ByteArrayDataSource(encryptedAttCont.getBytes(), "text/html");
            ((ByteArrayDataSource) encryptedAttachment).setName(attachment.getName());
            encryptedAttachments.add(encryptedAttachment);
        }

        PublicKey rsaPubKey = RSACrypt.getPublicKeyFromString(rsaPubKeyString);

        byte[] encryptedDesKeyString = RSACrypt.encrypt(desKey.getEncoded(), rsaPubKey);
        DataSource encryptedDesKeyDataSource = new ByteArrayDataSource(encryptedDesKeyString, "text/html");
        ((ByteArrayDataSource) encryptedDesKeyDataSource).setName("encdeskey");
        encryptedAttachments.add(encryptedDesKeyDataSource);

        return new Pair<>(encryptedContent, encryptedAttachments);
    }

    @SneakyThrows
    public static Pair<String, List<DataSource>> decrypt(String content, List<DataSource> attachments, String fromEmail) {
        File file = new File("src/main/java/com/study/course4/emailclient/files/" + mailSession.getEmail() + "/crypt/rsapriv/" + fromEmail);
        FileInputStream in = new FileInputStream(file);
        byte[] privRsaKeyBytes = new byte[in.available()];
        in.read(privRsaKeyBytes);
        PrivateKey privateKey = RSACrypt.getPrivateKeyFromByteArray(privRsaKeyBytes);
        DataSource encDesKeyDataSource = attachments.stream()
                .filter(dataSource -> dataSource.getName().equals("encdeskey"))
                .findAny()
                .get();
        attachments.remove(encDesKeyDataSource);
        InputStream inputStream = encDesKeyDataSource.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        os.write(buffer);
        byte[] encDesKey = os.toByteArray();

        byte[] base64desKey = RSACrypt.decrypt(encDesKey, privateKey);
        SecretKey desKey = new SecretKeySpec(base64desKey, "DES");
        DESCrypt desCrypt = new DESCrypt(desKey);
        String decryptedContent = new String(Base64.getDecoder().decode(desCrypt.decrypt(content)));
        List<DataSource> decryptedAttachments = new ArrayList<>();
        for(DataSource attachment : attachments) {
            String attCont = new BufferedReader(
                    new InputStreamReader(attachment.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String decryptedAttachContent = new String(Base64.getDecoder().decode(desCrypt.decrypt(attCont)));
            DataSource decryptedAttach = new ByteArrayDataSource(decryptedAttachContent.getBytes(), "text/html");
            ((ByteArrayDataSource) decryptedAttach).setName(attachment.getName());
            decryptedAttachments.add(decryptedAttach);
        }
        return new Pair<>(decryptedContent, decryptedAttachments);
    }
}