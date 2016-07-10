package workers;

import appexceptions.AllreadyInFileException;
import appexceptions.CantMakeDirsException;
import launcher.Launcher;

import java.io.*;

/**
 * @author a.talismanov on 06.07.2016.
 */

public class MailFileWorker {
    private static boolean isOk = true;

    public static boolean addMailIntoFile(String mail) {
        final String mailsPath = "C:\\mail\\csv";
        File dirWithMails = new File(mailsPath);

        if (!dirWithMails.exists()) {
            Launcher.logger.debug("creating folders");

            try {
                dirWithMails.mkdirs();
            } catch (SecurityException e) {
                Launcher.logger.fatal("Can't make directories at " + mailsPath, e);
                throw new CantMakeDirsException("Can't make directories at " + mailsPath);
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
                    Launcher.logger.debug("mail = " + mail);
                    Launcher.logger.debug("temp[0] = " + temp[0]);
                    throw new AllreadyInFileException();
                }
            }
        } catch (FileNotFoundException e) {
            Launcher.logger.fatal("FileNotFoundException", e);
        } catch (IOException e) {
            Launcher.logger.fatal("IOException", e);
        }

        try (FileWriter fileWriter = new FileWriter(pathToFile, true)) {
            fileWriter.write(mail);
            fileWriter.write(";");
            fileWriter.write("0");
            fileWriter.write(";");
            fileWriter.write("\n");
        } catch (IOException e) {
            Launcher.logger.fatal("IOException", e);
            isOk = false;
        }
    }
}
