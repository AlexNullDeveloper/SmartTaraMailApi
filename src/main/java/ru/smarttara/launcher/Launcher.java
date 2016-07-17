package ru.smarttara.launcher;

import ru.smarttara.mainFrame.MainFrame;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * @author a.talismanov on 07.07.2016.
 */
public class Launcher {

    private static String emailFrom;
    private static String password;
    public static Logger logger = Logger.getLogger(Launcher.class.getName());

    public static String getEmailFrom() {
        return emailFrom;
    }

    public static String getPassword() {
        return password;
    }

    public static void main(String[] args) {

        BasicConfigurator.configure();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                logger.error("Exception", e);
            }
            if (args.length == 2) {
                logger.debug("args.length == 2");
                emailFrom = args[0];
                password = args[1];
            }
            new MainFrame("Основное меню","Heroku PostgreSQL");
        });
    }
}
