package ru.smarttara.util;

import java.awt.EventQueue;

public class ProgressBarRunnable implements Runnable {

    private ProcessFrame processFrame;
    private double step;

    public ProgressBarRunnable(ProcessFrame processFrame, double step) {
        this.processFrame = processFrame;
        this.step = step;
    }

    @Override
    public void run() {
            ProgressBarCallbackRunnable progressBarCallbackRunnable = new ProgressBarCallbackRunnable(processFrame,step);
            EventQueue.invokeLater(progressBarCallbackRunnable);
    }
}
