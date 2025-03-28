package pcd.ass01.multithreading;

import javax.swing.*;

public class BoidsController {

    private BoidsSimulator boidsSimulator;

    public void setBoidsNumber(final int nBoids, final BoidsModel model) {
        model.setBoidsNumber(nBoids);
    }

    public void setBoidsSimulator(final BoidsSimulator boidsSimulator) {
        this.boidsSimulator = boidsSimulator;
    }

    public void initializeBoidsSimulator(final BoidsModel model) {
        this.boidsSimulator = new BoidsSimulator(model);
    }

    public BoidsSimulator getSimulator() {
        return this.boidsSimulator;
    }

    public void stopAndRestartSimulation(BoidsView view, BoidsModel model) {
        if (boidsSimulator != null) {
            boidsSimulator.stopSimulation();
        }

        boolean restarting = false;
        while (!restarting) {
            String input = JOptionPane.showInputDialog(null, "Inserisci il numero di boids:", 
                    "Numero di Boids", JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                System.exit(0);
            }

            try {
                int nBoids = Integer.parseInt(input);
                if (nBoids > 0) {
                    setBoidsNumber(nBoids, model);
                    initializeBoidsSimulator(model);
                    boidsSimulator.attachView(view);
                    new Thread(boidsSimulator::runSimulation).start();
                    restarting = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Il numero di boids deve essere positivo",
                            "Errore di input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserisci un numero valido",
                        "Errore di input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
