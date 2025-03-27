package pcd.ass01.multithreading.jpftesting;

import javax.swing.*;

public class JPFBoidsView {

    private JButton stopButton, pauseButton;
    private boolean isRunning;
    private JPFBoidsSimulator simulator;

    public JPFBoidsView() {
        this.isRunning = true;

        stopButton.addActionListener(e -> {
            simulator.stopSimulation();
            System.exit(0);
        });

        pauseButton.addActionListener(e -> {
            if (isRunning) {
                simulator.pauseSimulation();
            } else {
                simulator.resumeSimulation();
            }
            isRunning = !isRunning;
        });
    }
}