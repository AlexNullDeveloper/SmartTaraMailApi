package controller;

import ApplicationExceptions.AllreadyInFileException;

import java.io.*;

/**
 * Created by ZXCASD on 06.07.2016.
 */

public class MailAdder {
    static boolean isOk = true;

    public static boolean addMailIntoFile(String mail) {
        final String mailsPath = "C:\\mail\\csv";
        File dirWithMails = new File(mailsPath);

        if (!dirWithMails.exists()) {
            System.out.println("creating folders");

            try {
                dirWithMails.mkdirs();
            } catch (SecurityException e) {
                isOk = false;
                e.printStackTrace();
            }
        }

        writeMailToFile(mail, mailsPath);

        return isOk;
    }

    private static void writeMailToFile(String mail, String mailsPath) {

        String pathToFile = mailsPath + "\\mails.csv";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile))) {
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                String[] temp = str.split(";");
                if (mail.equals(temp[0])) {
                    System.out.println("mail = " + mail);
                    System.out.println("temp[0] = " + temp[0]);
                    throw new AllreadyInFileException();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(pathToFile, true)) {
            fileWriter.write(mail);
            fileWriter.write(";");
            fileWriter.write("0");
            fileWriter.write(";");
            fileWriter.write("\n");
        } catch (IOException e) {
            isOk = false;
            e.printStackTrace();
        }
    }
}
