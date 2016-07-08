package Launcher;

import presenter.MailFrame;

import javax.swing.*;

/**
 * Created by a.talismanov on 07.07.2016.
 */
public class Launcher {

    private static String emailFrom;
    private static String password;
    private final static boolean SHOW_SEND_BUTTON = true;
    private final static boolean DONT_SHOW_SEND_BUTTON = false;

    public static String getEmailFrom() {
        return emailFrom;
    }

    public static String getPassword() {
        return password;
    }

    public static void main(String[] args) {

        if (args.length == 2) {
            emailFrom = args[0];
            password = args[1];
            SwingUtilities.invokeLater(() -> new MailFrame("Работа с почтой", SHOW_SEND_BUTTON));
        } else {
            SwingUtilities.invokeLater(() -> new MailFrame("Работа с почтой", DONT_SHOW_SEND_BUTTON));
        }
    }
}
