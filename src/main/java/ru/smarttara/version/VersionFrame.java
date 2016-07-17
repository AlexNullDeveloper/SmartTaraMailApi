package ru.smarttara.version;


import javax.swing.*;
import java.awt.*;

/**
 * @author a.talismanov
 * @version 1.0.0 08.06.2016
 */
public class VersionFrame extends JFrame {

    private static final String COPYRIGHT_SYMBOL = "\u00a9";
    private static final String AUTOR = "Автор: \t Талисманов Александр";
    private static final String COPYRIGHT_STRING = COPYRIGHT_SYMBOL + " Talismanov Inc. Все права защищены";
    private static final String VERSION_OF_PROGRAM = "Версия программы: \t 1.0.0";
    private static final int WIDTH_OF_FRAME = 240;
    private static final int HEIGHT_OF_FRAME = 80;
    private static final int X_POS_FROMLEFT_CORNER_OF_SCREEN = 600;
    private static final int Y_POS_FROMLEFT_CORNER_OF_SCREEN = 350;
    private static final int ROWS = 3;
    private static final int COLS = 1;
    private static final int HGAP = 1;
    private static final int VGAP = 1;
    private final JPanel mainPanel;

    public VersionFrame(String title) throws HeadlessException {
        super(title);
        mainPanel = (JPanel) getContentPane();
        init();
    }

    private void init() {
        this.setResizable(false);
        mainPanel.setLayout(new GridLayout(ROWS, COLS, HGAP, VGAP));
        mainPanel.add(new JLabel(VERSION_OF_PROGRAM));
        mainPanel.add(new JLabel(AUTOR));
        mainPanel.add(new JLabel(COPYRIGHT_STRING));
        this.pack();
        this.setBounds(X_POS_FROMLEFT_CORNER_OF_SCREEN, Y_POS_FROMLEFT_CORNER_OF_SCREEN, WIDTH_OF_FRAME, HEIGHT_OF_FRAME);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        VersionFrame versionFrame = new VersionFrame("Версия");
    }
}