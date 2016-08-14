package ru.smarttara.util;

import ru.smarttara.mainFrame.MainFrame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.FlowLayout;

public class ProcessFrame extends JFrame {

    private JLabel label;
    private JProgressBar jProgressBar;
    private double stepOfProgress;

    public ProcessFrame(String nameOfFrame) {
        super(nameOfFrame);
        label = new JLabel("Процесс выполняется");
        jProgressBar = new JProgressBar();
        jProgressBar.setStringPainted(true);

        setBounds(MainFrame.getLeftPointX() + 100, MainFrame.getTopPointY() + 100, 280, 100);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(label);
        getContentPane().add(jProgressBar);
        setVisible(true);
    }

    public double getStepOfProgress() {
        return stepOfProgress;
    }

    public void setStepOfProgress(double stepOfProgress) {
        this.stepOfProgress = stepOfProgress;
    }

    public JProgressBar getjProgressBar() {
        return jProgressBar;
    }

    public void setjProgressBar(JProgressBar jProgressBar) {
        this.jProgressBar = jProgressBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProcessFrame processFrame = new ProcessFrame("тестовое окно");
            ProgressBarRunnable progressBarRunnable = new ProgressBarRunnable(processFrame, 1.00);
            new Thread(progressBarRunnable).start();
            });
    }
}
