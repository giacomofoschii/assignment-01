package pcd.ass01.multithreading;

import java.util.Random;

public class BoidsController {
    final static double SEPARATION_WEIGHT = 1.0;
    final static double ALIGNMENT_WEIGHT = 1.0;
    final static double COHESION_WEIGHT = 1.0;

    final static int ENVIRONMENT_WIDTH = 1000;
    final static int ENVIRONMENT_HEIGHT = 1000;
    static final double MAX_SPEED = 4.0;
    static final double PERCEPTION_RADIUS = 50.0;
    static final double AVOID_RADIUS = 20.0;

    final static int SCREEN_WIDTH = 1000;
    final static int SCREEN_HEIGHT = 800;

    private BoidsSimulator simulator;
    private BoidsModel model;
    private BoidsView view;

    public void setBoidsNumber(final int nBoids) {
        this.model.setBoidsNumber(nBoids);
    }

    public BoidsSimulator getSimulator() {
        return this.simulator;
    }

    public BoidsModel getModel() {
        return this.model;
    }

    public BoidsView getView() {
        return this.view;
    }

    public void initialize() {
        this.model = new BoidsModel(
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS,
                new Random());
        this.view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.simulator = new BoidsSimulator(this);
        this.simulator.runSimulation();
    }

}
