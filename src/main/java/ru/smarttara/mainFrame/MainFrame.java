package ru.smarttara.mainFrame;

/**
 * Created by ZXCASD on 17.07.2016.
 */

import ru.smarttara.catalogs.CatalogOfEmails;
import ru.smarttara.presenter.MailFrame;
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
    private static final int APPLICATION_HEIGHT = 768;
    private static final int APPLICATION_WIDTH = 1024;
    private int topPointY;
    private int leftPointX;
    private static String sysdate = null;

    public MainFrame(String nameOfFrame, String nameOfDB) {
        super("SmartTara. " + nameOfFrame + ". База " + nameOfDB);
        setResizable(false);

        JMenuBar mainMenuBar = new JMenuBar();

        initComponents(mainMenuBar);
        setJMenuBar(mainMenuBar);
        pack();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        topPointY = (screen.height - APPLICATION_HEIGHT) / 2;
        leftPointX = (screen.width - APPLICATION_WIDTH) / 2;
        setBounds(leftPointX, topPointY, APPLICATION_WIDTH, APPLICATION_HEIGHT);
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
