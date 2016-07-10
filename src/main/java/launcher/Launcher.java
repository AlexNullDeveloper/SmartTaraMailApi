package launcher;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import presenter.MailFrame;

import javax.swing.*;

/**
 * @author a.talismanov on 07.07.2016.
 */
public class Launcher {

    private static String emailFrom;
    private static String password;
    private final static boolean SHOW_SEND_BUTTON = true;
    private final static boolean DONT_SHOW_SEND_BUTTON = false;
    public static Logger logger = Logger.getLogger(Launcher.class.getName());


    public static String getEmailFrom() {
        return emailFrom;
    }

    public static String getPassword() {
        return password;
    }

    public static void main(String[] args) {

        BasicConfigurator.configure();

        if (args.length == 2) {
            logger.debug("args.length == 2");
            emailFrom = args[0];
            password = args[1];
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    logger.error("Exception", e);
                }
                new MailFrame("Работа с почтой", SHOW_SEND_BUTTON);
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    logger.error("Exception", e);
                }
                new MailFrame("Работа с почтой", DONT_SHOW_SEND_BUTTON);
            });
        }
    }
}
