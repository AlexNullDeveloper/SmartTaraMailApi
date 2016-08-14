package ru.smarttara.util;

import javax.swing.JProgressBar;

public class ProgressBarCallbackRunnable implements Runnable {
    private ProcessFrame processFrame;

    private double step;

    public ProgressBarCallbackRunnable(ProcessFrame processFrame, double step) {
        this.processFrame = processFrame;
        this.step = step;
    }

    @Override
    public void run() {
        JProgressBar jProgressBar = processFrame.getjProgressBar();
        String processFrameStr = jProgressBar.getString();

        double increment = increment(processFrameStr);

        jProgressBar.setValue((int) increment);

        jProgressBar.setString(String.format("%.2f",increment) + "%");
    }

    private double increment(String processFrameStr) {
        return Double.parseDouble(getStringWithoutEndChar(processFrameStr)) + step;
    }

    private String getStringWithoutEndChar(String processFrameStr) {
        return processFrameStr.replace(",",".").replace("%","");
    }
}
