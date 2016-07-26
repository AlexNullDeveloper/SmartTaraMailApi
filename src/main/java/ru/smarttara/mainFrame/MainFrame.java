package ru.smarttara.mainFrame;

/**
 * Created by ZXCASD on 17.07.2016.
 */

import ru.smarttara.catalogs.CatalogOfEmails;
import ru.smarttara.mailing.MailFrame;
import ru.smarttara.version.VersionFrame;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

/**
 * Основное меню приложения
 *
 * @author aleksander_talismanov
 * @version alpha 1.00 11.03.2016
 */

public class MainFrame extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static int topPointY;
    private static int leftPointX;
    private static Dimension screen;
    private static final int applicationHeight;
    private static final int applicationWidth;

    static {
        screen = Toolkit.getDefaultToolkit().getScreenSize();
        applicationHeight = screen.height * 55 / 100;
        applicationWidth = screen.width * 42 / 100;
        topPointY = screen.height / 4;
        leftPointX = screen.width / 4;
    }

    public static int getTopPointY() {
        return topPointY;
    }

    public static int getLeftPointX() {
        return leftPointX;
    }

    public static Dimension getScreen() {
        return screen;
    }



    public MainFrame(String nameOfFrame, String nameOfDB) {
        super("SmartTara. " + nameOfFrame + ". База " + nameOfDB);
        setResizable(false);

        JMenuBar mainMenuBar = new JMenuBar();

        initComponents(mainMenuBar);
        setJMenuBar(mainMenuBar);
        pack();
        setBounds(leftPointX, topPointY, applicationWidth, applicationHeight);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JMenu initCatalogMenu() {
        JMenu catalogsMenu = new JMenu("Каталоги");

        JMenuItem emailCatItem = new JMenuItem("каталог Emailов");

        emailCatItem.addActionListener(ae -> new CatalogOfEmails("Каталог электронный почт"));
        catalogsMenu.add(emailCatItem);

        JMenuItem versionItem = new JMenuItem("Версия АБС");
        versionItem.addActionListener(e -> new VersionFrame("Версия"));
        catalogsMenu.add(versionItem);

        JMenuItem exitItem = new JMenuItem("Выход");
        catalogsMenu.add(exitItem);
        exitItem.addActionListener(ae -> System.exit(0));

        return catalogsMenu;
    }

    private JMenu initHeadBookMenu() {
        JMenu headBookMenu = new JMenu("Работа с почтой");
        JMenuItem documentsReestrItem = new JMenuItem("Записать или отправить");
        headBookMenu.add(documentsReestrItem);
        documentsReestrItem.addActionListener(ae -> new MailFrame("Запись и отправка почты"));

        return headBookMenu;
    }

    private void initComponents(JMenuBar menuBar) {
        menuBar.add(initCatalogMenu());
        menuBar.add(initHeadBookMenu());
    }
}
