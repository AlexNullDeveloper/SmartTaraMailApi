package ru.smarttara.launcher;

import ru.smarttara.mainFrame.MainFrame;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * this class launches application
 *
 * @author a.talismanov on 07.07.2016.
 */
public class Launcher {

    /**
     * email from which send e-mails
     */
    private static String emailFrom;
    /**
     * password for email to authorize
     */
    private static String password;
    /**
     * application logger
     */
    public static Logger logger = Logger.getLogger(Launcher.class.getName());

    /**
     * default LOOK_AND_FEEL for application
     */
    public static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

    /**
     * title for mainframe
     */
    public static final String MAIN_FRAME_TITLE = "Основное меню";

    /**
     * database to display in title
     */
    public static final String DATABASE_TO_DISPLAY = "Heroku PostgreSQL";


    /**
     * Returns email from which you gonna send messages
     *
     * @return email String
     */
    public static String getEmailFrom() {
        return emailFrom;
    }

    /**
     * returns password which was entered as argument
     *
     * @return password String
     */
    public static String getPassword() {
        return password;
    }

    /**
     * @params args commandline parameters
     * first is EmailFrom
     * second is password for Email
     */
    public static void main(String[] args) {

        BasicConfigurator.configure();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(LOOK_AND_FEEL);
            } catch (Exception e) {
                logger.error("Exception when trying to set LookAndFeel", e);
            }
            if (args.length == 2) {
                emailFrom = args[0];
                password = args[1];
            }
            new MainFrame(MAIN_FRAME_TITLE, DATABASE_TO_DISPLAY);
        });
    }
}
