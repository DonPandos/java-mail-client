package com.study.course4.emailclient.service;

import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

public class AccountService {
    @SneakyThrows
    public static List<String> getAccounts() {
        return Files.readAllLines(new File("src/main/java/com/study/course4/emailclient/files/accounts.txt").toPath());
    }

    public static String getAccountByEmail(String email) {
        List<String> accounts = getAccounts();
        for(String account : accounts) {
            if(account.substring(0, account.indexOf(" ")).equals(email)) return account;
        }
        return null;
    }

    @SneakyThrows
    public static void deleteAccount(String accountToDelete) {
        List<String> accounts = getAccounts();
        accounts.remove(accountToDelete);
        FileWriter fw = new FileWriter("src/main/java/com/study/course4/emailclient/files/accounts.txt", false);
        BufferedWriter bw = new BufferedWriter(fw);
        for(String account : accounts) {
            bw.write(account + "\n");
        }
        bw.close();
    }
}
