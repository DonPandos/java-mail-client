package com.study.course4.emailclient;

import com.study.course4.emailclient.configuration.MailConfiguration;
import com.study.course4.emailclient.crypt.RSACrypt;
import com.study.course4.emailclient.mail.EmailAuthenticator;
import com.study.course4.emailclient.mail.MailSession;
import com.study.course4.emailclient.service.CryptService;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Store;
import lombok.SneakyThrows;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class TestH {


    @SneakyThrows
    public static void main(String[] args) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        int sum = 0;
        for(int i = 0; i < 1000; i++) {
            kpg.initialize(1024);
            KeyPair keyPair = kpg.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            if(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()).length == 216) sum++;
            System.out.println(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()).length);
        }
        System.out.println("SUM:" + sum);
      //  byte[] privateKeyBytes= Base64.getEncoder().encode(privateKey.getEncoded());
//        System.out.println(new String(privateKeyBytes));
//        File file;
//        FileOutputStream fos = new FileOutputStream("test.test");
//        fos.write(privateKeyBytes);
//
//        FileInputStream fis = new FileInputStream("test.test");
//        byte[] readed = new byte[fis.available()];
//        fis.read(readed);
     //   System.out.println(new String(readed));
//        MailConfiguration.email = "ewrr_96@mail.ru";
//        MailConfiguration.password = "ch1nk1603ch1nk1603";
//        MailSender mailSender = MailConfiguration.getJavaMailSender();
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ewrr_96@mail.ru");
//        message.setTo("bogdant99@mail.ru");
//        message.setSubject("Hello");
//        message.setText("Login: ");
//        mailSender.send(message);


//        Properties props = new Properties();
//        props.setProperty("mail.debug", "false");
//        props.setProperty("mail.store.protocol", "imaps");
//        props.setProperty("mail.imap.ssl.enable", "true");
//        props.setProperty("mail.imap.port", "993");
//        props.setProperty("mail.imaps.connectiontimeout", "5000");
//        props.setProperty("mail.imaps.timeout", "5000");
//
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.starttls.enable", "true");
//        props.setProperty("mail.smtp.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        Authenticator auth = new EmailAuthenticator("kavun.bogdan16@gmail.com", "tipeK440");
//        Session session = Session.getDefaultInstance(props, auth);
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("kavun.bogdan16@gmail.com"));
//            message.setRecipients(
//                    Message.RecipientType.TO, InternetAddress.parse("ewrr_96@mail.ru")
//            );
//            message.setSubject("Subj");
//
//            MimeBodyPart mimeBodyPart = new MimeBodyPart();
//            mimeBodyPart.setContent("Htmlcontent", "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(mimeBodyPart);
//
//            message.setContent(multipart);
//            Transport.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String pubKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjG7n6xR0Do7ZMpYEqWThOA6OC82Lra+us+2zQ1lFgOCny/VBW7SqWubs19FbgV0DZGbMqdptCTdjfs/Q8JcsPRzYFdc9Tu0J3w0+KrWw6/GeHIja/qM1+mfo5duIOX+ynfipZ9FVOT2y7jeT/qF53cB2ElhoIbRohsH3ygerh6BP4grVbzmxeiGV8TAsK7fMIIKMEz0vvbnb5eqnb1qYtdlBX5pfk4K19tdD7/jAfy5AuScO05C6fK9hMhf1caJCm3sR0fooe5tj/AloHQQZoXtjw465O324FPIa/k6SxAij0Vbe1hlRx8LPahiegqxiUv2YN7cDezi1sZ3ArZj3QIDAQAB";
//        String privKeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMAjw9SoifuA+8dnRNddg0I598sDvoMBxQdpvrMSNwzZJjvyR6qWH84g8pjLqfsLBVfsLfWMyf4KG+IM5rNatczWzs6xRPlGO4ehZB/edB+UVQR9RS8qhdjnlsPA4TbwlAkbC8hkvR3KpGlr2Z2G5+C/crVkBU68QN7Md6DWdei9AgMBAAECgYEAhZAXEFv8VvWJkEqsIx2hIcdUBi7bHwx63zVV1/sgg3Dr0B4STXXANDTZDvf8eENn+n92T0wIGe0DBvc645B58O0HRtH/AIdyUCD+1CP9tLm9HSEVR9e3mITa0A48bwwOnkFLWxB4IFZiNAKhVkzz3W111vccjVbK7oNZgjjYo6ECQQD6NP8/4+iz1cfhRVAsv7dzZRWWO80Mw69tLJqaobdYcAkrxYwUEotsmaef0VITOw/Si/M668sRpOW2+m0cqytpAkEAxJaYz2iRt+98pfT8X98H7zUATfDzaOXQvDFYGrKTPgrHRoFmSXHC6PALzfgYNpBaIA7tvR8KjQ5AbOivenUMNQJAHnJqiTTLsW6XVyNfIULnKQSo/tIiHqaFZ2yhF6YMMJfZldkBNcQX8Uw1aK5JJYvvXfinzYi5+litl3mb43w02QJBALYNTJgOm5t9wa4icLEL5toj+BM1xwRmhuYwwAmBRBAZhS+K18G+AyUaX6QvU6T8CypTCOCx5WB5STpSRWcfC90CQCNmVqVom0OVMcPIEBqui8nEc/wQD1VWJEWFFDVMl8XTz5YXsUuUznawt8RRRSrGyz+xazwT8dXEo9CtC+JO808=";
//        KeyFactory rsaKf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
//        PublicKey publicKey = rsaKf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(pubKeyString)));
//        PrivateKey privateKey = rsaKf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privKeyString)));
//        String content = "낰\uF1CC\uF834榶殂ꪸ鸀䤞ꜗ\uE7EE뀹隡�韄�롑鋙鯥왁핆裮Ṱ䆴䆴쭼⺔幩勎罹⎼橐샆豝꾔�㥝홤\uA7D3䄞娴\uF4F9\uEFC3외ㅛ퉋\uEF60庂\uE23E\uAB10傕螷핾倯鱄摵楟ꔌꈡ鑢\uF08E\uEBE2讼";
//        //String temp = RSACrypt.encrypt(content.getBytes(), publicKey);
//       // System.out.println(temp);
//
//        System.out.println(RSACrypt.decrypt(content.getBytes(), privateKey));

//        KeyPairGenerator pairGenerator = KeyPairGenerator.getInstance("RSA");
//        pairGenerator.initialize(1024);
//        KeyPair keyPair = pairGenerator.generateKeyPair();
//        Key publicKey = keyPair.getPublic();
//        Key privateKey = keyPair.getPrivate();
//
//        final byte[] encodedPublicKeyBytes = Base64.getEncoder().encode(publicKey.getEncoded());
//        final byte[] encodedPrivateKeyBytes = Base64.getEncoder().encode(privateKey.getEncoded());
//
//        final String encPubKey = new String(encodedPublicKeyBytes);
//        final String encPrivKey = new String(encodedPrivateKeyBytes);
//
//        System.out.println(encPubKey);
//        System.out.println(encPrivKey);

//        Cipher cipher = Cipher.getInstance("RSA");
//        byte[] decPubKey = Base64.getDecoder().decode(encodedPublicKeyBytes);
//        X509EncodedKeySpec X509pubKey = new X509EncodedKeySpec(decPubKey);
//        PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(X509pubKey);
//        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//        byte[] rsaEncrypted = cipher.doFinal("ertertertertergdfgdfgdfgdfgdfgdfg".getBytes());
//        System.out.println(Arrays.toString(rsaEncrypted));
//        System.out.println(rsaEncrypted.length);
//        cipher = Cipher.getInstance("RSA");
//        byte[] decPrivKey = Base64.getDecoder().decode(encodedPrivateKeyBytes);
//        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(decPrivKey);
//        PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec);
//        cipher.init(Cipher.DECRYPT_MODE, privKey);
//        byte[] rsaDecrypted = cipher.doFinal(rsaEncrypted);
//
//        byte[] decPubKey = Base64.getDecoder().decode(encodedPublicKeyBytes);
//        X509EncodedKeySpec X509pubKey = new X509EncodedKeySpec(decPubKey);
//        PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(X509pubKey);
////        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
////        byte[] rsaEncrypted = cipher.doFinal("ertertertertergdfgdfgdfgdfgdfgdfg".getBytes());
////        System.out.println(Arrays.toString(rsaEncrypted));
//        byte[] enc = RSACrypt.encrypt("WERTYUIOP@#$%^&*()_rtyuiop[?}{}{|123124", pubKey);
//        System.out.println(enc.length);
//        byte[] decPrivKey = Base64.getDecoder().decode(encodedPrivateKeyBytes);
//        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(decPrivKey);
//        PrivateKey privKey = KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec);
//        System.out.println(RSACrypt.decrypt(enc, privKey));



//        System.out.println(new String(rsaDecrypted));
//        session.setDebug(false);
//        Store store = session.getStore("imaps");
//        store.connect("imap.gmail.com", "aleksey.borovikov.11@gmail.com", "sup1403f,");
//        Folder folderSent = store.getFolder("[Gmail]/Отправленные");
//        folderSent.open(Folder.READ_ONLY);
//        for (Folder folder : store.getDefaultFolder().list()) {
//            System.out.println("---" + folder.getName());
//            if(folder.getName().equals("[Gmail]")) {
//                for(Folder folder1 : folder.list()) {
//                    System.out.println(folder1.getName());
//                }
//            }
//            String[] attributes = ((IMAPFolder) folder).getAttributes();
//            for (String attr : attributes) {
//                System.out.println(attr);
//            }
//        }
        //
//        Properties props = new Properties();
//        props.put("mail.debug", "false");
//        props.put("mail.store.protocol", "imaps");
//        props.put("mail.imap.ssl.enable", "true");
//        props.put("mail.imap.port", 993);
//
//        Authenticator auth = new EmailAuthenticator("ewrr_96@mail.ru", "ch1nk1603ch1nk1603");
//        Session session = Session.getDefaultInstance(props, auth);
//        session.setDebug(false);
//        try {
//            Store store = session.getStore();
//
//            store.connect("imap.mail.ru", "ewrr_96@mail.ru", "ch1nk1603ch1nk1603");
//
//
//            Folder inbox = store.getFolder("inbox");
//            Folder sent = store.getFolder("Отправленные");
//            sent.open(Folder.READ_ONLY);
//
//            System.out.println(new Date());
////            List<MimeMessage> messages = Arrays.stream(sent.getMessages(1, 20)).map(message -> {
////                try {
////                    return new MimeMessage((MimeMessage) message);
////                } catch (MessagingException e) {
////                    e.printStackTrace();
////                }
////                return null;
////            }).collect(Collectors.toList()); // refreshed
//            //List<Message> messages = Arrays.asList((sent.getMessages(1, 20)));
//            for(int i = 1; i < 20; i++) {
//                MimeMessage mimeMessage = (MimeMessage) sent.getMessage(i);
//            }
//            System.out.println(new Date());
////            for (MimeMessage message : messages) {
////                System.out.println(message.getContentType());
////                MimeMessageParser mimeMessageParser = new MimeMessageParser(message).parse();
////                System.out.println(mimeMessageParser.getHtmlContent());
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
